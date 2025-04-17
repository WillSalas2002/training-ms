package com.epam.training.service;

import com.epam.training.dto.TrainingRequest;
import com.epam.training.model.MonthSummary;
import com.epam.training.model.TrainingSummary;
import com.epam.training.model.YearSummary;
import com.epam.training.repository.TrainingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository repository;

    @Override
    public void save(TrainingRequest request) {
        String username = request.getUsername();
        int year = request.getDate().getYear();
        Month month = request.getDate().getMonth();
        int duration = request.getDuration();

        TrainingSummary report = repository.findByUsername(username).orElse(null);
        if (report == null) {
            TrainingSummary newReport = buildTrainingReport(request);
            repository.save(newReport);
            return;
        }

        YearSummary yearSummary = getYearSummary(report, year);
        if (yearSummary == null) {
            MonthSummary monthSummary = new MonthSummary(month, duration);
            report.getYears().add(new YearSummary(year, new ArrayList<>(List.of(monthSummary))));
            repository.save(report);
            return;
        }

        MonthSummary monthSummary = getMonthSummary(yearSummary, month);
        if (monthSummary == null) {
            yearSummary.getMonths().add(new MonthSummary(month, duration));
        } else {
            monthSummary.setDuration(monthSummary.getDuration() + duration);
        }
        repository.save(report);
    }

    // TODO: clarify whether I should delete the the all the trainings or the ones that weren't conducted
    @Override
    public void delete(TrainingRequest request) {
        repository.deleteByUsername(request.getUsername());
    }

    private static TrainingSummary buildTrainingReport(TrainingRequest request) {
        MonthSummary monthSummary = new MonthSummary(request.getDate().getMonth(), request.getDuration());
        YearSummary yearSummary = new YearSummary(request.getDate().getYear(), List.of(monthSummary));

        return TrainingSummary.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .status(request.isActive())
                .years(new ArrayList<>(List.of(yearSummary)))
                .build();
    }

    private static MonthSummary getMonthSummary(YearSummary yearSummary, Month month) {
        return yearSummary.getMonths().stream()
                .filter(m -> m.getMonth().equals(month))
                .findFirst()
                .orElse(null);
    }

    private static YearSummary getYearSummary(TrainingSummary report, int year) {
        return report.getYears().stream()
                .filter(y -> y.getYear() == year)
                .findFirst()
                .orElse(null);
    }
}
