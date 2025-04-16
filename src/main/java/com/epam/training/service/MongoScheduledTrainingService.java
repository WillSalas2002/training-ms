package com.epam.training.service;

import com.epam.training.dto.TrainingRequest;
import com.epam.training.model.TrainingSummary;
import com.epam.training.repository.TrainerTrainingSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongoScheduledTrainingService {

    private final TrainerTrainingSummaryRepository repository;

    public void save(TrainingRequest request) {
        TrainingSummary summary = buildTrainingSummary(request);
        repository.insert(summary);
    }

    // TODO: DON'T DELETE CONDUCTED TRAININGS!!!
    public void delete(TrainingRequest request) {
        repository.deleteByUsername(request.getUsername());
    }

    private static TrainingSummary buildTrainingSummary(TrainingRequest request) {
        return TrainingSummary.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .status(request.isActive())
                .duration(request.getDuration())
                .date(request.getDate())
                .build();
    }
}
