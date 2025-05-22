package com.epam.training.cucumber.component.service;

import com.epam.training.dto.TrainingRequest;
import com.epam.training.model.MonthSummary;
import com.epam.training.model.TrainingSummary;
import com.epam.training.model.YearSummary;
import com.epam.training.repository.TrainingRepository;
import com.epam.training.service.TrainingServiceImpl;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TrainingServiceStepDefinitions {

    @Mock
    private TrainingRepository trainingRepository;
    @InjectMocks
    private TrainingServiceImpl trainingService;

    private TrainingRequest trainingRequest;
    private TrainingSummary existingTrainingSummary;

    @Captor
    private ArgumentCaptor<TrainingSummary> trainingSummaryCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("a training request with the following details:")
    public void aTrainingRequestWithTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> row = rows.get(0);

        trainingRequest = TrainingRequest.builder()
                .username(row.get("username"))
                .firstName(row.get("firstName"))
                .lastName(row.get("lastName"))
                .date(LocalDateTime.parse(row.get("date"), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .duration(Integer.parseInt(row.get("duration")))
                .isActive(Boolean.parseBoolean(row.get("active")))
                .build();
    }

    @Given("no existing training record is found for username {string}")
    public void noExistingTrainingRecordIsFoundForUsername(String username) {
        when(trainingRepository.findByUsername(username)).thenReturn(Optional.empty());
    }

    @Given("an existing training record is found for username {string} with year {int}")
    public void anExistingTrainingRecordIsFoundForUsernameWithYear(String username, int year) {
        // Create training summary with a different year than the one in the request
        MonthSummary monthSummary = new MonthSummary(Month.JANUARY, 100);
        YearSummary yearSummary = new YearSummary(year, new ArrayList<>(List.of(monthSummary)));

        existingTrainingSummary = TrainingSummary.builder()
                .username(username)
                .firstName("Existing")
                .lastName("User")
                .status(true)
                .years(new ArrayList<>(List.of(yearSummary)))
                .build();

        when(trainingRepository.findByUsername(username)).thenReturn(Optional.of(existingTrainingSummary));
    }

    @Given("an existing training record is found for username {string} with year {int} but without month {}")
    public void anExistingTrainingRecordIsFoundForUsernameWithYearButWithoutMonth(String username, int year, Month month) {
        // Create a year summary with a different month than the one in the request
        Month differentMonth = month == Month.JANUARY ? Month.FEBRUARY : Month.JANUARY;
        MonthSummary monthSummary = new MonthSummary(differentMonth, 100);
        YearSummary yearSummary = new YearSummary(year, new ArrayList<>(List.of(monthSummary)));

        existingTrainingSummary = TrainingSummary.builder()
                .username(username)
                .firstName("Existing")
                .lastName("User")
                .status(true)
                .years(new ArrayList<>(List.of(yearSummary)))
                .build();

        when(trainingRepository.findByUsername(username)).thenReturn(Optional.of(existingTrainingSummary));
    }

    @Given("an existing training record is found for username {string} with year {int} and month {} with duration {int}")
    public void anExistingTrainingRecordIsFoundForUsernameWithYearAndMonth(String username, int year, Month month, int duration) {
        MonthSummary monthSummary = new MonthSummary(month, duration);
        YearSummary yearSummary = new YearSummary(year, new ArrayList<>(List.of(monthSummary)));

        existingTrainingSummary = TrainingSummary.builder()
                .username(username)
                .firstName("Existing")
                .lastName("User")
                .status(true)
                .years(new ArrayList<>(List.of(yearSummary)))
                .build();

        when(trainingRepository.findByUsername(username)).thenReturn(Optional.of(existingTrainingSummary));
    }

    @When("the training record is saved")
    public void theTrainingRecordIsSaved() {
        when(trainingRepository.save(any(TrainingSummary.class))).thenAnswer(invocation -> invocation.getArgument(0));
        trainingService.save(trainingRequest);
    }

    @When("the training record is deleted")
    public void theTrainingRecordIsDeleted() {
        doNothing().when(trainingRepository).deleteByUsername(trainingRequest.getUsername());
        trainingService.delete(trainingRequest);
    }

    @Then("a new training record should be created and saved")
    public void aNewTrainingRecordShouldBeCreatedAndSaved() {
        verify(trainingRepository).findByUsername(trainingRequest.getUsername());
        verify(trainingRepository).save(trainingSummaryCaptor.capture());

        TrainingSummary savedSummary = trainingSummaryCaptor.getValue();
        assertEquals(trainingRequest.getUsername(), savedSummary.getUsername());
        assertEquals(trainingRequest.getFirstName(), savedSummary.getFirstName());
        assertEquals(trainingRequest.getLastName(), savedSummary.getLastName());
        assertEquals(trainingRequest.isActive(), savedSummary.isStatus());
        assertEquals(1, savedSummary.getYears().size());

        YearSummary yearSummary = savedSummary.getYears().get(0);
        assertEquals(trainingRequest.getDate().getYear(), yearSummary.getYear());
        assertEquals(1, yearSummary.getMonths().size());

        MonthSummary monthSummary = yearSummary.getMonths().get(0);
        assertEquals(trainingRequest.getDate().getMonth(), monthSummary.getMonth());
        assertEquals(trainingRequest.getDuration(), monthSummary.getDuration());
    }

    @Then("a new year summary should be added to the existing training record")
    public void aNewYearSummaryShouldBeAddedToTheExistingTrainingRecord() {
        verify(trainingRepository).save(trainingSummaryCaptor.capture());

        TrainingSummary savedSummary = trainingSummaryCaptor.getValue();
        assertEquals(existingTrainingSummary.getUsername(), savedSummary.getUsername());
        assertEquals(2, savedSummary.getYears().size());

        // Find the new year summary that was added
        YearSummary newYearSummary = savedSummary.getYears().stream()
                .filter(y -> y.getYear() == trainingRequest.getDate().getYear())
                .findFirst()
                .orElse(null);

        assertNotNull(newYearSummary);
        assertEquals(1, newYearSummary.getMonths().size());

        MonthSummary monthSummary = newYearSummary.getMonths().get(0);
        assertEquals(trainingRequest.getDate().getMonth(), monthSummary.getMonth());
        assertEquals(trainingRequest.getDuration(), monthSummary.getDuration());
    }

    @Then("a new month summary should be added to the existing year summary")
    public void aNewMonthSummaryShouldBeAddedToTheExistingYearSummary() {
        verify(trainingRepository).save(trainingSummaryCaptor.capture());

        TrainingSummary savedSummary = trainingSummaryCaptor.getValue();
        YearSummary yearSummary = savedSummary.getYears().get(0);
        assertEquals(2, yearSummary.getMonths().size());

        // Find the new month summary that was added
        MonthSummary newMonthSummary = yearSummary.getMonths().stream()
                .filter(m -> m.getMonth() == trainingRequest.getDate().getMonth())
                .findFirst()
                .orElse(null);

        assertNotNull(newMonthSummary);
        assertEquals(trainingRequest.getDuration(), newMonthSummary.getDuration());
    }

    @Then("the month summary duration should be updated to {int}")
    public void theMonthSummaryDurationShouldBeUpdatedTo(int expectedDuration) {
        verify(trainingRepository).save(trainingSummaryCaptor.capture());

        TrainingSummary savedSummary = trainingSummaryCaptor.getValue();
        YearSummary yearSummary = savedSummary.getYears().get(0);

        MonthSummary monthSummary = yearSummary.getMonths().get(0);
        assertEquals(expectedDuration, monthSummary.getDuration());
    }

    @Then("the updated training record should be saved")
    public void theUpdatedTrainingRecordShouldBeSaved() {
        verify(trainingRepository, times(1)).save(any(TrainingSummary.class));
    }

    @Then("the training record should be deleted for username {string}")
    public void theTrainingRecordShouldBeDeletedForUsername(String username) {
        verify(trainingRepository).deleteByUsername(username);
    }
}