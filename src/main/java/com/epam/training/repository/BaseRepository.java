package com.epam.training.repository;

import com.epam.training.model.ScheduledTraining;

import java.util.List;

public interface BaseRepository<ENTITY_TYPE> {

    void save(ENTITY_TYPE entityType);
    List<ScheduledTraining> findByTrainerUsername(String trainerUsername);
    void deleteByUsername(String trainerUsername);
}
