@api @run
Feature: Update holiday calendar

  Scenario Outline: Create a new holiday

  Background:
    Given Bearer token is generated

    When POST request is performed with following request body:
      | description            | <dynamic_description> |
      | date                   | <dynamic_date>        |
      | length                 | 0                     |
      | operational_country_id | 1                     |
      | location               | [1]                   |
      | adjustLeave            | true                  |
    Then the status code received in the response is 200
    And the response body parameters match the request body