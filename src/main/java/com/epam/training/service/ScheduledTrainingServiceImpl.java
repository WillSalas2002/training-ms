package com.epam.training.service;

import com.epam.training.dto.TrainingRequest;
import com.epam.training.model.ScheduledTraining;
import com.epam.training.model.Trainer;
import com.epam.training.repository.ScheduledTrainingRepository;
import com.epam.training.util.TransactionContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTrainingServiceImpl implements ScheduledTrainingService {

    private final ScheduledTrainingRepository scheduledTrainingRepository;

    @Override
    public void save(TrainingRequest request) {
        log.info("Transaction ID: {}. Starting to save trainer data: {}", TransactionContext.getTransactionId(), request);

        ScheduledTraining scheduledTraining = ScheduledTraining.builder()
                .trainer(Trainer.builder()
                        .username(request.getUsername())
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .status(request.isActive())
                        .build()
                )
                .duration(request.getDuration())
                .date(request.getDate())
                .build();
        scheduledTrainingRepository.save(scheduledTraining);
    }

    @Override
    public void delete(TrainingRequest request) {
        log.info("Transaction ID: {}. Starting to delete trainer with data: {}", TransactionContext.getTransactionId(), request);
        scheduledTrainingRepository.deleteByUsername(request.getUsername());
    }
}
