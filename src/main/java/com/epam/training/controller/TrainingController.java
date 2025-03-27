package com.epam.training.controller;

import com.epam.training.dto.TrainingRequest;
import com.epam.training.enums.ActionType;
import com.epam.training.service.ScheduledTrainingServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final ScheduledTrainingServiceImpl scheduledTrainingServiceImpl;
    private final ObjectMapper objectMapper;

    @PostMapping
    public void saveOrDelete(@RequestBody TrainingRequest request) {
        if (request.getActionType().equals(ActionType.DELETE)) {
            scheduledTrainingServiceImpl.delete(request);
            return;
        }
        scheduledTrainingServiceImpl.save(request);
    }

    @JmsListener(destination = "training-ms.queue")
    public void receiveMessage(Message message) {
        try {
            if (message instanceof TextMessage textMessage) {
                String json = textMessage.getText();
                TrainingRequest request = objectMapper.readValue(json, TrainingRequest.class);
                log.info("Received message: {}", request);
                if (request.getActionType().equals(ActionType.DELETE)) {
                    scheduledTrainingServiceImpl.delete(request);
                    return;
                }
                scheduledTrainingServiceImpl.save(request);
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage());
        }
    }
}
