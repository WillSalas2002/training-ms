# language: en
Feature: Training Service Operations
  As a trainer
  I want to save and delete training records
  So that I can manage training data effectively

  Scenario: Save a training record for a new user
    Given a training request with the following details:
      | username  | firstName | lastName | date                   | duration | active |
      | john.doe  | John      | Doe      | 2025-03-15T10:00:00    | 120      | true   |
    And no existing training record is found for username "john.doe"
    When the training record is saved
    Then a new training record should be created and saved

  Scenario: Save a training record for an existing user with new year
    Given a training request with the following details:
      | username  | firstName | lastName | date                   | duration | active |
      | jane.doe  | Jane      | Doe      | 2025-03-15T10:00:00    | 120      | true   |
    And an existing training record is found for username "jane.doe" with year 2024
    When the training record is saved
    Then a new year summary should be added to the existing training record
    And the updated training record should be saved

  Scenario: Save a training record for an existing user with existing year but new month
    Given a training request with the following details:
      | username  | firstName | lastName | date                   | duration | active |
      | bob.smith | Bob       | Smith    | 2025-03-15T10:00:00    | 120      | true   |
    And an existing training record is found for username "bob.smith" with year 2025 but without month MARCH
    When the training record is saved
    Then a new month summary should be added to the existing year summary
    And the updated training record should be saved

  Scenario: Save a training record for an existing user with existing year and month
    Given a training request with the following details:
      | username  | firstName | lastName | date                   | duration | active |
      | alice.kim | Alice     | Kim      | 2025-03-15T10:00:00    | 120      | true   |
    And an existing training record is found for username "alice.kim" with year 2025 and month MARCH with duration 100
    When the training record is saved
    Then the month summary duration should be updated to 220
    And the updated training record should be saved

  Scenario: Delete a training record
    Given a training request with the following details:
      | username  | firstName | lastName | date                   | duration | active |
      | sam.wilson | Sam      | Wilson   | 2025-03-15T10:00:00    | 120      | true   |
    When the training record is deleted
    Then the training record should be deleted for username "sam.wilson"