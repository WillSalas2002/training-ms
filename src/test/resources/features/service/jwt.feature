Feature: JWT Service operations
  As a service user
  I want to generate and validate JWT tokens
  So that I can secure my application

  Scenario: Generate a valid JWT token
    When I generate a JWT token
    Then the token should be generated successfully
    And the token should contain the default subject

  Scenario: Validate a valid JWT token
    Given I have a valid JWT token
    When I validate the token
    Then the validation should be successful

  Scenario: Validate an invalid JWT token
    Given I have an invalid JWT token
    When I validate the token
    Then the validation should fail with an exception