package com.parasoft.parabank.controller;

import java.sql.Timestamp;

/**
 * TestController manages test execution details for each thread using ThreadLocal.
 * This enables safe parallel execution of tests by isolating test data per thread.
 * Use {@link #getInstance()} to access the thread's TestController instance,
 * and {@link #setInstance()} to reset the instance for a new test execution.
 */
public class TestController {
    // Test execution details
    private String testCaseId;
    private String testCaseName;
    private String releaseVersion;
    private String environment;
    private String platform;
    private String executionStatus;
    private Timestamp executionStartTime;
    private Timestamp executionEndTime;
    private int durationSeconds;
    private String tags;

    /**
     * ThreadLocal instance to isolate TestController per thread.
     */
    private final static ThreadLocal<TestController> threadLocalInstance = ThreadLocal.withInitial(TestController::new);

    /**
     * Returns the TestController instance for the current thread.
     * @return TestController for the current thread
     */
    public static TestController getInstance() {
        return threadLocalInstance.get();
    }

    /**
     * Creates and sets a new TestController instance for the current thread.
     * Use this at the start of each test execution.
     * @return the new TestController instance
     */
    public static TestController setInstance() {
        threadLocalInstance.set(new TestController());
        return getInstance();
    }

    /**
     * Removes the TestController instance from the current thread.
     * Call this after test execution to avoid memory leaks.
     */
    public static void removeInstance() {
        threadLocalInstance.remove();
    }

    /** @return the test case ID */
    public String getTestCaseId() {
        return testCaseId;
    }

    /** @param testCaseId the test case ID to set */
    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    /** @return the test case name */
    public String getTestCaseName() {
        return testCaseName;
    }

    /** @param testCaseName the test case name to set */
    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    /** @return the release version */
    public String getReleaseVersion() {
        return releaseVersion;
    }

    /** @param releaseVersion the release version to set */
    public void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    /** @return the environment */
    public String getEnvironment() {
        return environment;
    }

    /** @param environment the environment to set */
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    /** @return the platform */
    public String getPlatform() {
        return platform;
    }

    /** @param platform the platform to set */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /** @return the execution status */
    public String getExecutionStatus() {
        return executionStatus;
    }

    /** @param executionStatus the execution status to set */
    public void setExecutionStatus(String executionStatus) {
        this.executionStatus = executionStatus;
    }

    /** @return the execution start time */
    public Timestamp getExecutionStartTime() {
        return executionStartTime;
    }

    /** @param executionStartTime the execution start time to set */
    public void setExecutionStartTime(Timestamp executionStartTime) {
        this.executionStartTime = executionStartTime;
    }

    /** @return the execution end time */
    public Timestamp getExecutionEndTime() {
        return executionEndTime;
    }

    /** @param executionEndTime the execution end time to set */
    public void setExecutionEndTime(Timestamp executionEndTime) {
        this.executionEndTime = executionEndTime;
    }

    /** @return the test duration in seconds */
    public int getDurationSeconds() {
        return durationSeconds;
    }

    /** @param durationSeconds the test duration in seconds to set */
    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    /** @return the tags associated with the test */
    public String getTags() {
        return tags;
    }

    /** @param tags the tags to set */
    public void setTags(String tags) {
        this.tags = tags;
    }
}