@DataPrep @Pre_Run_Scripts
Feature: User Registration

  @UserRegistration_TC_001
  Scenario Outline: [DataPrep_<Scenario_No>] Validate admin is able to register new users for Transfer Funds module testing
    Given System loads data for "<TC_ID>" from module "<Module>"
    And User clicks on "Register" link
    When User enters all mandatory details for user registration
    And User clicks on "Register" button
    Then Verify the message "Your account was created successfully. You are now logged in." is displayed

    Examples:
      | Scenario_No | Module            | TC_ID                      |
      | 1           | User_Registration | UserRegistration_TC_001_01 |
      | 2           | User_Registration | UserRegistration_TC_001_02 |
      | 3           | User_Registration | UserRegistration_TC_001_03 |
      | 4           | User_Registration | UserRegistration_TC_001_04 |
      | 5           | User_Registration | UserRegistration_TC_001_05 |
