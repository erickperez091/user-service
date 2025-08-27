package com.example.user_service.services.impl;

import com.example.common.configuration.WebClientFilter;
import com.example.user_service.services.CloudAuthService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

import static com.example.common.utilities.RequestLogEnhancer.enhance;

@Service
@Log4j2
@ConditionalOnProperty(name = "security.firebase.enabled", havingValue = "true")
public class FirebaseAuthService implements CloudAuthService {

    private final WebClient.Builder webClientBuilder;
    private final String firebaseApiKey;
    private final PasswordEncoder passwordEncoder;

    private WebClient webClient;

    @Autowired
    public FirebaseAuthService(
            @Qualifier("normalWebClient") WebClient.Builder webClientBuilder,
            @Value("${security.firebase.api.key}") String firebaseApiKey, PasswordEncoder passwordEncoder) {
        this.webClientBuilder = webClientBuilder;
        this.firebaseApiKey = firebaseApiKey;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void init() {
        String firebaseUrl = "https://identitytoolkit.googleapis.com/v1";
        HttpClient httpClient = new HttpClient() {
            @Override
            public Request newRequest(URI uri) {
                Request request = super.newRequest(uri);
                return enhance(request);
            }
        };

        this.webClient = this.webClientBuilder
                .baseUrl(firebaseUrl)
                .clientConnector(new JettyClientHttpConnector(httpClient))
                .filter(WebClientFilter.logRequest())
                .build();
    }

    public String authenticateUser(String email, String password) {
        String url = "/accounts:signInWithPassword?key=" + firebaseApiKey;

        Map<String, Object> requestBody = Map.of("email", email, "password", password, "returnSecureToken", true);

        try {

            Map<String, Object> response = this.webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                            Mono.error(new BadCredentialsException("ERROR 400"))
                    )
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                            Mono.error(new BadCredentialsException("ERROR 500")))
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .block();

            if (Objects.nonNull(response) && response.containsKey("idToken")) {
                return (String) response.get("idToken");
            }
            throw new BadCredentialsException("Invalid Credentials");
        } catch (WebClientResponseException ex) {
            throw new BadCredentialsException("Invalid Credentials");
        }
    }

    public void createUser(String email, String password) {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setEmailVerified(true)
                    .setDisabled(false);

            FirebaseAuth.getInstance().createUser(request);
        } catch (FirebaseAuthException ex) {
            throw new RuntimeException("Error creating user: " + ex.getMessage());
        }
    }

}
