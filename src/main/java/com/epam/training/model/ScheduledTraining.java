package com.epam.training.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ScheduledTraining {

    private Trainer trainer;
    private Integer duration;
    private LocalDateTime date;
}
