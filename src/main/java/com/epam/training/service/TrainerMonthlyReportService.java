package com.epam.training.service;

import com.epam.training.dto.TrainerMonthlySummary;

public interface TrainerMonthlyReportService {

    TrainerMonthlySummary generateMonthlyReport(String trainerUsername);
}
