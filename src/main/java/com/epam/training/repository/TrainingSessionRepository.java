package com.epam.training.repository;

import com.epam.training.model.TrainingSession;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TrainingSessionRepository {

    private final Map<String, List<TrainingSession>> trainingSessionStorage = new HashMap<>();

    public void save(TrainingSession trainingSession) {
        if (trainingSessionStorage.get(trainingSession.getTrainerUsername()) == null) {
            ArrayList<TrainingSession> trainingSessions = new ArrayList<>();
            trainingSessions.add(trainingSession);
            trainingSessionStorage.put(trainingSession.getTrainerUsername(), trainingSessions);
        } else {
            List<TrainingSession> trainingSessions = trainingSessionStorage.get(trainingSession.getTrainerUsername());
            trainingSessions.add(trainingSession);
        }
    }

    public List<TrainingSession> findByTrainerUsername(String trainerUsername) {
        return trainingSessionStorage.get(trainerUsername);
    }

    public void remove(String trainerUsername) {
        trainingSessionStorage.remove(trainerUsername);
    }
}
