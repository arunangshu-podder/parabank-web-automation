package com.parasoft.parabank.base;

import com.aventstack.extentreports.ExtentTest;
import com.parasoft.parabank.controller.*;
import com.parasoft.parabank.utility.Constants;
import com.parasoft.parabank.utility.DBUtils;
import com.parasoft.parabank.utility.FileUtils;
import io.cucumber.java.*;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
            setPreExecTestDetails(scenario);
            launchBrowser();
            ReportsController.logInfo("Browser launched successfully for test " + scenario.getName());
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            ReportsController.failTest("Error while starting test: " + e.getMessage());
            throw new RuntimeException("Error while starting test: " + e);
        }
    }

    @After
    public void scenarioTeardown(Scenario scenario) {
        try {
            String status = getScenarioStatus(scenario);
            if (status.equalsIgnoreCase("PASSED")) {
                ReportsController.passTest("Scenario passed");
            } else if (status.equalsIgnoreCase("FAILED")) {
                ReportsController.failTest("Scenario failed: " + scenario.getName());
            } else {
                ReportsController.skipTest("Scenario skipped: " + scenario.getName());
            }
            setPostExecTestDetails(scenario);
            recordExecutionDetails();
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

    private void setPreExecTestDetails(Scenario scenario) throws IOException {
        TestController testController = TestController.setInstance();

        String scenarioId = "";
        Pattern pattern = Pattern.compile("\\[(.*?)]");
        Matcher matcher = pattern.matcher(scenario.getName());
        if (matcher.find()) {
            scenarioId = matcher.group(1);
        } else {
            scenarioId = "-";
            LogController.error("No scenario ID found in the scenario name: " + scenario.getName());
        }

        testController.setTestCaseId(scenarioId);
        testController.setTestCaseName(scenario.getName());
        testController.setReleaseVersion(Constants.RELEASE_VERSION);
        testController.setEnvironment(Constants.ENVIRONMENT);
        testController.setPlatform(String.valueOf(Constants.BROWSER_TYPE));
        testController.setExecutionStartTime(new Timestamp(System.currentTimeMillis()));

        Set<String> tags = scenario.getSourceTagNames().stream()
                .filter(tag -> !tag.contains("_TC_")).collect(Collectors.toSet());
        testController.setTags(String.join(",", tags));
    }

    private void setPostExecTestDetails(Scenario scenario) {
        TestController testController = TestController.getInstance();
        testController.setExecutionEndTime(new Timestamp(System.currentTimeMillis()));
        int durationInSeconds = (int) ((testController.getExecutionEndTime().getTime() -
                testController.getExecutionStartTime().getTime()) / 1000);
        testController.setDurationSeconds(durationInSeconds);
        testController.setExecutionStatus(getScenarioStatus(scenario));
    }

    private void recordExecutionDetails() {
        try {
            if (Constants.ANALYTICS_ENABLED) {
                new DBUtils().insertExecutionRecord(TestController.getInstance());
            }
        } catch (IOException e) {
            LogController.error("Error while recording execution details: " + e.getMessage());
        }
    }

    private String getScenarioStatus(Scenario scenario) {
        return scenario.getStatus().name();
    }
}
