Feature: Training Controller API
  As an API client
  I want to save and delete training records
  So that I can manage training data

  Scenario: Successfully save a training record
    Given I have a valid training request with action "ADD"
    When I make a POST request to "/api/v1/trainings" with the training request
    Then the response of training status should be 200
    And the training should be saved

  Scenario: Successfully delete a training record
    Given I have a valid training request with action "DELETE"
    When I make a POST request to "/api/v1/trainings" with the training request
    And the training should be deleted
