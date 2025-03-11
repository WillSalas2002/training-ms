package com.epam.training.service;

import com.epam.training.dto.TrainingRequest;
import com.epam.training.model.Trainer;
import com.epam.training.model.TrainingSession;
import com.epam.training.repository.TrainerRepository;
import com.epam.training.repository.TrainingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainerRepository trainerRepository;
    private final TrainingSessionRepository trainingSessionRepository;

    public void save(TrainingRequest trainingRequest) {
        Trainer trainer = Trainer.builder()
                .username(trainingRequest.getUsername())
                .firstName(trainingRequest.getFirstName())
                .lastName(trainingRequest.getLastName())
                .status(trainingRequest.isActive())
                .build();
        trainerRepository.save(trainer);

        TrainingSession trainingSession = TrainingSession.builder()
                .trainerUsername(trainingRequest.getUsername())
                .duration(trainingRequest.getDuration())
                .date(trainingRequest.getDate())
                .build();
        trainingSessionRepository.save(trainingSession);
    }

    public void delete(TrainingRequest request) {
        trainerRepository.remove(request.getUsername());
        trainingSessionRepository.remove(request.getUsername());
    }
}
