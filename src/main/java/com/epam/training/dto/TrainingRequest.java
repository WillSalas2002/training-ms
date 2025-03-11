package com.epam.training.dto;

import com.epam.training.enums.ActionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class TrainingRequest {

    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private LocalDateTime date;
    private Integer duration;
    private ActionType actionType;
}
