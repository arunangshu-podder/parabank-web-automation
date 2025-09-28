package com.parasoft.parabank.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.parasoft.parabank.utility.Constants;
import com.parasoft.parabank.utility.DateUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;

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
     * Marks the test as failed and attaches a screenshot.
     *
     * @param message Message to log
     */
    public static void skipTest(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.skip(message);
        }
    }

    /**
     * Flushes the report to disk.
     */
    public static void buildReport() {
        getExtentReports().flush();
        LogController.info("Generated report: " + REPORT_PATH + "/ExtentReport.html");
        uploadReport(REPORT_PATH);
    }

    /**
     * Captures a screenshot and attaches it to the report.
     * Handles errors gracefully and logs them.
     */
    private static void addScreenshotToReport() {
        ExtentTest test = getTest();
        if (test == null) return;
        String fileName = takeScreenshot();
        if (fileName != null) {
            test.addScreenCaptureFromPath("./"+fileName);
        }
    }

    /**
     * Takes a screenshot and saves it to the report directory.
     *
     * @return Absolute path to the screenshot file, or null if failed
     */
    private static String takeScreenshot() {
        String timestamp = DateUtils.getCurrentTimestamp("yyyyMMdd_HHmmss_SSS");
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
            return destn.getName();
        } catch (Exception e) {
            LogController.error("Error while capturing screenshot: " + e.getMessage());
            return null;
        }
    }

    /*********************************************************************************
     * For AWS account at org level, follow below steps:                             *
     * 1) Create an IAM group/role for your team.                                    *
     * 2) Attach an  IAM policy to allow only team members to read objects.          *
     * 3) Authenticate team members via IAM Identity Center (SSO) to access reports. *
     *********************************************************************************/
    /**
     * Uploads all files from a local report folder to an Amazon S3 bucket and returns the public URL
     * of the uploaded HTML report file.
     * A unique base key (prefix) is generated for each upload using the current timestamp in the
     * format {@code yyyyMMddHHmmssSSS}, ensuring that each report is stored in its own folder
     * within the S3 bucket. All files in the specified local folder are uploaded with the
     * {@link CannedAccessControlList#PublicRead} ACL so they can be accessed publicly.
     *
     * If the folder contains an HTML report file (e.g. {@code ExtentReport.html}), its corresponding
     * public S3 URL is returned. If multiple HTML files are present, the last one processed will
     * determine the return value.
     *
     *
     * @param localFolderPath the path to the local folder containing the report files (e.g. {@code "test-output"})
     * @throws AmazonServiceException if the request was rejected by Amazon S3
     * @throws SdkClientException     if the client could not contact S3 or could not parse the response
     */
    private static void uploadReport(String localFolderPath) {
        if (Constants.S3_UPLOAD_ENABLED) {
            try {
                String s3BaseKey = DateUtils.getCurrentTimestamp("yyyyMMddHHmmssSSS");
                BasicAWSCredentials awsCreds = new BasicAWSCredentials(Constants.AWS_ACCESS_KEY, Constants.AWS_SECRET);
                AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                        .withRegion(Regions.AP_SOUTH_1)
                        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                        .build();
                File folder = new File(localFolderPath);
                String reportUrl = null;
                String logUrl = null;
                for (File file : folder.listFiles()) {
                    String key = s3BaseKey + "/" + file.getName();
                    s3Client.putObject(new PutObjectRequest(Constants.AWS_S3_BUCKET, key, file));
                    if (file.getName().endsWith(".html")) {
                        reportUrl = "https://" + Constants.AWS_S3_BUCKET + ".s3." + s3Client.getRegionName()
                                + ".amazonaws.com/" + key;
                    } else if (file.getName().endsWith(".log")) {
                        logUrl = "https://" + Constants.AWS_S3_BUCKET + ".s3." + s3Client.getRegionName()
                                + ".amazonaws.com/" + key;
                    }
                }
                LogController.info("Report available at: " + reportUrl);
                LogController.info("Logs available at: " + logUrl);
            } catch (AmazonServiceException e) {
                LogController.error(e.getMessage());
            } catch (SdkClientException e) {
                LogController.error(e.getMessage());
            }
        } else {
            LogController.error("Report upload to S3 not enabled.");
        }
    }
}
