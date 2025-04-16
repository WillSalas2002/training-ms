package com.epam.training.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return Objects.equals(username, trainer.username) && Objects.equals(firstName, trainer.firstName) && Objects.equals(lastName, trainer.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName);
    }
}
