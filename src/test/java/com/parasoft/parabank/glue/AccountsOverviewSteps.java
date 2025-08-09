package com.parasoft.parabank.glue;

import com.parasoft.parabank.controller.ReportsController;
import com.parasoft.parabank.data.TransferFundsDataObject;
import com.parasoft.parabank.pages.HomePage;
import com.parasoft.parabank.utility.ScenarioContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.parasoft.parabank.utility.Constants.DATA;

public class AccountsOverviewSteps {
    private final ScenarioContext scenarioContext;

    public AccountsOverviewSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }


    @When("^User clicks on From Account$")
    public void clickOnFromAccount() {
        try {
            HomePage homePage = new HomePage();
            TransferFundsDataObject data = (TransferFundsDataObject)scenarioContext.get(DATA);

            homePage.clickLink(data.FROM_ACCOUNT, "Unable to find From Account on Accounts Overview page.");
            ReportsController.passStep("Successfully clicked on From Account.");
        } catch (Exception e) {
            ReportsController.failStep("Failed to click on From Account: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Then("^User should be able to see the transfer details$")
    public void validateTransferDetails() {
        try {
            HomePage homePage = new HomePage();
            TransferFundsDataObject data = (TransferFundsDataObject)scenarioContext.get(DATA);

            homePage.validateTransferDetails(data);
            ReportsController.passStep("Transfer details validated successfully.");
        } catch (Exception e) {
            ReportsController.failStep("Failed to click on From Account: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
