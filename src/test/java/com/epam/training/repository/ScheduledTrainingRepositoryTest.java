package com.epam.training.repository;

import com.epam.training.model.ScheduledTraining;
import com.epam.training.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScheduledTrainingRepositoryTest {

    private ScheduledTrainingRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ScheduledTrainingRepository();
    }

    @Test
    void testSaveAndFindByTrainerUsername() {
        String username = "John.Doe";
        ScheduledTraining training = buildTraining(username, LocalDateTime.of(2025, 3, 15, 10, 0));

        repository.save(training);
        List<ScheduledTraining> retrievedTrainings = repository.findByTrainerUsername(username);

        assertNotNull(retrievedTrainings);
        assertEquals(1, retrievedTrainings.size());
        assertEquals(training, retrievedTrainings.get(0));
    }

    @Test
    void testFindByTrainerUsername_NoTrainings() {
        List<ScheduledTraining> retrievedTrainings = repository.findByTrainerUsername("unknownTrainer");
        assertNull(retrievedTrainings);
    }

    @Test
    void testDeleteByUsername() {
        String username = "John.Doe";
        ScheduledTraining conductedTraining = buildTraining(username, LocalDateTime.now().minusDays(1));
        ScheduledTraining futureTraining = buildTraining(username, LocalDateTime.now().plusDays(1));

        repository.save(conductedTraining);
        repository.save(futureTraining);
        repository.deleteByUsername(username);

        List<ScheduledTraining> remainingTrainings = repository.findByTrainerUsername(username);

        assertNotNull(remainingTrainings);
        assertEquals(1, remainingTrainings.size());
        assertEquals(conductedTraining, remainingTrainings.get(0));
    }

    private static ScheduledTraining buildTraining(String username, LocalDateTime date) {
        return ScheduledTraining.builder()
                .date(date)
                .duration(90)
                .trainer(buildTrainer(username))
                .build();
    }

    private static Trainer buildTrainer(String username) {
        return Trainer.builder()
                .username(username)
                .firstName("John")
                .lastName("Doe")
                .status(true)
                .build();
    }
}
