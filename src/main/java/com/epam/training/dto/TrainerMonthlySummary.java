package com.epam.training.dto;

import com.epam.training.model.Trainer;
import lombok.Builder;
import lombok.Data;

import java.time.Month;
import java.util.Map;


@Builder
@Data
public class TrainerMonthlySummary {

    private Trainer trainer;
    private Map<Integer, Map<Month, Double>> summary;
}
