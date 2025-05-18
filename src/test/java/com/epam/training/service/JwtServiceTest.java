package com.epam.training.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    @Spy
    private JwtService jwtService;

    private final String testSigningKey = "dGVzdFNpZ25pbmdLZXlGb3JUZXNUaW5nUHVycG9zZXNPbmx5QWxzb05lZWRzVG9CZUxvbmc=";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "jwtSigningKey", testSigningKey);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        String token = jwtService.generateToken();

        assertNotNull(token);
        assertFalse(token.isEmpty());

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Base64.getDecoder().decode(testSigningKey))
                .build()
                .parseClaimsJws(token);

        assertEquals(JwtService.DEFAULT_SUBJECT, claims.getBody().getSubject());
        assertNotNull(claims.getBody().getIssuedAt());
    }

    @Test
    void validateToken_WithValidToken_ShouldNotThrowException() {
        String token = jwtService.generateToken();

        assertDoesNotThrow(() -> jwtService.validateToken(token));
    }

    @Test
    void validateToken_WithInvalidSignature_ShouldThrowException() {
        String token = jwtService.generateToken();

        String corruptedToken = token.substring(0, token.length() - 1) + (token.charAt(token.length() - 1) == 'A' ? 'B' : 'A');

        assertThrows(SignatureException.class, () -> jwtService.validateToken(corruptedToken));
    }

    @Test
    void getSigningKey_ShouldReturnValidKey() throws Exception {
        java.lang.reflect.Method method = JwtService.class.getDeclaredMethod("getSigningKey");
        method.setAccessible(true);

        Object result = method.invoke(jwtService);

        assertNotNull(result);
        assertInstanceOf(Key.class, result);
    }
}
