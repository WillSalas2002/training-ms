package com.epam.training.repository;

import com.epam.training.model.MonthSummary;
import com.epam.training.model.TrainingSummary;
import com.epam.training.model.YearSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class ScheduledTrainingRepositoryTest {

    @Autowired
    private TrainingRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void cleanUp() {
        mongoTemplate.getDb().drop();
    }

    @Test
    void testSaveAndFindByTrainerUsername() {
        String username = "John.Doe";
        TrainingSummary training = buildTrainingReport(username, LocalDateTime.of(2025, 3, 15, 10, 0));

        repository.save(training);
        Optional<TrainingSummary> trainingSummaryOptional = repository.findByUsername(username);

        assertTrue(trainingSummaryOptional.isPresent());
        assertEquals(username, trainingSummaryOptional.get().getUsername());
    }

    @Test
    void testFindByTrainerUsername_NoTrainings() {
        Optional<TrainingSummary> trainingSummaryOptional = repository.findByUsername("John.Doe");
        assertFalse(trainingSummaryOptional.isPresent());
    }

    @Test
    void testDeleteByUsername() {
        String username = "John.Doe";
        TrainingSummary conductedTraining = buildTrainingReport(username, LocalDateTime.now().minusDays(1));
        TrainingSummary futureTraining = buildTrainingReport(username, LocalDateTime.now().plusYears(1));

        repository.save(conductedTraining);
        repository.save(futureTraining);
        repository.deleteByUsername(username);

        Optional<TrainingSummary> trainingSummaryOptional = repository.findByUsername(username);

        assertTrue(trainingSummaryOptional.isEmpty());
    }

    private static TrainingSummary buildTrainingReport(String username, LocalDateTime date) {
        MonthSummary monthSummary = new MonthSummary(date.getMonth(), 120);
        YearSummary yearSummary = new YearSummary(date.getYear(), List.of(monthSummary));
        String[] split = username.split("\\.");

        return TrainingSummary.builder()
                .username(username)
                .firstName(split[0])
                .lastName(split[1])
                .status(true)
                .years(new ArrayList<>(List.of(yearSummary)))
                .build();
    }
}
