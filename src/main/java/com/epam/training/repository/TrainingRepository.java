package com.epam.training.repository;

import com.epam.training.model.TrainingSummary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TrainingRepository extends MongoRepository<TrainingSummary, String> {

    Optional<TrainingSummary> findByUsername(String username);
    void deleteByUsername(String username);
}
