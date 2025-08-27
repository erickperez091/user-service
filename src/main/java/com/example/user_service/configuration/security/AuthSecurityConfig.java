package com.example.user_service.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@RequiredArgsConstructor
public class AuthSecurityConfig {

    @Bean(name = "customAuthenticationManager")
    @ConditionalOnProperty(name = "security.firebase.enabled", havingValue = "true")
    public AuthenticationManager authenticationManager(FirebaseAuthenticationProvider firebaseAuthenticationProvider){
        return new ProviderManager(firebaseAuthenticationProvider);
    }


    /*
    public AuthenticationManager defaultAuthenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // este es el que Spring crea por defecto
    }
     */

    @Bean(name = "customAuthenticationManager")
    @ConditionalOnProperty(name = "security.firebase.enabled", havingValue = "false")
    public AuthenticationManager defaultAuthenticationManager(HttpSecurity http, CustomAuthenticationProvider customAuthenticationProvider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(customAuthenticationProvider)
                .getOrBuild();
    }
}
