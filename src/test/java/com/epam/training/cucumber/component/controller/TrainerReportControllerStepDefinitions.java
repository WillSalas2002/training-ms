package com.epam.training.cucumber.component.controller;

import com.epam.training.controller.TrainerReportController;
import com.epam.training.model.TrainingSummary;
import com.epam.training.service.TrainerMonthlyReportService;
import io.cucumber.java.After;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TrainerReportControllerStepDefinitions {

    @Mock
    private TrainerMonthlyReportService service;

    @InjectMocks
    private TrainerReportController controller;

    private MockMvc mockMvc;
    private AutoCloseable autoCloseable;

    @Before
    public void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice()
                .build();
    }

    @After
    public void cleanup() throws Exception {
        autoCloseable.close();
    }

    @Given("a trainer exists with username {string}")
    public void a_trainer_exists_with_username(String username) {
        TrainingSummary mockSummary = TrainingSummary.builder()
                .id("123")
                .username(username)
                .firstName("John")
                .lastName("Doe")
                .status(true)
                .years(new ArrayList<>())
                .build();

        when(service.generateMonthlyReport(eq(username))).thenReturn(mockSummary);
    }

    @Given("a trainer does not exist with username {string}")
    public void a_trainer_does_not_exist_with_username(String username) {
        when(service.generateMonthlyReport(eq(username)))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,
                        "Trainer not found with username: " + username));
    }

    @When("I make a GET request to {string}")
    public void i_make_a_get_request_to(String endpoint) throws Exception {
        mockMvc.perform(get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Then("the response body should contain the trainer's summary data")
    public void the_response_body_should_contain_the_trainer_s_summary_data() throws Exception {
        mockMvc.perform(get("/api/v1/trainers/john.doe/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john.doe"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }
}
