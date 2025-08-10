@ReqLoan
Feature: Request Loan Test Scenarios

  @ReqLoan_TC_001 @SanityTests
  Scenario Outline: [ReqLoan_TC_001] Validate user able to apply for new loan
    Given System loads data for "<TC_ID>" from module "<Module>"
    And User logs in to application
    And Navigates to "Request Loan" menu option
    When User fills in the loan application form with valid details
    And User clicks on "Apply Now" button
    Then Verify the message "Congratulations, your loan has been approved." is displayed
  Examples:
    | Module       | TC_ID          |
    | Request_Loan | ReqLoan_TC_001 |

  @ReqLoan_TC_002 @SanityTests @RegressionTests @Develop
  Scenario Outline: [ReqLoan_TC_002] Validate user is not able to apply for new loan due to insufficient funds
    Given System loads data for "<TC_ID>" from module "<Module>"
    And User logs in to application
    And Navigates to "Request Loan" menu option
    When User fills in the loan application form with valid details
    And User clicks on "Apply Now" button
    Then Verify the message "You do not have sufficient funds for the given down payment." is displayed
    Examples:
      | Module       | TC_ID          |
      | Request_Loan | ReqLoan_TC_002 |
