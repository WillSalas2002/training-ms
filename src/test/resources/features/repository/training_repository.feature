# language: en
Feature: Training Repository Operations
  As a system user
  I want to perform CRUD operations on training summaries
  So that I can manage training data effectively

  Background:
    Given the training database is empty

  Scenario: Save and find training summary by username
    Given a trainer in the repository with username "John.Doe"
    When a training summary is created for the trainer on "2025-03-15T10:00:00"
    And the training summary is saved
    Then the training summary can be found by username "John.Doe"
    And the found training summary has username "John.Doe"

  Scenario: Find training summary for non-existent username
    When I search for a training summary with username "John.Doe"
    Then no training summary is found

  Scenario: Delete training summaries by username
    Given a trainer with username "John.Doe"
    And a training summary is created for the trainer on "2024-05-10T10:00:00"
    And the training summary is saved
    And another training summary is created for the trainer on "2026-05-10T10:00:00"
    And the training summary is saved
    When I delete all training summaries for username "John.Doe"
    Then no training summary is found when searching for "John.Doe"