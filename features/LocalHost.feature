Feature: Load grails app
  In order to verify the app is running
  I want to run the grails app

  Scenario: Verify app is running
    Given I am on the app default page
    Then I should see only
      """
      grails app is running!
      """