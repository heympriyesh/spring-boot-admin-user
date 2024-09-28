package com.usermanagement.Auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usermanagement.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final Logger logger = LoggerFactory.getLogger(JWTAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        // Log the authentication exception
        logger.error("Authentication failed: " + authException.getMessage(), authException);

        ErrorResponse errorResponse = new ErrorResponse(false, "Authentication failed: " + authException.getMessage());
        // Send an error response
        PrintWriter writer = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        writer.println(objectMapper.writeValueAsString(errorResponse));
        writer.flush();
    }

}
