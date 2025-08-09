package com.parasoft.parabank.base;

import com.parasoft.parabank.commands.*;
import com.parasoft.parabank.controller.DriverController;
import com.parasoft.parabank.utility.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {
    protected BuildCommand commands = new BuildCommand();
    private FluentWait<WebDriver> wait = new FluentWait<>(DriverController.getDriver())
                                            .withTimeout(Duration.ofSeconds(Constants.IMPLICIT_WAIT))
                                            .pollingEvery(Duration.ofSeconds(Constants.WAIT));

    protected WebElement getElement(By locator) {
        return DriverController.getDriver().findElement(locator);
    }

    protected List<WebElement> getElements(By locator) {
        return DriverController.getDriver().findElements(locator);
    }

    protected void enterText(By locator, String text, String errorMsg) {
        commands.addCommand(new ConditionalWaitCommand(ExpectedConditions.visibilityOfElementLocated(locator), errorMsg));
        commands.addCommand(new TypeCommand(locator, text, errorMsg));
    }

    protected void click(By locator, String errorMsg) {
        commands.addCommand(new ConditionalWaitCommand(ExpectedConditions.visibilityOfElementLocated(locator), errorMsg));
        commands.addCommand(new ConditionalWaitCommand(ExpectedConditions.elementToBeClickable(locator), errorMsg));
        commands.addCommand(new ClickCommand(locator, errorMsg));
    }

    protected String getText(By locator) {
        return getElement(locator).getText();
    }

    protected String getValueByAttribute(By locator, String attributeName) {
        return getElement(locator).getAttribute(attributeName);
    }

    protected void selectListOption(By locator, String listOption, String errorMsg) {
        commands.addCommand(new SelectCommand(locator, listOption, errorMsg));
    }

    protected void validateVisibilityOfElement(By locator, int timeoutInSecs, String errorMsg) {
        commands.addCommand(
                new ConditionalWaitCommand(ExpectedConditions.visibilityOfElementLocated(locator), timeoutInSecs, errorMsg));
    }

    protected void validateVisibilityOfElement(By locator, String errorMsg) {
        validateVisibilityOfElement(locator, Constants.WAIT, errorMsg);
    }

    protected boolean verifyVisibilityOfElement(By locator, int timeoutInSecs) {
        try {
            return wait.withTimeout(Duration.ofSeconds(timeoutInSecs)).until(driver -> {
                WebElement element = driver.findElement(locator);
                return element.isDisplayed();
            });
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean verifyVisibilityOfElement(By locator) {
        return verifyVisibilityOfElement(locator, Constants.WAIT);
    }

    protected void executeSteps() throws Exception {
        commands.executeAll();
    }
}
