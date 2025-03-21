package com.epam.training.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Trainer {

    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Fist name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    private boolean status;
}
