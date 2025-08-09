package com.parasoft.parabank.pages;

import org.openqa.selenium.By;

public class LogInPage extends GenericActions {
    By username = By.name("username");
    By password = By.name("password");
    By loginBtn = By.cssSelector("div.login > input.button");
    By customerLoginBanner = By.xpath("//h2[text()='Customer Login']");

    public void verifyLoginPanelDisplayed() throws Exception {
        validateVisibilityOfElement(customerLoginBanner, "Navigation to Login Page failed.");
        executeSteps();
    }

    public void login(String userId, String pwd) throws Exception {
        enterText(username, userId, "Unable to enter user id.");
        enterText(password, pwd, "Unalbe to enter user password.");
        click(loginBtn, "Unable to click login button.");
        executeSteps();
    }



}
