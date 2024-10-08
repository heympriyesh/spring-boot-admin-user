package com.usermanagement.controllers;

import com.usermanagement.Auth.JwtHelper;
import com.usermanagement.config.AuthConfig;
import com.usermanagement.dto.ApiResponse;
import com.usermanagement.dto.UserRequestDto;
import com.usermanagement.dto.UserResponseDto;
import com.usermanagement.dto.req.JwtRequest;
import com.usermanagement.enitiy.UserEntity;
import com.usermanagement.exp.UserAlreadyExistsException;
import com.usermanagement.service.EmailService;
import com.usermanagement.service.PasswordResetService;
import com.usermanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthConfig authConfig;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private JwtHelper helper;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            UserResponseDto userResponseDto = userService.createUser(userRequestDto);
            UserDetails userDetails = userService.loadUserByUsername(userResponseDto.getEmail());
            System.out.println("from db info" + userDetails);
            System.out.println(userDetails.getUsername());
            System.out.println(userDetails.getPassword());

            String token = this.helper.generateToken(userDetails);

            return new ResponseEntity<>(ApiResponse.success("User created successfully!",token), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException ex) {
            // Handle the exception and return an appropriate response
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("User already exists: " + ex.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody JwtRequest jwtRequest) {
        this.doAuthenticate(jwtRequest.getEmail(), jwtRequest.getPassword());
        UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getEmail());
        String token = this.helper.generateToken(userDetails);
//        JwtResponse jwtResponse = JwtResponse.builder().token(token).build();
        return new ResponseEntity<>(ApiResponse.success("Token generated successfully",token), HttpStatus.OK);
    }

    @PostMapping("/forgot")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String token = passwordResetService.generatePasswordResetToken(email);
        emailService.sendPasswordResetMail(email, token);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Password rest link has been sent to your email"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestParam String token, @RequestBody Map<String, String> requesetBody) {
        String newPassword = requesetBody.get("password");
        UserEntity user = passwordResetService.getUserByResetToken(token);

        if (user == null || user.isResetTokenExpired()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Invalid or expired reset token."));

        }

        passwordResetService.resetPassword(user, newPassword);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Password reset successfully."));
    }

    private void doAuthenticate(String email, String password) {
        System.out.println("Login Info");
        System.out.println(email);
        System.out.println(password);
        System.out.println("------");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        System.out.println(authentication +"  do authenticarte");
        try {
            System.out.println("Started");
            manager.authenticate(authentication);
            System.out.println("Eneded");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Authentication successful for user: " + email);
        } catch (BadCredentialsException e) {
            System.out.println("Authentication not-successful for user: " + email);
            throw new BadCredentialsException(" Invalid Username or Password  !!");

        }

    }

}
