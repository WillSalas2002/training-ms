package com.epam.training.cucumber.component.controller;

import com.epam.training.controller.TrainingController;
import com.epam.training.dto.TrainingRequest;
import com.epam.training.enums.ActionType;
import com.epam.training.service.TrainingServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc
public class TrainingControllerStepDefinitions {

    @Mock
    private TrainingServiceImpl trainingService;

    @InjectMocks
    private TrainingController controller;

    private MockMvc mockMvc;
    private MvcResult mvcResult;
    private TrainingRequest trainingRequest;
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    private Exception thrownException;
    private AutoCloseable autoCloseable;

    @Before
    public void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        thrownException = null;
    }

    @Before
    public void cleanup() throws Exception {
        autoCloseable.close();
    }

    @Given("I have a valid training request with action {string}")
    public void i_have_a_valid_training_request_with_action(String action) {
        ActionType actionType = ActionType.valueOf(action);
        trainingRequest = TrainingRequest.builder()
                .username("Will.Salas")
                .firstName("Will")
                .lastName("Salas")
                .date(LocalDateTime.now())
                .actionType(actionType)
                .duration(120)
                .isActive(true)
                .build();
        trainingRequest.setActionType(actionType);
    }

    @When("I make a POST request to {string} with the training request")
    public void i_make_a_post_request_to_with_the_training_request(String endpoint) {
        try {
            mvcResult = mockMvc.perform(post(endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(trainingRequest)))
                    .andReturn();
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("the response of training status should be {int}")
    public void the_response_of_training_should_be(Integer statusCode) {
        if (thrownException != null) {
            throw new AssertionError("Request failed with exception: " + thrownException.getMessage(), thrownException);
        }

        assertEquals("Response status should be " + statusCode,
                statusCode, mvcResult.getResponse().getStatus());
    }

    @Then("the training should be saved")
    public void the_training_should_be_saved() {
        verify(trainingService, times(1)).save(trainingRequest);
    }

    @Then("the training should be deleted")
    public void the_training_should_be_deleted() {
        verify(trainingService, times(1)).delete(trainingRequest);
    }
}
