package com.epam.training.controller;

import com.epam.training.model.TrainingSummary;
import com.epam.training.service.TrainerMonthlyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trainers")
public class TrainerReportController {

    private final TrainerMonthlyReportService service;

    @GetMapping("{username}/summary")
    public TrainingSummary summarize(@PathVariable("username") String username) {
        return service.generateMonthlyReport(username);
    }
}
