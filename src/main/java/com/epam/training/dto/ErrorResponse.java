package com.epam.training.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss.SSS")
    private LocalDateTime date = LocalDateTime.now();
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
