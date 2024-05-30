@ui @run
Feature: Login functionality

  Background:
    Given the OrangeHRM home page is opened

  Scenario: User logs in successfully with valid credentials
    When the user initiates the login process
    Then the [Default] tab is displayed

  @Negative
  Scenario Outline: User logs in successfully with invalid credentials
    When the user initiates the login process with invalid credentials "<user>" and "<pass>"
    Then the "Invalid credentials" error message is displayed

    Examples:
      | user        | pass         |
      | AdminBLAbla | admin123     |
      | Admin       | admin123*/*/ |
      | testABC     | pass2658     |
