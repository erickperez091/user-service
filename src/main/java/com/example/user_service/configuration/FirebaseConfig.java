package com.example.user_service.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@ConditionalOnProperty(name = "security.firebase.enabled", havingValue = "true")
public class FirebaseConfig {

    @PostConstruct
    public void init() throws IOException {
        try(InputStream accountDetails = new ClassPathResource("serviceAccountKey.json").getInputStream()){
            GoogleCredentials credentials = GoogleCredentials.fromStream(accountDetails);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();

            if(FirebaseApp.getApps().isEmpty()){
                FirebaseApp.initializeApp(options);
            }
        }
    }
}
