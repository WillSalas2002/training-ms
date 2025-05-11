package com.epam.training.service;

import com.epam.training.dto.TrainingRequest;

public interface TrainingService {

    void save(TrainingRequest request);
    void delete(TrainingRequest request);
}
