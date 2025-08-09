package com.parasoft.parabank.pages;

import com.parasoft.parabank.base.BasePage;
import org.openqa.selenium.By;

public class GenericActions extends BasePage {
    protected String genericLink = "//a[text()='%s']";
    protected String genericBtn = "//input[@class='button' and @value='%s']";
    protected String genericTextElement = "//*[text()='%s']";

    public void clickLink(String linkText, String errorMsg) throws Exception {
        By locator = By.xpath(String.format(genericLink, linkText));
        click(locator, errorMsg);

        executeSteps();
    }

    public void clickButton(String buttonText, String errorMsg) throws Exception {
        By locator = By.xpath(String.format(genericBtn, buttonText));
        click(locator, errorMsg);

        executeSteps();
    }

    public boolean verifyElementWithTextDisplayed(String text) {
        By locator = By.xpath(String.format(genericTextElement, text));
        return verifyVisibilityOfElement(locator);
    }
}
