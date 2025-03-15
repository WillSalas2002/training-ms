package com.epam.training.service;

import com.epam.training.dto.TrainingRequest;
import com.epam.training.model.Trainer;
import com.epam.training.model.TrainingSession;
import com.epam.training.repository.TrainerRepository;
import com.epam.training.repository.TrainingSessionRepository;
import com.epam.training.util.TransactionContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainerRepository trainerRepository;
    private final TrainingSessionRepository trainingSessionRepository;

    public void save(TrainingRequest request) {
        log.info("Transaction ID: {}. Starting to save trainer data: {}", TransactionContext.getTransactionId(), request);
        Trainer trainer = Trainer.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .status(request.isActive())
                .build();
        trainerRepository.save(trainer);

        TrainingSession trainingSession = TrainingSession.builder()
                .trainerUsername(request.getUsername())
                .duration(request.getDuration())
                .date(request.getDate())
                .build();
        trainingSessionRepository.save(trainingSession);
    }

    public void delete(TrainingRequest request) {
        log.info("Transaction ID: {}. Starting to delete trainer with data: {}", TransactionContext.getTransactionId(), request);
        trainingSessionRepository.remove(request.getUsername());
    }
}
