package com.epam.training.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Month;

@Data
@AllArgsConstructor
public class MonthSummary {
    private Month month;
    private int duration;
}
