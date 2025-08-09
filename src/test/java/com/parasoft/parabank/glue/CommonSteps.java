package com.parasoft.parabank.glue;

import com.parasoft.parabank.controller.DataController;
import com.parasoft.parabank.controller.ReportsController;
import com.parasoft.parabank.data.CommonDataObject;
import com.parasoft.parabank.pages.GenericActions;
import com.parasoft.parabank.pages.HomePage;
import com.parasoft.parabank.pages.LogInPage;
import com.parasoft.parabank.utility.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.parasoft.parabank.utility.Constants.DATA;

public class CommonSteps{

    private final ScenarioContext scenarioContext;

    public CommonSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Given("^System loads data for \"(.+)\" from module \"(.+)\"$")
    public void readData(String testCaseId, String moduleName) {
        try {
            ReportsController.logInfo("Started reading data.");

            new DataController().readData(testCaseId, moduleName, scenarioContext);
            ReportsController.passStep("Successfully loaded data.");
        } catch (Exception e) {
            ReportsController.failStep("Failed to load data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Given("User logs in to application")
    public void userLogIn() throws Exception {
        try {
            CommonDataObject data = (CommonDataObject)scenarioContext.get(DATA);

            LogInPage login = new LogInPage();
            login.verifyLoginPanelDisplayed();
            login.login(data.USER, data.PASSWORD);
            ReportsController.logInfo("User login completed with username: " + data.USER);

            HomePage homePage = new HomePage();
            homePage.verifyWelcomeMessageDisplayed();
            ReportsController.passStep("User login successful.");
        } catch (Exception e) {
            ReportsController.failStep("User login failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Given("^User clicks on \"(.+)\" link$")
    public void clickLink(String linkText) {
        try {
            GenericActions actions = new GenericActions();
            actions.clickLink(linkText, String.format("Unable to click on link %s.", linkText));
            ReportsController.passStep(String.format("Clicked on link %s successfully.", linkText));
        } catch (Exception e) {
            ReportsController.failStep(String.format("Failed to click on link %s: ", linkText) + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @When("^User clicks on \"(.+)\" button$")
    public void clickButton(String btnText) {
        try {
            GenericActions actions = new GenericActions();
            actions.clickButton(btnText, String.format("Unable to click on button %s.", btnText));
            ReportsController.passStep(String.format("Clicked on button %s successfully.", btnText));
        } catch (Exception e) {
            ReportsController.failStep(String.format("Failed to click on button %s: ", btnText) + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Then("^Verify the message \"(.+)\" is displayed$")
    public void verifyMessageDisplayed(String msgText) {
        try {
            GenericActions actions = new GenericActions();
            if (actions.verifyElementWithTextDisplayed(msgText)) {
                ReportsController.passStep(String.format("Message %s is displayed.", msgText));
            } else {
                String message = String.format("Message '%s' is not displayed.", msgText);
                throw new Exception(message);
            }
        } catch (Exception e) {
            ReportsController.failStep("Verification failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Given("^Navigates to \"(.+)\" menu option$")
    public void clickMenuOption(String menuOption) {
        try {
            HomePage homePage = new HomePage();
            homePage.clickLink(menuOption, "Unable to click on link " + menuOption);
            ReportsController.passStep(String.format("Clicked on menu option %s successfully.", menuOption));
        } catch (Exception e) {
            ReportsController.failStep(String.format("Failed to click on menu option %s: ", menuOption) + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
