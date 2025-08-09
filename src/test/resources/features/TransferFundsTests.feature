@TransFunds
Feature: Transfer Funds Test Scenarios

  @TransFunds_TC_001 @SanityTests @RegressionTests
  Scenario Outline: [TransFunds_TC_001] Validate user able to transfer funds from one account to another
    Given System loads data for "<TC_ID>" from module "<Module>"
    And User logs in to application
    And Navigates to "Transfer Funds" menu option
    When User enters transfer details
    And User clicks on "Transfer" button
    Then Verify the message "Transfer Complete!" is displayed
  Examples:
    | Module         | TC_ID             |
    | Transfer_Funds | TransFunds_TC_001 |

  @TransFunds_TC_002 @RegressionTests
  Scenario Outline: [TransFunds_TC_002] Validate user able to view transfer details after funds transferred from one account to another
    Given System loads data for "<TC_ID>" from module "<Module>"
    And User logs in to application
    And Navigates to "Transfer Funds" menu option
    When User enters transfer details
    And User clicks on "Transfer" button
    Then Verify the message "Transfer Complete!" is displayed
    When Navigates to "Accounts Overview" menu option
    And User clicks on From Account
    Then User should be able to see the transfer details
    Examples:
      | Module         | TC_ID             |
      | Transfer_Funds | TransFunds_TC_002 |