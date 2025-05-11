package com.epam.training.service;

import com.epam.training.dto.TrainingRequest;
import com.epam.training.model.TrainingSummary;
import com.epam.training.repository.TrainingRepository;
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
    private TrainingRepository repository;

    @InjectMocks
    private TrainingServiceImpl service;

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
        service.save(trainingRequest);

        ArgumentCaptor<TrainingSummary> captor = ArgumentCaptor.forClass(TrainingSummary.class);
        verify(repository, times(1)).save(captor.capture());

        TrainingSummary capturedTraining = captor.getValue();
        assertNotNull(capturedTraining);
        assertEquals("John.Doe", capturedTraining.getUsername());
        assertEquals("John", capturedTraining.getFirstName());
        assertEquals("Doe", capturedTraining.getLastName());
    }

    @Test
    void testSave_WhenTrainingRequestIsNull_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> service.save(null));
        
        verify(repository, never()).save(any(TrainingSummary.class));
    }

    @Test
    void testDelete_ShouldCallRepositoryWithCorrectUsername() {
        service.delete(trainingRequest);

        verify(repository, times(1)).deleteByUsername("John.Doe");
    }
}
