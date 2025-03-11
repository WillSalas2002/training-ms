package com.epam.training.repository;

import java.util.Optional;

public interface BaseRepository<ENTITY_TYPE> {

    void save(ENTITY_TYPE entityType);
    Optional<ENTITY_TYPE> findByTrainerUsername(String trainerUsername);
}
