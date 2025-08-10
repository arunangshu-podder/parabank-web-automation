package com.parasoft.parabank.utility;

import com.parasoft.parabank.controller.LogController;
import com.parasoft.parabank.controller.PropertiesController;
import com.parasoft.parabank.controller.TestController;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Utility class for database operations related.
 */
public class DBUtils {

    private final String sql = "INSERT INTO test_executions (" +
            "test_case_id, test_case_name, release_version, environment, platform," +
            "execution_status, execution_start_time, execution_end_time, duration_seconds, test_tags" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private String URL;
    private String USER;
    private String PASSWORD;

    /**
     * Inserts the current thread's test execution record into the analytics database.
     * Uses TestController's ThreadLocal instance for parallel-safe data access.
     * @throws IOException if database properties cannot be loaded
     */
    public synchronized void insertExecutionRecord(TestController testController) throws IOException {
        Properties dbDetails = PropertiesController.getAnalyticsDBDetails();
        URL = dbDetails.getProperty("db.url");
        USER = dbDetails.getProperty("db.username");
        PASSWORD = dbDetails.getProperty("db.password");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, testController.getTestCaseId());
            stmt.setString(2, testController.getTestCaseName());
            stmt.setString(3, testController.getReleaseVersion());
            stmt.setString(4, testController.getEnvironment());
            stmt.setString(5, testController.getPlatform());
            stmt.setString(6, testController.getExecutionStatus());
            stmt.setTimestamp(7, testController.getExecutionStartTime());
            stmt.setTimestamp(8, testController.getExecutionEndTime());
            stmt.setInt(9, testController.getDurationSeconds());
            stmt.setString(10, testController.getTags());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                LogController.info("Test execution details recorded successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LogController.error("Error while inserting test execution record: " + e.getMessage());
        }
    }
}
