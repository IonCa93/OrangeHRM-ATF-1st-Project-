@UI @run
Feature: Make a Post in Buzz Newsfeed

  Scenario: Verify Post functionality
    Given user is logged in OrangeHRM system
    When user clicks on the Buzz menu-item from the left pane
    And "What’s on your mind" field is populated with random text
    And Post button is clicked
    Then a new post is displayed on the top of the page
