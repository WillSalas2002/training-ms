package com.epam.training.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Data
public class ScheduledTraining {

    private Trainer trainer;
    private Integer duration;
    private LocalDateTime date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledTraining that = (ScheduledTraining) o;
        return Objects.equals(trainer, that.trainer) && Objects.equals(duration, that.duration) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainer, duration, date);
    }
}
