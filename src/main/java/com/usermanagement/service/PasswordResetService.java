package com.usermanagement.service;

import com.usermanagement.config.AuthConfig;
import com.usermanagement.enitiy.UserEntity;
import com.usermanagement.exception.ResourceNotFoundException;
import com.usermanagement.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthConfig authConfig;

    public String generatePasswordResetToken(String email) {
        Optional<UserEntity> user = userRepo.findByEmail(email);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        String token = UUID.randomUUID().toString();

//        Set Token and expiry time (10 minutes from now)
        UserEntity existingUser = user.get();
        existingUser.setResetPasswordToken(token);
        existingUser.setResetPasswordTokenExpiry(LocalDateTime.now().plusMinutes(15));
        userRepo.save(existingUser);

        return token;
    }

    public UserEntity getUserByResetToken(String token) {
        return userRepo.findByResetPasswordToken(token);
    }

    public void resetPassword(UserEntity user, String newPassword) {
//        user.setPassword(newPassword); // Ensure to encode the password
        user.setPassword(authConfig.passwordEncoder().encode(newPassword));
        user.setResetPasswordToken(null); // Clear the token
        user.setResetPasswordTokenExpiry(null); // Clear the expiration
        userRepo.save(user);
    }
}
