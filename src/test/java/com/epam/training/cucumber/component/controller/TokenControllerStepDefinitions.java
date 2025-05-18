package com.epam.training.cucumber.component.controller;

import com.epam.training.controller.TokenController;
import com.epam.training.service.JwtService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TokenControllerStepDefinitions {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private TokenController tokenController;

    private MockMvc mockMvc;
    private MvcResult mvcResult;
    private AutoCloseable autoCloseable;
    private final String MOCK_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0cmFpbmluZy1tcyJ9.signature";

    @Before
    public void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tokenController)
                .setControllerAdvice() // Add global exception handler if exists
                .build();
    }

    @After
    public void cleanUp() throws Exception {
        autoCloseable.close();
    }

    @When("I make a GET request to endpoint {string}")
    public void i_make_a_get_request_to(String endpoint) throws Exception {
        when(jwtService.generateToken()).thenReturn(MOCK_TOKEN);
        mvcResult = mockMvc.perform(get(endpoint))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Then("the response status should be ok {int}")
    public void the_response_status_should_be(Integer statusCode) {
        assertTrue("Response status should be " + statusCode,
                mvcResult.getResponse().getStatus() == statusCode);
    }

    @Then("the response body should contain a valid JWT token")
    public void the_response_body_should_contain_a_valid_jwt_token() throws Exception {
        String responseBody = mvcResult.getResponse().getContentAsString();
        assertTrue("Response body should contain JWT token", responseBody.equals(MOCK_TOKEN));
    }
}
