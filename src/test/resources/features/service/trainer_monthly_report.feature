# language: en
Feature: Trainer Monthly Report Generation
  As a trainer
  I want to generate monthly reports of my training activities
  So that I can track my progress and performance

  Scenario: Generate monthly report for existing trainer
    Given a trainer with username "john.doe"
    And the trainer has existing training records
    When the monthly report is generated for the trainer
    Then the report should contain the trainer's training summary

  Scenario: Attempt to generate monthly report for non-existent trainer
    Given a trainer with username "non.existent"
    And the trainer has no existing training records
    When the monthly report is generated for the trainer
    Then a NoSuchElementException should be thrown