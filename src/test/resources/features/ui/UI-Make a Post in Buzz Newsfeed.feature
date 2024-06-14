@UI @Run
Feature: Make a Post in Buzz Newsfeed

  Scenario: Verify Post functionality
    Given user is logged in OrangeHRM system
    When user makes a post in Buzz Newsfeed
    Then a new post is displayed on the top of the page
