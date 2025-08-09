package com.parasoft.parabank.pages;

import com.parasoft.parabank.data.UserRegistrationDataObject;
import org.openqa.selenium.By;

public class UserRegistrationPage extends GenericActions {
    By firstName = By.id("customer.firstName");
    By lastName = By.id("customer.lastName");
    By address = By.id("customer.address.street");
    By city = By.id("customer.address.city");
    By state = By.id("customer.address.state");
    By zipcode = By.id("customer.address.zipCode");
    By phone = By.id("customer.phoneNumber");
    By ssn = By.id("customer.ssn");
    By username = By.id("customer.username");
    By password = By.id("customer.password");
    By confirmPassword = By.id("repeatedPassword");

    public void fillUserDetails(UserRegistrationDataObject dataObject) throws Exception {
        enterText(firstName, dataObject.FIRST_NAME, "Unable to enter user first name.");
        enterText(lastName, dataObject.LAST_NAME, "Unable to enter user last name.");
        enterText(address, dataObject.ADDRESS, "Unable to enter user address.");
        enterText(city, dataObject.CITY, "Unable to enter user city.");
        enterText(state, dataObject.STATE, "Unable to enter user state.");
        enterText(zipcode, dataObject.ZIP_CODE, "Unable to enter user zip code.");
        enterText(phone, dataObject.PHONE, "Unable to enter user phone number.");
        enterText(ssn, dataObject.SSN, "Unable to enter user ssn number.");
        enterText(username, dataObject.USERNAME, "Unable to enter username.");
        enterText(password, dataObject.PASSWORD, "Unable to enter password.");
        enterText(confirmPassword, dataObject.PASSWORD, "Unable to enter password for confirmation.");

        executeSteps();
    }
}
