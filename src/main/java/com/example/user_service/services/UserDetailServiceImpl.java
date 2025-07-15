package com.example.user_service.services;

import com.example.user_service.entity.CustomUser;
import com.example.user_service.repository.CustomUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final CustomUserRepository customUserRepository;

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        Optional<CustomUser> optionalCustomUser = this.customUserRepository.findByUsername( username );
        return null;
    }
}
