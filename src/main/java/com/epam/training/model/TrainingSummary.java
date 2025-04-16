package com.epam.training.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Data
@Document(collection = "trainer_training_summary")
public class TrainingSummary {
    @Id
    private String id;
    @Indexed
    private String username;
    private String firstName;
    private String lastName;
    private boolean status;
    private Integer duration;
    private LocalDateTime date;

}
