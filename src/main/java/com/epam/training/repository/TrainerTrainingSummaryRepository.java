package com.epam.training.repository;

import com.epam.training.model.TrainingSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerTrainingSummaryRepository extends MongoRepository<TrainingSummary, String> {

    List<TrainingSummary> findByUsername(String username);
    void deleteByUsername(String username);
}
