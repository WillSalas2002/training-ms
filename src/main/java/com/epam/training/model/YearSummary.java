package com.epam.training.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class YearSummary {
    private int year;
    private List<MonthSummary> months;

    public List<MonthSummary> getMonths(){
        if (months == null) {
            months = new ArrayList<>();
        }
        return months;
    }
}
