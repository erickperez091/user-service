package com.example.user_service.services;

import com.example.common.configuration.WebClientFilter;
import com.example.common.entity.EnumUtil;
import com.example.common.utilities.IdUtil;
import com.example.user_service.dto.LoginAttemptDTO;
import com.example.user_service.entity.LoginAttempt;
import com.example.user_service.repository.LoginAttemptRepository;
import com.example.user_service.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

import static com.example.common.utilities.RequestLogEnhancer.enhance;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;
    private final UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;
    private final IdUtil idUtil;

    private WebClient webClient;

    @Value("${service.general.protocol}")
    private String serviceGeneralProtocol;

    @Value("${auth.service.name}")
    private String authServiceName;

    @Value("${auth.service.base-url}")
    private String authServiceBaseUrl;

    @Value("${auth.service.login-attempt-url}")
    private String loginAttemptsUrl;

    @Value("${security.internal.api.key}")
    private String internalKey;

    @PostConstruct
    private void init() {
        String authServiceUrl = String.format("%s%s/%s", this.serviceGeneralProtocol, this.authServiceName, this.authServiceBaseUrl);
        HttpClient httpClient = new HttpClient() {
            @Override
            public Request newRequest(URI uri) {
                Request request = super.newRequest(uri);
                return enhance(request);
            }
        };

        this.webClient = this.webClientBuilder
                .baseUrl(authServiceUrl)
                .clientConnector(new JettyClientHttpConnector(httpClient))
                .filter(WebClientFilter.logRequest())
                .build();
    }

    @Value("${security.max.attempts}")
    private int maxAttempts;

    public void callUpdateLogingAttempt(String username) {
        LoginAttemptDTO loginAttemptDTO = new LoginAttemptDTO(username);
        this.webClient.post()
                .uri(this.loginAttemptsUrl)
                .header("X-Internal-Key", internalKey)
                .body(BodyInserters.fromValue(loginAttemptDTO))
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }


    public void updateLoginAttempt(LoginAttemptDTO loginAttemptDTO) {
        Optional<LoginAttempt> loginAttemptOptional = this.loginAttemptRepository.findLoginAttemptByUsername(loginAttemptDTO.username());

        loginAttemptOptional.ifPresentOrElse(loginAttempt -> {
            loginAttempt.setAttempts(loginAttempt.getAttempts() + 1);
            this.loginAttemptRepository.save(loginAttempt);
            if (loginAttempt.getAttempts() >= this.maxAttempts) {
                Optional<com.example.user_service.entity.User> userOptional = this.userRepository.findByUsername(loginAttemptDTO.username());
                userOptional.ifPresent(user -> {
                    // TODO lock user
                });
            }
        }, () -> {
            LoginAttempt loginAttempt = new LoginAttempt();
            loginAttempt.setId(idUtil.generateId(EnumUtil.UUIDType.SHORT));
            loginAttempt.setUsername(loginAttemptDTO.username());
            loginAttempt.setAttempts(1);
            this.loginAttemptRepository.save(loginAttempt);
        });
    }
}
