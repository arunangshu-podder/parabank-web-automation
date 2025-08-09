package com.parasoft.parabank.commands;

import com.parasoft.parabank.controller.DriverController;
import com.parasoft.parabank.utility.Constants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

/**
 * Command to perform a conditional wait using FluentWait and ExpectedCondition.
 * Provides error handling and descriptive error messages.
 */
public class ConditionalWaitCommand implements Command {
    private FluentWait<WebDriver> wait;
    private final ExpectedCondition<WebElement> condition;
    private final String errorMsg;

    /**
     * Constructs a ConditionalWaitCommand with default timeout and polling.
     *
     * @param condition the ExpectedCondition to wait for
     * @param errorMsg  the error message to use if waiting fails
     */
    public ConditionalWaitCommand(ExpectedCondition<WebElement> condition, String errorMsg) {
        this.wait = new FluentWait<>(DriverController.getDriver())
                .withTimeout(Duration.ofSeconds(Constants.IMPLICIT_WAIT))
                .pollingEvery(Duration.ofSeconds(Constants.WAIT));
        this.condition = condition;
        this.errorMsg = errorMsg;
    }

    /**
     * Constructs a ConditionalWaitCommand with custom timeout.
     *
     * @param condition the ExpectedCondition to wait for
     * @param timeout   the timeout in seconds
     * @param errorMsg  the error message to use if waiting fails
     */
    public ConditionalWaitCommand(ExpectedCondition<WebElement> condition, int timeout, String errorMsg) {
        this.wait = new FluentWait<>(DriverController.getDriver())
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(Constants.WAIT));
        this.condition = condition;
        this.errorMsg = errorMsg;
    }

    /**
     * Constructs a ConditionalWaitCommand with custom timeout and polling interval.
     *
     * @param condition   the ExpectedCondition to wait for
     * @param timeout     the timeout in seconds
     * @param pollingTime the polling interval in seconds
     * @param errorMsg    the error message to use if waiting fails
     */
    public ConditionalWaitCommand(ExpectedCondition<WebElement> condition, int timeout, int pollingTime, String errorMsg) {
        this.wait = new FluentWait<>(DriverController.getDriver())
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(pollingTime));
        this.condition = condition;
        this.errorMsg = errorMsg;
    }

    /**
     * Executes the wait until the condition is met.
     *
     * @throws Exception if the condition is not met within the timeout
     */
    @Override
    public void execute() throws Exception {
        try {
            wait.until(condition);
        } catch (Exception e) {
            throw new Exception(errorMsg + ": " + e.getMessage(), e);
        }
    }
}
