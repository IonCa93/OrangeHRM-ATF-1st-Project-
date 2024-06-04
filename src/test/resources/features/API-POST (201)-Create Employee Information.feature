@api @ignore @run
Feature: Create Employee Information

  Background:
    Given Bearer token is generated

  Scenario: Create a new employee

    When A POST request is performed with following request body:
      | firstName  | Jo                   |
      | middleName | Doe                  |
      | lastName   | Cash                 |
      | employeeId | <dynamic_employeeId> |
      | locationId | <dynamic_locationId> |
      | joinedDate | <dynamic_joinedDate> |
    Then The response status code is 201
    And The response body parameters should match the request body