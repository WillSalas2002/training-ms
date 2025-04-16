package com.epam.training.repository;

import com.epam.training.model.TrainingSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
class ScheduledTrainingRepositoryTest {

    @Autowired
    private TrainerTrainingSummaryRepository repository;

    @Test
    void testSaveAndFindByTrainerUsername() {
        String username = "John.Doe";
        TrainingSummary training = buildTrainingSummary(username, LocalDateTime.of(2025, 3, 15, 10, 0));

        repository.save(training);
        List<TrainingSummary> retrievedTrainings = repository.findByUsername(username);

        assertNotNull(retrievedTrainings);
        assertEquals(1, retrievedTrainings.size());
        assertEquals(training, retrievedTrainings.get(0));
    }

    @Test
    void testFindByTrainerUsername_NoTrainings() {
        List<TrainingSummary> retrievedTrainings = repository.findByUsername("unknownTrainer");
        assertEquals(0, retrievedTrainings.size());
    }

    @Test
    void testDeleteByUsername() {
        String username = "John.Doe";
        TrainingSummary conductedTraining = buildTrainingSummary(username, LocalDateTime.now().minusDays(1));
        TrainingSummary futureTraining = buildTrainingSummary(username, LocalDateTime.now().plusDays(1));

        repository.save(conductedTraining);
        repository.save(futureTraining);
        repository.deleteByUsername(username);

        List<TrainingSummary> remainingTrainings = repository.findByUsername(username);

        assertNotNull(remainingTrainings);
        assertEquals(0, remainingTrainings.size());
    }

    private static TrainingSummary buildTrainingSummary(String username, LocalDateTime date) {
        String[] split = username.split("\\.");
        return TrainingSummary.builder()
                .firstName(split[0])
                .lastName(split[1])
                .username(username)
                .status(true)
                .duration(90)
                .date(date)
                .build();
    }
}
