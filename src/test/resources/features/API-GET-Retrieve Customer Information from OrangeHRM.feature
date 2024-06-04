@api @run
Feature: Retrieve Customer Information from OrangeHRM

  Background:
    Given Bearer token is generated

  Scenario: Fetch client information
    When user sends a GET request to retrieve customer information
    Then the response status code should be 200
    And the response body should contain the following customer data:
      | customerId | isDeleted | name                                | description                                              |
      | 1          | 0         | 01 Communique Laboratory Inc(OCQLF) | 01 Communique Laboratory Inc(OCQLF) Description          |
      | 2          | 0         | 1 800 Flowers.com Inc(FLWS)         | 1 800 Flowers.com Inc(FLWS) Description                  |
      | 3          | 0         | 141 Capital Inc(ONCP)               | 141 Capital Inc(ONCP) Description                        |
      | 4          | 0         | 1847 Holdings LLC(0955837D)         | 1847 Holdings LLC(0955837D) Description                  |
      | 5          | 0         | 1867 Western Financial Corp(WFCL)   | 1867 Western Financial Corp(WFCL) Description            |
      | 6          | 0         | 1mage Software Inc(ISOL)            | 1mage Software Inc(ISOL) Description                     |
      | 7          | 0         | 1st Capital Bank(FISB)              | 1st Capital Bank(FISB) Description                       |
      | 8          | 0         | 1st Century Bancshares Inc(FCTY)    | 1st Century Bancshares Inc(FCTY) Description             |
      | 9          | 0         | 1st Colonial Bancorp Inc(FCOB)      | 1st Colonial Bancorp Inc(FCOB) Description               |
      | 10         | 0         | 1st Constitution Bancorp(FCCY)      | 1st Constitution Bancorp(FCCY) Description               |
      | 11         | 0         | Nitso Inegrations                   | Nitso Integration is client whose domain is integrations |
      | 12         | 0         | Test                                | Nitso Integration is client whose domain is integrations |