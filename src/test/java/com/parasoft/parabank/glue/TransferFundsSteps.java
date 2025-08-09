package com.parasoft.parabank.glue;

import com.parasoft.parabank.controller.ReportsController;
import com.parasoft.parabank.data.TransferFundsDataObject;
import com.parasoft.parabank.pages.HomePage;
import com.parasoft.parabank.utility.ScenarioContext;
import io.cucumber.java.en.When;

import static com.parasoft.parabank.utility.Constants.DATA;

public class TransferFundsSteps {
    private final ScenarioContext scenarioContext;

    public TransferFundsSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @When("^User enters transfer details$")
    public void enterTransferDetails() throws Exception {
        try {
            ReportsController.logInfo("Entering transfer details.");

            HomePage homePage = new HomePage();
            TransferFundsDataObject data = (TransferFundsDataObject)scenarioContext.get(DATA);

            homePage.enterTransferDetails(data);
            ReportsController.passStep("Entered transfer details successfully.");
        } catch (Exception e) {
            ReportsController.failStep("Failed to enter transfer details: " + e.getMessage());
            throw new Exception(e);
        }
    }
}
