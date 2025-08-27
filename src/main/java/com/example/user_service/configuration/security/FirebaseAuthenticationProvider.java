package com.example.user_service.configuration.security;

import com.example.user_service.services.CloudAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Log4j2
@Component
@ConditionalOnProperty(name = "security.firebase.enabled", havingValue = "true")
public class FirebaseAuthenticationProvider implements AuthenticationProvider {

    private final CloudAuthService cloudAuthService;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        this.cloudAuthService.authenticateUser(username, password);


        if(Objects.isNull(userDetails)){
            throw new AuthenticationException("User not found") {};
        }

        if(!userDetails.isAccountNonLocked()){
            throw new LockedException("User account is locked");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
