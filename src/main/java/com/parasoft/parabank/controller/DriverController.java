package com.parasoft.parabank.controller;

import com.parasoft.parabank.utility.Browser;
import com.parasoft.parabank.utility.Constants;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;

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
    public static WebDriver getDriver(Browser browserType) {
        try {
            switch (browserType.toString().toLowerCase()) {
                case "chrome":
                    return getChromeDriver();
                case "firefox":
                    return getFirefoxDriver();
                case "edge":
                    return getEdgeDriver();
                case "safari":
                    return getSafariDriver();
                default:
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
                driver.quit();
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
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage"); // avoids /dev/shm issues in containers
            options.addArguments("--disable-gpu");
            return Constants.GRID_ENABLED ?
                    getRemoteWebDriver(options) : new ChromeDriver(options);
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
            return Constants.GRID_ENABLED ?
                    getRemoteWebDriver(options) : new FirefoxDriver(options);
        } catch (Exception e) {
            LogController.error("Failed to initialize FirefoxDriver: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Edge browser options
     */
    private static WebDriver getEdgeDriver() {
        try {
            EdgeOptions options = new EdgeOptions();
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            options.addArguments("--inprivate");
            return Constants.GRID_ENABLED ?
                    getRemoteWebDriver(options) : new EdgeDriver(options);
        } catch (Exception e) {
            LogController.error("Failed to initialize EdgeDriver: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Safari browser options
     */
    private static WebDriver getSafariDriver() {
        try {
            SafariOptions options = new SafariOptions();
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            return Constants.GRID_ENABLED ?
                    getRemoteWebDriver(options) : new SafariDriver(options);
        } catch (Exception e) {
            LogController.error("Failed to initialize SafariDriver: " + e.getMessage());
            throw e;
        }
    }

    private static WebDriver getRemoteWebDriver(MutableCapabilities options) {
        try {
            return new RemoteWebDriver(new URL(Constants.GRID_URL), options);
        } catch (MalformedURLException e) {
            LogController.error("Invalid URL for RemoteWebDriver: " + e.getMessage());
        }
        return null;
    }
}
