package com.parasoft.parabank.controller;

import com.parasoft.parabank.utility.Constants;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Manages WebDriver instances per thread.
 */
public class DriverController {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverController() {}

    /**
     * Returns the current thread's WebDriver.
     * If not available, creates a new one using the default browser.
     *
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        if (DRIVER.get() == null) {
            try {
                DRIVER.set(getDriver(Constants.BROWSER_TYPE));
            } catch (Exception e) {
                LogController.error("Failed to initialize WebDriver: " + e.getMessage());
                throw e;
            }
        }
        return DRIVER.get();
    }

    /**
     * Returns a new WebDriver based on the browser type provided.
     *
     * @param browserType "chrome" or "firefox"
     * @return new WebDriver instance
     * @throws IllegalArgumentException if browser type is not supported
     */
    public static WebDriver getDriver(String browserType) {
        try {
            if (browserType.equalsIgnoreCase("chrome")) {
                return getChromeDriver();
            } else if (browserType.equalsIgnoreCase("firefox")) {
                return getFirefoxDriver();
            } else {
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
            }
        } catch (Exception e) {
            LogController.error("Error creating WebDriver for browser '" + browserType + "': " + e.getMessage());
            throw e;
        }
    }

    /**
     * Closes and removes the current thread's WebDriver, if it exists.
     */
    public static void killDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.close();
            } catch (Exception e) {
                LogController.error("Error closing WebDriver: " + e.getMessage());
            } finally {
                DRIVER.remove();
            }
        }
    }

    /**
     * Chrome browser options
     */
    private static WebDriver getChromeDriver() {
        try {
            ChromeOptions options = new ChromeOptions();
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            options.addArguments("--incognito");
            return new ChromeDriver(options);
        } catch (Exception e) {
            LogController.error("Failed to initialize ChromeDriver: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Firefox browser options
     */
    private static WebDriver getFirefoxDriver() {
        try {
            FirefoxOptions options = new FirefoxOptions();
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            options.addArguments("--private");
            return new FirefoxDriver(options);
        } catch (Exception e) {
            LogController.error("Failed to initialize FirefoxDriver: " + e.getMessage());
            throw e;
        }
    }
}
