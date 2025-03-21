package com.epam.training.dto;

import com.epam.training.enums.ActionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class TrainingRequest {

    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Fist name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    private boolean isActive;
    @NotNull(message = "Date should be provided")
    private LocalDateTime date;
    @NotNull(message = "Duration should be provided")
    private Integer duration;
    @NotNull(message = "Action type should be specified")
    private ActionType actionType;
}
