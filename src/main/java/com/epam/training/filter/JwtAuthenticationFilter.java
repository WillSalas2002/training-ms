package com.epam.training.filter;

import com.epam.training.dto.ErrorResponse;
import com.epam.training.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String MESSAGE_INVALID_TOKEN = "Invalid JWT token";
    public static final String MESSAGE_TOKEN_NOT_PROVIDED = "JWT token not provided";
    public static final String EMPTY_STRING = "";
    private final static String[] ALLOWED_ENDPOINTS = {"/api/v1/tokens"};

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain
    ) throws ServletException, IOException {

        if (Arrays.asList(ALLOWED_ENDPOINTS).contains(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String authHeader = request.getHeader(AUTH_HEADER_NAME);
            if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
                returnErrorResponse(response, MESSAGE_TOKEN_NOT_PROVIDED);
                return;
            }
            String token = authHeader.replace(BEARER_PREFIX, EMPTY_STRING);
            jwtService.validateToken(token);

            chain.doFilter(request, response);
        } catch (RuntimeException e) {
            returnErrorResponse(response, MESSAGE_INVALID_TOKEN);
        }
    }

    private void returnErrorResponse(HttpServletResponse response, String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(message);
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(jsonResponse);
    }
}
