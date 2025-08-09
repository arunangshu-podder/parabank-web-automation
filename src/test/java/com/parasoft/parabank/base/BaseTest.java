package com.parasoft.parabank.base;

import com.aventstack.extentreports.ExtentTest;
import com.parasoft.parabank.controller.DriverController;
import com.parasoft.parabank.controller.LogController;
import com.parasoft.parabank.controller.PropertiesController;
import com.parasoft.parabank.controller.ReportsController;
import com.parasoft.parabank.utility.Constants;
import com.parasoft.parabank.utility.FileUtils;
import io.cucumber.java.*;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.time.Duration;

public class BaseTest {
    @BeforeAll
    public static void setup() {
        try {
            FileUtils.clearFolder("target/reports");
            PropertiesController.getConfigProperties();
            ReportsController.initiateReporter("Automation Report", "Cucumber Selenium Tests");
        } catch (IOException e) {
            e.printStackTrace();
            LogController.error("Error while initializing properties: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Before
    public void scenarioSetup(Scenario scenario) {
        try {
            startScenario(scenario);
            launchBrowser();
            ReportsController.logInfo("Browser launched successfully for test " + scenario.getName());
        } catch (RuntimeException e) {
            e.printStackTrace();
            ReportsController.failTest("Error while starting test: " + e.getMessage());
            throw new RuntimeException("Error while starting test: " + e);
        }
    }

    @After
    public void scenarioTeardown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                ReportsController.failTest("Scenario failed: " + scenario.getName());
            } else {
                ReportsController.passTest("Scenario passed");
            }
            LogController.info("Execution completed for test " + scenario.getName());
        } catch (RuntimeException e) {
            e.printStackTrace();
            ReportsController.failTest("Error while finishing test. Scenario failed: " + scenario.getName());
        } finally {
            ReportsController.removeTest();
            DriverController.killDriver();
        }
    }

    @AfterAll
    public static void tearDown() {
        ReportsController.buildReport();
    }

    private void startScenario(Scenario scenario) {
        ExtentTest scn = ReportsController.getExtentReports().createTest(scenario.getName());
        ReportsController.setTest(scn);
    }

    private void launchBrowser() {
        WebDriver driver = DriverController.getDriver();
        driver.get(Constants.APP_URL);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.IMPLICIT_WAIT));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Constants.PAGELOAD_WAIT));
    }
}
