package com.epam.training.cucumber.component.service;

import com.epam.training.model.MonthSummary;
import com.epam.training.model.TrainingSummary;
import com.epam.training.model.YearSummary;
import com.epam.training.repository.TrainingRepository;
import com.epam.training.service.TrainerMonthlyReportServiceImpl;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class TrainerMonthlyReportStepDefinitions {

    @Mock
    private TrainingRepository trainingRepository;
    @InjectMocks
    private TrainerMonthlyReportServiceImpl reportService;

    private String trainerUsername;
    private TrainingSummary expectedSummary;
    private TrainingSummary actualSummary;
    private Exception thrownException;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("a trainer in the repository with username {string}")
    public void aTrainerWithUsername(String username) {
        this.trainerUsername = username;
    }

    @Given("the trainer has existing training records")
    public void theTrainerHasExistingTrainingRecords() {
        // Current month and year
        int currentYear = LocalDate.now().getYear();
        Month currentMonth = LocalDate.now().getMonth();

        // Create a sample training summary for the trainer
        MonthSummary monthSummary = new MonthSummary(currentMonth, 120);
        YearSummary yearSummary = new YearSummary(currentYear, new ArrayList<>(List.of(monthSummary)));

        expectedSummary = TrainingSummary.builder()
                .username(trainerUsername)
                .firstName("John")
                .lastName("Doe")
                .status(true)
                .years(new ArrayList<>(List.of(yearSummary)))
                .build();

        // Configure the mock repository to return this summary
        when(trainingRepository.findByUsername(trainerUsername)).thenReturn(Optional.of(expectedSummary));
    }

    @Given("the trainer has no existing training records")
    public void theTrainerHasNoExistingTrainingRecords() {
        when(trainingRepository.findByUsername(trainerUsername)).thenReturn(Optional.empty());
    }

    @When("the monthly report is generated for the trainer")
    public void theMonthlyReportIsGeneratedForTheTrainer() {
        try {
            actualSummary = reportService.generateMonthlyReport(trainerUsername);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("the report should contain the trainer's training summary")
    public void theReportShouldContainTheTrainersTrainingSummary() {
        assertNotNull(actualSummary);
        assertEquals(expectedSummary.getUsername(), actualSummary.getUsername());
        assertEquals(expectedSummary.getFirstName(), actualSummary.getFirstName());
        assertEquals(expectedSummary.getLastName(), actualSummary.getLastName());
        assertEquals(expectedSummary.isStatus(), actualSummary.isStatus());

        // Check years
        assertEquals(expectedSummary.getYears().size(), actualSummary.getYears().size());
        YearSummary expectedYear = expectedSummary.getYears().get(0);
        YearSummary actualYear = actualSummary.getYears().get(0);
        assertEquals(expectedYear.getYear(), actualYear.getYear());

        // Check months
        assertEquals(expectedYear.getMonths().size(), actualYear.getMonths().size());
        MonthSummary expectedMonth = expectedYear.getMonths().get(0);
        MonthSummary actualMonth = actualYear.getMonths().get(0);
        assertEquals(expectedMonth.getMonth(), actualMonth.getMonth());
        assertEquals(expectedMonth.getDuration(), actualMonth.getDuration());
    }

    @Then("a NoSuchElementException should be thrown")
    public void aNoSuchElementExceptionShouldBeThrown() {
        assertNotNull(thrownException);
        assertEquals(NoSuchElementException.class, thrownException.getClass());
    }
}
