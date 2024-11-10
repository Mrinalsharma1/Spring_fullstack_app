Feature: User Login Feature

  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user enters valid credentials
    Then the user should be redirected to the dashboard

  Scenario: Unsuccessful login with invalid credentials
    Given the user is on the login page
    When the user enters invalid credentials
    Then the user should see an error message

  Scenario: Login with empty fields
    Given the user is on the login page
    When the user tries to login without entering credentials
    Then the user should see a validation message