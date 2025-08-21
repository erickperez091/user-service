package com.example.user_service.services.impl;

import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        return optionalUser
                .map(userEntity -> new org.springframework.security.core.userdetails.User(userEntity.getUsername(), userEntity.getPassword(), true, true, true, userEntity.isAccountNonLocked(), List.of(new SimpleGrantedAuthority(userEntity.getRole().name()))))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}
