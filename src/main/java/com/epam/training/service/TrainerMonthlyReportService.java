package com.epam.training.service;

import com.epam.training.dto.TrainerMonthlySummary;
import com.epam.training.model.Trainer;
import com.epam.training.model.TrainingSession;
import com.epam.training.repository.TrainerRepository;
import com.epam.training.repository.TrainingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

@Service
@RequiredArgsConstructor
public class TrainerMonthlyReportService {

    private final TrainerRepository trainerRepository;
    private final TrainingSessionRepository trainingSessionRepository;

    public TrainerMonthlySummary generateMonthlyReport(String trainerUsername) {
        Trainer trainer = trainerRepository.findByTrainerUsername(trainerUsername)
                .orElseThrow(NoSuchElementException::new);
        List<TrainingSession> trainingSessions = trainingSessionRepository.findByTrainerUsername(trainerUsername);
        Map<Integer, List<TrainingSession>> yearlySummary = trainingSessions.stream()
                .filter(ts -> ts.getTrainerUsername().equals(trainerUsername))
                .collect(groupingBy(ts -> ts.getDate().getYear()));
        Map<Integer, Map<Month, Integer>> summary = new HashMap<>();
        for (Map.Entry<Integer, List<TrainingSession>> entry : yearlySummary.entrySet()) {
            Integer year = entry.getKey();
            List<TrainingSession> trainingSessionsOfYear = entry.getValue();
            Map<Month, Integer> summaryByMonth = trainingSessionsOfYear
                    .stream()
                    .collect(groupingBy(ts -> ts.getDate().getMonth(),
                            summingInt(TrainingSession::getDuration)));
            summary.put(year, summaryByMonth);
        }
        return TrainerMonthlySummary.builder()
                .username(trainer.getUsername())
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .status(trainer.isStatus())
                .summary(summary)
                .build();
    }
}
