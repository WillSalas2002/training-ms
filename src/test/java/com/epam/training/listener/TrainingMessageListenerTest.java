package com.epam.training.listener;

import com.epam.training.dto.TrainingRequest;
import com.epam.training.enums.ActionType;
import com.epam.training.service.MongoScheduledTrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class TrainingMessageListenerTest {

    private MongoScheduledTrainingService mongoScheduledTrainingService;
    private TrainingMessageListener listener;

    @BeforeEach
    void setUp() {
        mongoScheduledTrainingService = mock(MongoScheduledTrainingService.class);
        listener = new TrainingMessageListener(mongoScheduledTrainingService);
    }

    @Test
    void shouldCallSaveWhenActionTypeIsSave() {
        // Given
        TrainingRequest request = TrainingRequest.builder()
                .actionType(ActionType.ADD)
                .build();

        // When
        listener.receiveMessage(request);

        // Then
        verify(mongoScheduledTrainingService).save(request);
        verify(mongoScheduledTrainingService, never()).delete(request);
    }

    @Test
    void shouldCallDeleteWhenActionTypeIsDelete() {
        // Given
        TrainingRequest request = TrainingRequest.builder()
                .actionType(ActionType.DELETE)
                .build();

        // When
        listener.receiveMessage(request);

        // Then
        verify(mongoScheduledTrainingService).delete(request);
        verify(mongoScheduledTrainingService, never()).save(request);
    }

    @Test
    void shouldThrowExceptionWhenActionTypeIsNull() {
        // Given
        TrainingRequest request = TrainingRequest.builder().build(); // No actionType set

        // Then
        assertThatThrownBy(() -> listener.receiveMessage(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(TrainingMessageListener.MESSAGE_INVALID_ACTION_TYPE);

        verifyNoInteractions(mongoScheduledTrainingService);
    }

}