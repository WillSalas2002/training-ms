package com.epam.training.controller;

import com.epam.training.dto.TrainerMonthlySummary;
import com.epam.training.service.TrainerMonthlyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trainers/{username}/summary")
public class TrainerReportController {

    private final TrainerMonthlyReportService service;

    @GetMapping
    public TrainerMonthlySummary summarize(@PathVariable("username") String username) {
        return service.generateMonthlyReport(username);
    }
}
