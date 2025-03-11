package com.epam.training.repository;

import com.epam.training.model.Trainer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class TrainerRepository {

    private final Map<String, Trainer> trainerStorage = new HashMap<>();

    public void save(Trainer entityType) {
        trainerStorage.put(entityType.getUsername(), entityType);
    }

    public Optional<Trainer> findByTrainerUsername(String trainerUsername) {
        return Optional.ofNullable(trainerStorage.get(trainerUsername));
    }

    public void remove(String trainerUsername) {
        trainerStorage.remove(trainerUsername);
    }
}
