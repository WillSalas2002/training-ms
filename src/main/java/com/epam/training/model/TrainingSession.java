package com.epam.training.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class TrainingSession {

    private String trainerUsername;
    private Integer duration;
    private LocalDateTime date;
}
