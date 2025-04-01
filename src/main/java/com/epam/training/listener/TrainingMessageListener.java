package com.epam.training.listener;

import com.epam.training.dto.TrainingRequest;
import com.epam.training.enums.ActionType;
import com.epam.training.service.ScheduledTrainingServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingMessageListener {

    private final ObjectMapper objectMapper;
    private final ScheduledTrainingServiceImpl scheduledTrainingServiceImpl;

    @JmsListener(destination = "training-ms.queue", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(Message message) throws JMSException, JsonProcessingException {
        try {
            if (message instanceof TextMessage textMessage) {
                String json = textMessage.getText();
                TrainingRequest request = objectMapper.readValue(json, TrainingRequest.class);
                if ("error".equals(request.getUsername())) {
                    throw new RuntimeException("Simulated error");
                }
                log.info("Received message: {}", request);

                if (request.getActionType().equals(ActionType.DELETE)) {
                    scheduledTrainingServiceImpl.delete(request);
                } else {
                    scheduledTrainingServiceImpl.save(request);
                }
            }
        } catch (Exception e) {
            log.error("Error processing message, triggering rollback: {}", e.getMessage());
            throw e;
        }
    }
}
