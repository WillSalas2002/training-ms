package com.epam.training.listener;

import com.epam.training.dto.TrainingRequest;
import com.epam.training.enums.ActionType;
import com.epam.training.service.MongoScheduledTrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingMessageListener {

    public static final String MESSAGE_INVALID_ACTION_TYPE = "Action type cannot be null";
    private final MongoScheduledTrainingService scheduledTrainingServiceImpl;

    @JmsListener(destination = "training-ms.queue", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(TrainingRequest request) {
        log.info("Received message: {}", request);
        if (request.getActionType() == null) {
            throw new RuntimeException(MESSAGE_INVALID_ACTION_TYPE);
        }
        if (ActionType.DELETE.equals(request.getActionType())) {
            scheduledTrainingServiceImpl.delete(request);
        } else {
            scheduledTrainingServiceImpl.save(request);
        }
    }
}
