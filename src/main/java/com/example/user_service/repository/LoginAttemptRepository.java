package com.example.user_service.repository;

import com.example.user_service.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, String> {

    Optional<LoginAttempt> findLoginAttemptByUsername(String username);
}
