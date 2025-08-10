package com.parasoft.parabank.glue;

import com.parasoft.parabank.controller.ReportsController;
import com.parasoft.parabank.data.RequestLoanDataObject;
import com.parasoft.parabank.data.TransferFundsDataObject;
import com.parasoft.parabank.pages.HomePage;
import com.parasoft.parabank.utility.ScenarioContext;
import io.cucumber.java.en.When;

import static com.parasoft.parabank.utility.Constants.DATA;

public class RequestLoanSteps {
    private final ScenarioContext scenarioContext;

    public RequestLoanSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @When("^User fills in the loan application form with valid details$")
    public void enterLoanDetails() throws Exception {
        try {
            ReportsController.logInfo("Entering loan details.");

            HomePage homePage = new HomePage();
            RequestLoanDataObject data = (RequestLoanDataObject)scenarioContext.get(DATA);

            homePage.enterLoanDetails(data);
            ReportsController.passStep("Entered loan details successfully.");
        } catch (Exception e) {
            ReportsController.failStep("Failed to enter loan details: " + e.getMessage());
            throw new Exception(e);
        }
    }
}
