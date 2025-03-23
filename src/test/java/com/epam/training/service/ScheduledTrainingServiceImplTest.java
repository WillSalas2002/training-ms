package com.epam.training.service;

import com.epam.training.dto.TrainingRequest;
import com.epam.training.model.ScheduledTraining;
import com.epam.training.repository.ScheduledTrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScheduledTrainingServiceImplTest {

    @Mock
    private ScheduledTrainingRepository scheduledTrainingRepository;

    @InjectMocks
    private ScheduledTrainingServiceImpl scheduledTrainingService;

    private TrainingRequest trainingRequest;

    @BeforeEach
    void setUp() {
        trainingRequest = TrainingRequest.builder()
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .duration(60)
                .date(LocalDateTime.of(2025, 3, 22, 10, 0))
                .build();
    }

    @Test
    void testSave_ShouldCallRepositoryWithCorrectData() {
        scheduledTrainingService.save(trainingRequest);

        ArgumentCaptor<ScheduledTraining> captor = ArgumentCaptor.forClass(ScheduledTraining.class);
        verify(scheduledTrainingRepository, times(1)).save(captor.capture());

        ScheduledTraining capturedTraining = captor.getValue();
        assertNotNull(capturedTraining);
        assertEquals("John.Doe", capturedTraining.getTrainer().getUsername());
        assertEquals("John", capturedTraining.getTrainer().getFirstName());
        assertEquals("Doe", capturedTraining.getTrainer().getLastName());
        assertEquals(60, capturedTraining.getDuration());
        assertEquals(LocalDateTime.of(2025, 3, 22, 10, 0), capturedTraining.getDate());
    }

    @Test
    void testSave_WhenTrainingRequestIsNull_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> scheduledTrainingService.save(null));
        
        verify(scheduledTrainingRepository, never()).save(any(ScheduledTraining.class));
    }

    @Test
    void testDelete_ShouldCallRepositoryWithCorrectUsername() {
        scheduledTrainingService.delete(trainingRequest);

        verify(scheduledTrainingRepository, times(1)).deleteByUsername("John.Doe");
    }
}
