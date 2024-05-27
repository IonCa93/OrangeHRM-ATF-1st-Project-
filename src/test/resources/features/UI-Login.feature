@ui @run
Feature: Login functionality

  Scenario: User logs in successfully
    Given the OrangeHRM home page is opened
    When the user initiates the login process
    Then the [Default] tab is displayed

  @Negative
  Scenario Outline: Verify negative login scenarios
    Given the OrangeHRM home page is opened
    When the user initiates the login process with invalid credentials "<user>" and "<pass>"
    Then the "Invalid credentials" error message is displayed

    Examples:
      | user        | pass         |
      | AdminBLAbla | admin123     |
      | Admin       | admin123*/*/ |
      | testABC     | pass2658     |
