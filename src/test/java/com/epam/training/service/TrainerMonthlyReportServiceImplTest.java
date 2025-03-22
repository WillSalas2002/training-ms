package com.epam.training.service;

import com.epam.training.dto.TrainerMonthlySummary;
import com.epam.training.model.ScheduledTraining;
import com.epam.training.model.Trainer;
import com.epam.training.repository.ScheduledTrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerMonthlyReportServiceImplTest {

    @Mock
    private ScheduledTrainingRepository scheduledTrainingRepository;

    @InjectMocks
    private TrainerMonthlyReportServiceImpl trainerMonthlyReportService;

    private Trainer trainer;
    private ScheduledTraining training1;
    private ScheduledTraining training2;
    private ScheduledTraining training3;

    @BeforeEach
    void setUp() {
        trainer = Trainer.builder()
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .status(true)
                .build();

        training1 = ScheduledTraining.builder()
                .trainer(trainer)
                .date(LocalDateTime.of(2024, Month.MARCH, 5, 14, 0))
                .duration(120)
                .build();

        training2 = ScheduledTraining.builder()
                .trainer(trainer)
                .date(LocalDateTime.of(2024, Month.MARCH, 15, 10, 0))
                .duration(90)
                .build();

        training3 = ScheduledTraining.builder()
                .trainer(trainer)
                .date(LocalDateTime.of(2024, Month.FEBRUARY, 22, 16, 0))
                .duration(60)
                .build();
    }

    @Test
    void testGenerateMonthlyReport_WhenTrainingsExist_ShouldReturnSummary() {
        when(scheduledTrainingRepository.findByTrainerUsername("John.Doe"))
                .thenReturn(List.of(training1, training2, training3));

        TrainerMonthlySummary summary = trainerMonthlyReportService.generateMonthlyReport("John.Doe");

        assertNotNull(summary);
        assertEquals(trainer, summary.getTrainer());
        assertTrue(summary.getSummary().containsKey(2024));
        assertEquals(2, summary.getSummary().get(2024).size());

        // Check February summary (60 min -> 1 hour)
        assertEquals(1.0, summary.getSummary().get(2024).get(Month.FEBRUARY));

        // Check March summary (120 + 90 = 210 min -> 3.5 hours)
        assertEquals(3.5, summary.getSummary().get(2024).get(Month.MARCH));

        verify(scheduledTrainingRepository, times(1)).findByTrainerUsername("John.Doe");
    }

    @Test
    void testGenerateMonthlyReport_WhenNoTrainingsExist_ShouldThrowException() {
        when(scheduledTrainingRepository.findByTrainerUsername("John.Doe"))
                .thenReturn(Collections.emptyList());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                trainerMonthlyReportService.generateMonthlyReport("John.Doe"));

        assertEquals("John.Doe", exception.getMessage());

        verify(scheduledTrainingRepository, times(1)).findByTrainerUsername("John.Doe");
    }

    @Test
    void testGenerateMonthlyReport_WhenRepositoryReturnsNull_ShouldThrowException() {
        when(scheduledTrainingRepository.findByTrainerUsername("John.Doe")).thenReturn(null);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                trainerMonthlyReportService.generateMonthlyReport("John.Doe"));

        assertEquals("John.Doe", exception.getMessage());

        verify(scheduledTrainingRepository, times(1)).findByTrainerUsername("John.Doe");
    }

    @Test
    void testGenerateMonthlyReport_WhenDifferentYearsExist_ShouldGroupByYear() {
        ScheduledTraining training4 = ScheduledTraining.builder()
                .trainer(trainer)
                .date(LocalDateTime.of(2023, Month.DECEMBER, 1, 9, 0))
                .duration(180)
                .build();

        when(scheduledTrainingRepository.findByTrainerUsername("John.Doe"))
                .thenReturn(List.of(training1, training2, training3, training4));

        TrainerMonthlySummary summary = trainerMonthlyReportService.generateMonthlyReport("John.Doe");

        assertNotNull(summary);
        assertEquals(trainer, summary.getTrainer());
        assertEquals(2, summary.getSummary().size()); // 2023 and 2024

        assertEquals(3.0, summary.getSummary().get(2023).get(Month.DECEMBER));
        assertEquals(3.5, summary.getSummary().get(2024).get(Month.MARCH));
        assertEquals(1.0, summary.getSummary().get(2024).get(Month.FEBRUARY));

        verify(scheduledTrainingRepository, times(1)).findByTrainerUsername("John.Doe");
    }

    @Test
    void testGenerateMonthlyReport_WhenTrainingsAreFromSameMonth_ShouldSumHoursCorrectly() {
        ScheduledTraining training4 = ScheduledTraining.builder()
                .trainer(trainer)
                .date(LocalDateTime.of(2024, Month.MARCH, 20, 18, 0))
                .duration(30)
                .build();

        when(scheduledTrainingRepository.findByTrainerUsername("John.Doe"))
                .thenReturn(List.of(training1, training2, training4));

        TrainerMonthlySummary summary = trainerMonthlyReportService.generateMonthlyReport("John.Doe");

        assertNotNull(summary);
        assertEquals(trainer, summary.getTrainer());
        assertEquals(1, summary.getSummary().get(2024).size()); // 2024

        // Check March summary (120 + 90 + 30 = 240 min -> 4 hours)
        assertEquals(4.0, summary.getSummary().get(2024).get(Month.MARCH));

        verify(scheduledTrainingRepository, times(1)).findByTrainerUsername("John.Doe");
    }
}
