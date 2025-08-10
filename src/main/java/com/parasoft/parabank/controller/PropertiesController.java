package com.parasoft.parabank.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Loads configuration properties.
 */
public class PropertiesController {

    public static Properties Config = new Properties();

    /**
     * Loads properties from the "./Config.properties" file into the Config object.
     * Uses try-with-resources for safe resource management.
     *
     * @throws IOException if the properties file cannot be read
     */
    public static void getConfigProperties() throws IOException {
        try (FileInputStream fis = new FileInputStream("./Config.properties")) {
            Config.load(fis);
        } catch (IOException e) {
            // Log the error and rethrow for upstream handling
            LogController.error("Failed to load configuration properties: " + e.getMessage());
            throw e;
        }
    }

    public static Properties getAnalyticsDBDetails() throws IOException {
        try (FileInputStream fis = new FileInputStream("./AnalyticsDB.properties")) {
            Properties analyticsConfig = new Properties();
            analyticsConfig.load(fis);
            return analyticsConfig;
        } catch (IOException e) {
            // Log the error and rethrow for upstream handling
            LogController.error("Failed to load configuration properties: " + e.getMessage());
            throw e;
        }
    }
}
