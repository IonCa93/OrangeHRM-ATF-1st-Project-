@api @Run
Feature: Create Employee Information

  Scenario Outline: Create a new employee

  Background:
    Given Bearer token is generated

    When A POST request is performed with following request body:
      | firstName  | Johnny               |
      | middleName | Dorian               |
      | lastName   | Cash                 |
      | employeeId | <dynamic_employeeId> |
      | locationId | <dynamic_locationId> |
      | joinedDate | <dynamic_joinedDate> |
    Then The response status code is 201
    And The response body parameters should match the request body