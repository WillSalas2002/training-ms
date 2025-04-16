package com.epam.training.service;

import com.epam.training.dto.Trainer;
import com.epam.training.dto.TrainerMonthlySummary;
import com.epam.training.model.TrainingSummary;
import com.epam.training.repository.TrainerTrainingSummaryRepository;
import com.epam.training.util.TransactionContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerMonthlyReportServiceImpl implements TrainerMonthlyReportService {

    public static final double INDEX_FOR_CONVERTING_MINUTES_TO_HOURS = 60.0;

    private final TrainerTrainingSummaryRepository scheduledTrainingRepository;

    public TrainerMonthlySummary generateMonthlyReport(String trainerUsername) {
        log.info("Transaction ID: {}. Fetching scheduled trainings for trainer: {}",
                TransactionContext.getTransactionId(), trainerUsername);

        List<TrainingSummary> scheduledTrainings = scheduledTrainingRepository.findByUsername(trainerUsername);
        if (scheduledTrainings == null || scheduledTrainings.isEmpty()) {
            log.warn("No scheduled trainings found for trainer: {}", trainerUsername);
            throw new NoSuchElementException(trainerUsername);
        }

        Map<Integer, Map<Month, Double>> monthlySummary = generateMonthlySummary(scheduledTrainings);

        log.info("Transaction ID: {}. Successfully generated summary for trainer: {}",
                TransactionContext.getTransactionId(), trainerUsername);

        TrainingSummary trainingSummary = scheduledTrainings.get(0);
        return TrainerMonthlySummary.builder()
                .trainer(Trainer.builder()
                        .firstName(trainingSummary.getFirstName())
                        .lastName(trainingSummary.getLastName())
                        .username(trainingSummary.getUsername())
                        .status(trainingSummary.isStatus())
                        .build())
                .summary(monthlySummary)
                .build();
    }

    private static Map<Integer, Map<Month, Double>> generateMonthlySummary(List<TrainingSummary> scheduledTrainings) {
        return scheduledTrainings.stream()
                .collect(groupingBy(
                        ts -> ts.getDate().getYear(),
                        groupingBy(
                                ts -> ts.getDate().getMonth(),
                                summingDouble(ts -> (ts.getDuration() / INDEX_FOR_CONVERTING_MINUTES_TO_HOURS))
                        )
                ));
    }
}
