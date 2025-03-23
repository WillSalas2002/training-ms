package com.epam.training.service;

import com.epam.training.dto.TrainerMonthlySummary;
import com.epam.training.model.ScheduledTraining;
import com.epam.training.repository.ScheduledTrainingRepository;
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

    private final ScheduledTrainingRepository scheduledTrainingRepository;

    public TrainerMonthlySummary generateMonthlyReport(String trainerUsername) {
        log.info("Transaction ID: {}. Fetching scheduled trainings for trainer: {}",
                TransactionContext.getTransactionId(), trainerUsername);

        List<ScheduledTraining> scheduledTrainings = scheduledTrainingRepository.findByTrainerUsername(trainerUsername);
        if (scheduledTrainings == null || scheduledTrainings.isEmpty()) {
            log.warn("No scheduled trainings found for trainer: {}", trainerUsername);
            throw new NoSuchElementException(trainerUsername);
        }

        Map<Integer, Map<Month, Double>> monthlySummary = generateMonthlySummary(scheduledTrainings);

        log.info("Transaction ID: {}. Successfully generated summary for trainer: {}",
                TransactionContext.getTransactionId(), trainerUsername);

        return TrainerMonthlySummary.builder()
                .trainer(scheduledTrainings.get(0).getTrainer())
                .summary(monthlySummary)
                .build();
    }

    private static Map<Integer, Map<Month, Double>> generateMonthlySummary(List<ScheduledTraining> scheduledTrainings) {
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
