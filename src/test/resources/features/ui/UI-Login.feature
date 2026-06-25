@UI @Run
Feature: Login functionality

  Background:
    Given the OrangeHRM home page is opened

  Scenario: User logs in successfully with valid credentials
    When the user initiates the login process
    Then the "Dashboard" tab is displayed

  @Negative
  Scenario Outline: User logs in successfully with invalid credentials
    When the user initiates the login process with invalid credentials "<user>" and "<pass>"
    Then the "Invalid credentials" error message is displayed

    Examples:
      | user        | pass         |
      | invalidUserOne | wrongPassOne |
      | invalidUserTwo | wrongPassTwo |
      | testABC     | pass2658     |
