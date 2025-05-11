package com.epam.training.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CucumberHooks {

    @Before
    public void beforeScenario(Scenario scenario) {
        log.info("Starting scenario: {}", scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        log.info("Finished scenario: {} with status: {}", scenario.getName(), scenario.getStatus());
    }
}
