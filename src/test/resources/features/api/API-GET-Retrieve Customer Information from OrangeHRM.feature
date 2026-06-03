@API @Run @test
Feature: Retrieve Customer Information from OrangeHRM

  Scenario: Fetch client information
    Given Bearer token is generated
    When user sends a GET request
    Then the response status code should be 200
    And the response body should contain the following customer data:
      | customerId | isDeleted | name                                      | description                                                                |
      | 1          | 0         | Apache Software Foundation                | non-profit corporation to support Apache software projects                 |
      | 2          | 0         | Discovery Communications, Inc.            | Global mass media and entertainment company                                |
      | 3          | 0         | The Priceline Group                       | American-based provider of travel and related online services to consumers |
      | 4          | 0         | HR administrator                          | <blank>                                                                    |
      | 5          | 0         | Technical Support                         | <blank>                                                                    |
      | 6          | 0         | Dhiavakar Software Foundation             | Multinational beverage corporation and manufacturer                        |
      | 7          | 0         | Nitso Inegrations                         | Nitso Integration is client whose domain is integrations                   |
      | 8          | 0         | Dhiva Software Foundation                 | Multinational beverage corporation and manufacturer                        |
      | 9          | 0         | Dhivakar Software Foundation              | <null>                                                                     |
      | 10         | 0         | Updated customer-test                     | <blank>                                                                    |
      | 11         | 0         | Dhiva Software Foundation for api testing | Multinational beverage corporation and manufacturer                        |
      | 12         | 0         | new one customer                          | new test                                                                   |
      | 13         | 0         | Zoro corporation                          | Nitso Integration is client whose domain is integrations                   |