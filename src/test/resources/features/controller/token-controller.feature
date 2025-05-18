Feature: Token Controller API
  As an API client
  I want to retrieve JWT tokens
  So that I can authenticate with the system

  Scenario: Successfully retrieve a JWT token
    When I make a GET request to endpoint "/api/v1/tokens"
    Then the response status should be ok 200
    And the response body should contain a valid JWT token