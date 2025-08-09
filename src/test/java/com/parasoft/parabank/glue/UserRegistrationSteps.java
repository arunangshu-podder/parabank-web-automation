package com.parasoft.parabank.glue;

import com.parasoft.parabank.controller.ReportsController;
import com.parasoft.parabank.data.UserRegistrationDataObject;
import com.parasoft.parabank.pages.UserRegistrationPage;
import com.parasoft.parabank.utility.ScenarioContext;
import io.cucumber.java.en.When;

import static com.parasoft.parabank.utility.Constants.DATA;

public class UserRegistrationSteps {
    private final ScenarioContext scenarioContext;

    public UserRegistrationSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @When("^User enters all mandatory details for user registration$")
    public void fillUserDetails() throws Exception {
        try {
            ReportsController.logInfo("Started filling user registration details.");

            UserRegistrationPage userRegistrationPage = new UserRegistrationPage();
            UserRegistrationDataObject data = (UserRegistrationDataObject)scenarioContext.get(DATA);
            userRegistrationPage.fillUserDetails(data);
            ReportsController.passStep("Filled user details successfully.");
        } catch (Exception e) {
            ReportsController.failStep("Failed to fill user details: " + e.getMessage());
            throw new Exception(e);
        }
    }
}
