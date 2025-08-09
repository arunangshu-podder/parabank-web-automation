package com.parasoft.parabank.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

/**
 * Command to select an option by visible text from a dropdown element.
 * Provides error handling and descriptive error messages.
 */
public class SelectCommand implements Command {
    private final String visibleText;
    private final By locator;
    private final String errorMsg;

    /**
     * Constructs a SelectCommand.
     *
     * @param locator     the By locator of the dropdown element
     * @param visibleText the visible text to select
     * @param errorMsg    the error message to use if selection fails
     */
    public SelectCommand(By locator, String visibleText, String errorMsg) {
        this.locator = locator;
        this.visibleText = visibleText;
        this.errorMsg = errorMsg;
    }

    /**
     * Executes the select action on the specified dropdown element.
     *
     * @throws Exception if the selection fails
     */
    @Override
    public void execute() throws Exception {
        try {
            Select dropdwonList = new Select(getElement(locator));
            dropdwonList.selectByVisibleText(visibleText);
        } catch (Exception e) {
            throw new Exception(errorMsg + ": " + e.getMessage(), e);
        }
    }
}
