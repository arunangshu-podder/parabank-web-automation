package com.parasoft.parabank.controller;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles reporting functionality using ExtentReports.
 * Provides methods for logging test steps, attaching screenshots, and building reports.
 */
public class ReportsController {
    private static final ExtentReports extentReports = new ExtentReports();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final String REPORT_PATH = System.getProperty("user.dir") + "/target/reports";

    /**
     * Initializes the ExtentSparkReporter with the given document title and report name.
     *
     * @param documentTitle Title of the report document
     * @param reportName Name of the report
     */
    public static void initiateReporter(String documentTitle, String reportName) {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH + "/ExtentReport.html");
        sparkReporter.config().setDocumentTitle(documentTitle);
        sparkReporter.config().setReportName(reportName);
        extentReports.attachReporter(sparkReporter);
    }

    /**
     * Gets the ExtentReports instance.
     *
     * @return ExtentReports object
     */
    public static ExtentReports getExtentReports() {
        return extentReports;
    }

    /**
     * Gets the ExtentTest instance for the current thread.
     *
     * @return ExtentTest object, or null if not set
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }

    /**
     * Sets the ExtentTest instance for the current thread.
     *
     * @param test ExtentTest object to set
     */
    public static void setTest(ExtentTest test) {
        extentTest.set(test);
    }

    /**
     * Removes the ExtentTest instance for the current thread.
     */
    public static void removeTest() {
        extentTest.remove();
    }

    /**
     * Logs a passing step in the report and in the log.
     *
     * @param message Message to log
     */
    public static void passStep(String message) {
        LogController.info(message);
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.PASS, message);
        }
    }

    /**
     * Logs a failing step in the report and in the log.
     *
     * @param message Message to log
     */
    public static void failStep(String message) {
        LogController.error(message);
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.FAIL, message);
        }
    }

    /**
     * Logs an informational step in the report and in the log.
     *
     * @param message Message to log
     */
    public static void logInfo(String message) {
        LogController.info(message);
        ExtentTest test = getTest();
        if (test != null) {
            test.info(message);
        }
    }

    /**
     * Marks the test as passed and attaches a screenshot.
     *
     * @param message Message to log
     */
    public static void passTest(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.pass(message);
            addScreenshotToReport();
        }
    }

    /**
     * Marks the test as failed and attaches a screenshot.
     *
     * @param message Message to log
     */
    public static void failTest(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.fail(message);
            addScreenshotToReport();
        }
    }

    /**
     * Flushes the report to disk.
     */
    public static void buildReport() {
        getExtentReports().flush();
        LogController.info("Generated report: " + REPORT_PATH + "/ExtentReport.html");
    }

    /**
     * Captures a screenshot and attaches it to the report.
     * Handles errors gracefully and logs them.
     */
    private static void addScreenshotToReport() {
        ExtentTest test = getTest();
        if (test == null) return;
        String filePath = takeScreenshot();
        if (filePath != null) {
            test.addScreenCaptureFromPath(filePath);
        }
    }

    /**
     * Takes a screenshot and saves it to the report directory.
     *
     * @return Absolute path to the screenshot file, or null if failed
     */
    private static String takeScreenshot() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String screenshotPath = REPORT_PATH + "/screenshot_" + timestamp + ".png";
        return takeScreenshot(screenshotPath);
    }

    /**
     * Takes a screenshot and saves it to the specified path.
     *
     * @param screenshotPath Path to save the screenshot
     * @return Absolute path to the screenshot file, or null if failed
     */
    private static String takeScreenshot(String screenshotPath) {
        try {
            File src = ((TakesScreenshot) DriverController.getDriver()).getScreenshotAs(OutputType.FILE);
            File destn = new File(screenshotPath);
            Files.copy(src.toPath(), destn.toPath());
            LogController.info("Screenshot saved at: " + screenshotPath);
            return destn.getAbsolutePath();
        } catch (Exception e) {
            LogController.error("Error while capturing screenshot: " + e.getMessage());
            return null;
        }
    }
}
