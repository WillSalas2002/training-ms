package com.epam.training.service;

import com.epam.training.model.TrainingSummary;

public interface TrainerMonthlyReportService {

    TrainingSummary generateMonthlyReport(String trainerUsername);
}
