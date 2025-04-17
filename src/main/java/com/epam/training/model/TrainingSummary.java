package com.epam.training.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Data
@Document(collection = "trainer_training_summary_2")
public class TrainingSummary {
    @Id
    private String id;
    @Indexed
    private String username;
    private String firstName;
    private String lastName;
    private boolean status;
    private List<YearSummary> years;
}
