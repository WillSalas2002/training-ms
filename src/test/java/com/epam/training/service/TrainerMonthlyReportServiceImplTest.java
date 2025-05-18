package com.epam.training.service;

import com.epam.training.model.MonthSummary;
import com.epam.training.model.TrainingSummary;
import com.epam.training.model.YearSummary;
import com.epam.training.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerMonthlyReportServiceImplTest {

    public static final int DEFAULT_DURATION = 120;
    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainerMonthlyReportServiceImpl trainerMonthlyReportService;

    private TrainingSummary trainingReport;

    @BeforeEach
    void setUp() {
        trainingReport = buildTrainingReport("John.Doe", LocalDateTime.of(2024, 11, 11, 11, 11));
    }

    @Test
    void testGenerateMonthlyReport_WhenTrainingsExist_ShouldReturnSummary() {
        String username = "John.Doe";
        when(trainingRepository.findByUsername(username))
                .thenReturn(Optional.ofNullable(trainingReport));

        TrainingSummary summary = trainerMonthlyReportService.generateMonthlyReport(username);

        assertNotNull(summary);
        assertEquals(username, summary.getUsername());
        assertEquals(1, summary.getYears().size());
        assertEquals(2024, summary.getYears().get(0).getYear());
        assertEquals(1, summary.getYears().get(0).getMonths().size());
        assertEquals(Month.NOVEMBER, summary.getYears().get(0).getMonths().get(0).getMonth());
        assertEquals(DEFAULT_DURATION, summary.getYears().get(0).getMonths().get(0).getDuration());
    }

    @Test
    void testGenerateMonthlyReport_WhenNoTrainingsExist_ShouldThrowException() {
        when(trainingRepository.findByUsername("John.Doe"))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                trainerMonthlyReportService.generateMonthlyReport("John.Doe"));

        verify(trainingRepository, times(1)).findByUsername("John.Doe");
    }

    @Test
    void testGenerateMonthlyReport_WhenRepositoryReturnsNull_ShouldThrowException() {
        when(trainingRepository.findByUsername("John.Doe")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                trainerMonthlyReportService.generateMonthlyReport("John.Doe"));

        verify(trainingRepository, times(1)).findByUsername("John.Doe");
    }

    @Test
    void testGenerateMonthlyReport_WhenDifferentYearsExist_ShouldGroupByYear() {
        String username = "John.Doe";
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 1, 0);
        MonthSummary monthSummary = new MonthSummary(date.getMonth(), DEFAULT_DURATION);
        YearSummary yearSummary = new YearSummary(date.getYear(), List.of(monthSummary));
        trainingReport.getYears().add(yearSummary);

        when(trainingRepository.findByUsername(username))
                .thenReturn(Optional.of(trainingReport));

        TrainingSummary trainingSummary = trainerMonthlyReportService.generateMonthlyReport(username);

        assertNotNull(trainingSummary);
        assertEquals(username, trainingSummary.getUsername());
        assertEquals(2, trainingSummary.getYears().size()); // 2024 and 2025

        assertEquals(2, trainingSummary.getYears().size());
        assertEquals(1, trainingSummary.getYears().get(0).getMonths().size());
        assertEquals(1, trainingSummary.getYears().get(1).getMonths().size());

        verify(trainingRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGenerateMonthlyReport_WhenTrainingsAreFromSameMonth_ShouldSumHoursCorrectly() {
        String username = "John.Doe";
        LocalDateTime date = LocalDateTime.of(2024, 11, 12, 11, 11);
        MonthSummary monthSummary = new MonthSummary(date.getMonth(), DEFAULT_DURATION);
        YearSummary yearSummary = new YearSummary(date.getYear(), List.of(monthSummary));
        trainingReport.getYears().add(yearSummary);

        when(trainingRepository.findByUsername(username))
                .thenReturn(Optional.of(trainingReport));

        TrainingSummary trainingSummary = trainerMonthlyReportService.generateMonthlyReport(username);

        assertNotNull(trainingSummary);
        assertEquals(username, trainingSummary.getUsername());
        assertEquals(1, trainingSummary.getYears().size());
        assertEquals(1, trainingSummary.getYears().get(0).getMonths().size());
        assertEquals(Month.NOVEMBER, trainingSummary.getYears().get(0).getMonths().get(0).getMonth());
        assertEquals(240, trainingSummary.getYears().get(0).getMonths().get(0).getDuration());
        verify(trainingRepository, times(1)).findByUsername(username);
    }

    private static TrainingSummary buildTrainingReport(String username, LocalDateTime date) {
        MonthSummary monthSummary = new MonthSummary(date.getMonth(), DEFAULT_DURATION);
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
