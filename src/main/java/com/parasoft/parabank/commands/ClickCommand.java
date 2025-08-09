package com.parasoft.parabank.commands;

import org.openqa.selenium.By;

/**
 * Command to click a web element located by a given locator.
 * Provides error handling and descriptive error messages.
 */
public class ClickCommand implements Command {
    private final By locator;
    private final String errorMsg;

    /**
     * Constructs a ClickCommand.
     *
     * @param locator  the By locator of the element to click
     * @param errorMsg the error message to use if the click fails
     */
    public ClickCommand(By locator, String errorMsg) {
        this.locator = locator;
        this.errorMsg = errorMsg;
    }

    /**
     * Executes the click action on the specified element.
     *
     * @throws Exception if the element cannot be clicked
     */
    @Override
    public void execute() throws Exception {
        try {
            getElement(locator).click();
        } catch (Exception e) {
            throw new Exception(errorMsg + ": " + e.getMessage(), e);
        }
    }
}
