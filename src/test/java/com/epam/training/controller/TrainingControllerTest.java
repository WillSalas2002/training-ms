package com.epam.training.controller;

import com.epam.training.config.SecurityConfig;
import com.epam.training.dto.TrainingRequest;
import com.epam.training.enums.ActionType;
import com.epam.training.filter.JwtAuthenticationFilter;
import com.epam.training.service.JwtService;
import com.epam.training.service.ScheduledTrainingServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainingController.class)
@ExtendWith(SpringExtension.class)
@Import(SecurityConfig.class)
class TrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ScheduledTrainingServiceImpl scheduledTrainingServiceImpl;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(jwtAuthenticationFilter)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser
    void shouldCallSave_WhenActionIsCreate() throws Exception {
        TrainingRequest request = buildTrainingRequest(ActionType.ADD);
        String token = generateToken();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request)))  // Simulating JSON body
                .andExpect(status().isOk());

        verify(scheduledTrainingServiceImpl, times(1)).save(request);
        verify(scheduledTrainingServiceImpl, never()).delete(any());
    }

    @Test
    @WithMockUser
    void shouldCallDelete_WhenActionIsDelete() throws Exception {
        TrainingRequest request = buildTrainingRequest(ActionType.DELETE);
        String token = generateToken();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(scheduledTrainingServiceImpl, times(1)).delete(request);
        verify(scheduledTrainingServiceImpl, never()).save(any());
    }

    @Test
    void shouldReturnUnauthorized_WhenNotAuthenticated() throws Exception {
        TrainingRequest request = TrainingRequest.builder()
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .duration(60)
                .date(LocalDateTime.of(2025, 3, 22, 10, 0))
                .actionType(ActionType.DELETE)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(scheduledTrainingServiceImpl, never()).save(any());
        verify(scheduledTrainingServiceImpl, never()).delete(any());
    }

    private String generateToken() {
        return jwtService.generateToken();
    }

    private static TrainingRequest buildTrainingRequest(ActionType actionType) {
        return TrainingRequest.builder()
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .duration(60)
                .date(LocalDateTime.of(2025, 3, 22, 10, 0))
                .actionType(actionType)
                .build();
    }
}
