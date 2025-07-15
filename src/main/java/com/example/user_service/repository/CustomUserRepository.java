package com.example.user_service.repository;

import com.example.user_service.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomUserRepository extends JpaRepository< CustomUser, String > {
    Optional< CustomUser> findByUsername( String username );
}
