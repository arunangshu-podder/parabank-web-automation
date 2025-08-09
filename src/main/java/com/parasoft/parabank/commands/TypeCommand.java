package com.parasoft.parabank.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

/**
 * Command to type text (and optionally a key) into a web element.
 * Provides error handling and descriptive error messages.
 */
public class TypeCommand implements Command {
    private final String text;
    private final Keys key;
    private final By locator;
    private final String errorMsg;

    /**
     * Constructs a TypeCommand for typing text only.
     *
     * @param locator  the By locator of the element to type into
     * @param text     the text to type
     * @param errorMsg the error message to use if typing fails
     */
    public TypeCommand(By locator, String text, String errorMsg) {
        this.locator = locator;
        this.text = text;
        this.key = null;
        this.errorMsg = errorMsg;
    }

    /**
     * Constructs a TypeCommand for typing text and a key.
     *
     * @param locator  the By locator of the element to type into
     * @param text     the text to type
     * @param key      the key to send after the text
     * @param errorMsg the error message to use if typing fails
     */
    public TypeCommand(By locator, String text, Keys key, String errorMsg) {
        this.locator = locator;
        this.text = text;
        this.key = key;
        this.errorMsg = errorMsg;
    }

    /**
     * Executes the type action on the specified element.
     *
     * @throws Exception if typing fails
     */
    @Override
    public void execute() throws Exception {
        try {
            if (key == null) {
                getElement(locator).sendKeys(text);
            } else {
                getElement(locator).sendKeys(text, key);
            }
        } catch (Exception e) {
            throw new Exception(errorMsg + ": " + e.getMessage(), e);
        }
    }
}
