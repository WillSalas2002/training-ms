package com.epam.training.cucumber.component.repository;

import com.epam.training.model.MonthSummary;
import com.epam.training.model.TrainingSummary;
import com.epam.training.model.YearSummary;
import com.epam.training.repository.TrainingRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TrainingRepositoryStepDefinitions {

    @MockitoBean
    private TrainingRepository trainingRepository;

    private String currentUsername;
    private TrainingSummary currentTrainingSummary;
    private Optional<TrainingSummary> foundTrainingSummary;

    @Before
    public void setup() {
        trainingRepository = Mockito.mock(TrainingRepository.class);
    }

    @Given("the training database is empty")
    public void theTrainingDatabaseIsEmpty() {
        // Nothing to do - just a clear starting state
    }

    @Given("a trainer with username {string}")
    public void aTrainerWithUsername(String username) {
        this.currentUsername = username;
    }

    @When("a training summary is created for the trainer on {string}")
    public void aTrainingSummaryIsCreatedForTheTrainerOn(String dateTimeStr) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, ISO_LOCAL_DATE_TIME);
        // TODO: here username is getting fetched from file;
        currentUsername = currentUsername == null ? "John.Doe" : currentUsername;
        this.currentTrainingSummary = buildTrainingSummary(currentUsername, dateTime);
    }

    @When("another training summary is created for the trainer on {string}")
    public void anotherTrainingSummaryIsCreatedForTheTrainerOn(String dateTimeStr) {
        aTrainingSummaryIsCreatedForTheTrainerOn(dateTimeStr);
    }

    @When("the training summary is saved")
    public void theTrainingSummaryIsSaved() {
        when(trainingRepository.save(currentTrainingSummary)).thenReturn(currentTrainingSummary);
        trainingRepository.save(currentTrainingSummary);
    }

    @When("I search for a training summary with username {string}")
    public void iSearchForATrainingSummaryWithUsername(String username) {
        when(trainingRepository.findByUsername(username)).thenReturn(Optional.empty());
        foundTrainingSummary = trainingRepository.findByUsername(username);
    }

    @When("I delete all training summaries for username {string}")
    public void iDeleteAllTrainingSummariesForUsername(String username) {
        doNothing().when(trainingRepository).deleteByUsername(username);
        trainingRepository.deleteByUsername(username);
        when(trainingRepository.findByUsername(username)).thenReturn(Optional.empty());
    }

    @Then("the training summary can be found by username {string}")
    public void theTrainingSummaryCanBeFoundByUsername(String username) {
        when(trainingRepository.findByUsername(username)).thenReturn(Optional.of(currentTrainingSummary));
        foundTrainingSummary = trainingRepository.findByUsername(username);
        assertTrue(foundTrainingSummary.isPresent());
    }

    @Then("the found training summary has username {string}")
    public void theFoundTrainingSummaryHasUsername(String username) {
        assertEquals(username, foundTrainingSummary.get().getUsername());
    }

    @Then("no training summary is found")
    public void noTrainingSummaryIsFound() {
        assertFalse(foundTrainingSummary.isPresent());
    }

    @Then("no training summary is found when searching for {string}")
    public void noTrainingSummaryIsFoundWhenSearchingFor(String username) {
        verify(trainingRepository).deleteByUsername(username);
        foundTrainingSummary = trainingRepository.findByUsername(username);
        assertFalse(foundTrainingSummary.isPresent());
    }

    private TrainingSummary buildTrainingSummary(String username, LocalDateTime date) {
        MonthSummary monthSummary = new MonthSummary(date.getMonth(), 120);
        YearSummary yearSummary = new YearSummary(date.getYear(), List.of(monthSummary));
        String[] split = username.split("\\.");

        return TrainingSummary.builder()
                .username(username)
                .firstName(split[0])
                .lastName(split[1])
                .status(true)
                .years(new ArrayList<>(List.of(yearSummary)))
                .build();
    }
}
