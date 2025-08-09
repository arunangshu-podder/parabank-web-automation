package com.parasoft.parabank.commands;

import com.parasoft.parabank.controller.DriverController;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Represents a command that can be executed as part of a test step.
 * Implementations should provide error handling and throw exceptions on failure.
 */
public interface Command {
    /**
     * Executes the command.
     *
     * @throws Exception if the command fails to execute
     */
    public abstract void execute() throws Exception;

    /**
     * Utility method to get a single WebElement by locator.
     *
     * @param locator the By locator
     * @return the found WebElement
     */
    default WebElement getElement(By locator) {
        return DriverController.getDriver().findElement(locator);
    }

    /**
     * Utility method to get a list of WebElements by locator.
     *
     * @param locator the By locator
     * @return the list of found WebElements
     */
    default List<WebElement> getElements(By locator) {
        return DriverController.getDriver().findElements(locator);
    }
}
