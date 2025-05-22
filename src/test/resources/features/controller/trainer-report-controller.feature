Feature: Trainer Report Controller API
  As an API client
  I want to retrieve training summaries for trainers
  So that I can view their monthly report data

  Scenario: Successfully retrieve a trainer's summary
    Given a trainer exists with username "john.doe"
    When I make a GET request to "/api/v1/trainers/john.doe/summary"
    And the response body should contain the trainer's summary data

  Scenario: Retrieve summary for non-existent trainer
    Given a trainer does not exist with username "unknown.user"
    When I make a GET request to "/api/v1/trainers/unknown.user/summary"
