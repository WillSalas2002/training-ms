package com.epam.training.controller;

import com.epam.training.dto.TrainingRequest;
import com.epam.training.enums.ActionType;
import com.epam.training.service.MongoScheduledTrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final MongoScheduledTrainingService mongoService;

    @PostMapping
    public void saveOrDelete(@RequestBody TrainingRequest request) {
        if (ActionType.DELETE.equals(request.getActionType())) {
            mongoService.delete(request);
        } else {
            mongoService.save(request);
        }
    }
}
