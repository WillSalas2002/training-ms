package com.epam.training.repository;

import com.epam.training.model.ScheduledTraining;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ScheduledTrainingRepository implements BaseRepository<ScheduledTraining> {

    private final Map<String, List<ScheduledTraining>> scheduledTrainingsStorage = new HashMap<>();

    @Override
    public void save(ScheduledTraining scheduledTraining) {
        String username = scheduledTraining.getTrainer().getUsername();

        if (scheduledTrainingsStorage.get(username) == null) {
            ArrayList<ScheduledTraining> scheduledTrainings = new ArrayList<>();
            scheduledTrainings.add(scheduledTraining);
            scheduledTrainingsStorage.put(username, scheduledTrainings);
        } else {
            List<ScheduledTraining> scheduledTrainings = scheduledTrainingsStorage.get(username);
            scheduledTrainings.add(scheduledTraining);
        }
    }

    @Override
    public List<ScheduledTraining> findByTrainerUsername(String trainerUsername) {
        return scheduledTrainingsStorage.get(trainerUsername);
    }

    @Override
    public void deleteByUsername(String trainerUsername) {
        List<ScheduledTraining> scheduledTrainings = scheduledTrainingsStorage.get(trainerUsername);
        scheduledTrainings.removeIf(ts -> ts.getDate().isAfter(LocalDateTime.now()));
    }
}
