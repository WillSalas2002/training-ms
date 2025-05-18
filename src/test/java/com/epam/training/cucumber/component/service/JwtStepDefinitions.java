package com.epam.training.cucumber.component.service;

import com.epam.training.service.JwtService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class JwtStepDefinitions {

    private static final String TOKEN_SIGN_IN_KEY = "dGVzdEtleUZvckN1Y3VtYmVyVGVzdGluZ1doaWNoTmVlZHNUb0JlQXRMZWFzdDMyQnl0ZXNMb25n";

    @InjectMocks
    private JwtService jwtService;

    private String generatedToken;
    private String providedToken;
    private Exception validationException;
    private AutoCloseable autoCloseable;

    @Before
    public void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtService, "jwtSigningKey", TOKEN_SIGN_IN_KEY);
    }

    @After
    public void cleanUp() throws Exception {
        autoCloseable.close();
    }

    @When("I generate a JWT token")
    public void i_generate_a_jwt_token() {
        generatedToken = jwtService.generateToken();
    }

    @Then("the token should be generated successfully")
    public void the_token_should_be_generated_successfully() {
        assertTrue("Token should be a non-empty string", !generatedToken.isEmpty());
    }

    @Then("the token should contain the default subject")
    public void the_token_should_contain_the_default_subject() {
        // Parse the token to verify subject
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(TOKEN_SIGN_IN_KEY));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(generatedToken)
                .getBody();

        assertEquals("training-ms",
                JwtService.DEFAULT_SUBJECT, claims.getSubject());
    }

    @Given("I have a valid JWT token")
    public void i_have_a_valid_jwt_token() {
        providedToken = jwtService.generateToken();
    }

    @Given("I have an invalid JWT token")
    public void i_have_an_invalid_jwt_token() {
        providedToken = "invalid.jwt.token";
    }

    @When("I validate the token")
    public void i_validate_the_token() {
        try {
            jwtService.validateToken(providedToken);
        } catch (Exception e) {
            validationException = e;
        }
    }

    @Then("the validation should be successful")
    public void the_validation_should_be_successful() {
        assertNull("No exception should be thrown for valid token", validationException);
    }

    @Then("the validation should fail with an exception")
    public void the_validation_should_fail_with_an_exception() {
        assertTrue("Exception should be a JWT exception",
                validationException instanceof JwtException);
    }
}
