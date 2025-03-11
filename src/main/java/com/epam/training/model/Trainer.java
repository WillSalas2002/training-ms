package com.epam.training.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Trainer {

    private String username;
    private String firstName;
    private String lastName;
    private boolean status;
}
