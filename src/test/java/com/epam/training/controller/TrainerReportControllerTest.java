package com.epam.training.controller;

import com.epam.training.filter.JwtAuthenticationFilter;
import com.epam.training.model.MonthSummary;
import com.epam.training.model.TrainingSummary;
import com.epam.training.model.YearSummary;
import com.epam.training.service.JwtService;
import com.epam.training.service.TrainerMonthlyReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainerReportController.class)
@ExtendWith(SpringExtension.class)
class TrainerReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private TrainerMonthlyReportService service;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private TrainingSummary trainingSummary;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        trainingSummary = buildTrainingReport("John.Doe", LocalDateTime.of(2023, 11, 11, 11, 11));
    }

    @Test
    void shouldReturnSummary_WhenTrainerExists() throws Exception {
        when(service.generateMonthlyReport("John.Doe")).thenReturn(trainingSummary);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/trainers/John.Doe/summary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John.Doe"));

        verify(service, times(1)).generateMonthlyReport("John.Doe");
    }

    @Test
    void shouldReturnNotFound_WhenTrainerNotFound() throws Exception {
        when(service.generateMonthlyReport("unknown_user")).thenThrow(new NoSuchElementException("unknown_user"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/trainers/unknown_user/summary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(1)).generateMonthlyReport("unknown_user");
    }

    @Test
    void shouldReturnNotFound_WhenUsernameIsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/trainers//summary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, never()).generateMonthlyReport(anyString());
    }

    private static TrainingSummary buildTrainingReport(String username, LocalDateTime date) {
        MonthSummary monthSummary = new MonthSummary(date.getMonth(), 120);
        YearSummary yearSummary = new YearSummary(date.getYear(), List.of(monthSummary));
        String[] split = username.split("\\.");

        return TrainingSummary.builder()
                .username(username)
                .firstName(split[0])
                .lastName(split[1])
                .status(true)
                .years(new ArrayList<>(List.of(yearSummary)))
                .build();
    }
}
