package com.example.user_service.services.impl;

import com.example.user_service.services.CloudAuthService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "security.firebase.enabled", havingValue = "false")
@Log4j2
public class NoOpCloudAuthService implements CloudAuthService {

    @Override
    public String authenticateUser(String email, String password) {
        try {
            logger.info("[NoOpCloudAuthService][authenticateUser]: START Authenticating user: {}", email);
            return "";
        }
        finally {
            logger.info("[NoOpCloudAuthService][authenticateUser]: END Authenticating user: {}", email);
        }
    }

    @Override
    public void createUser(String email, String password) {
        logger.info("[NoOpCloudAuthService][createUser]: START Creating User no cloud");
        logger.info("[NoOpCloudAuthService][createUser]: END Creating User no cloud");
    }
}
