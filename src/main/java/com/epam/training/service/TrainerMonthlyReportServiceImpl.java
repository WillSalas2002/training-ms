package com.epam.training.service;

import com.epam.training.model.TrainingSummary;
import com.epam.training.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerMonthlyReportServiceImpl implements TrainerMonthlyReportService {

    private final TrainingRepository trainingRepository;

    public TrainingSummary generateMonthlyReport(String trainerUsername) {
        Optional<TrainingSummary> summary = trainingRepository.findByUsername(trainerUsername);
        return summary.orElseThrow(NoSuchElementException::new);
    }
}
