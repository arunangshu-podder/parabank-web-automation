package com.parasoft.parabank.pages;

import com.parasoft.parabank.data.RequestLoanDataObject;
import com.parasoft.parabank.data.TransferFundsDataObject;
import org.openqa.selenium.By;

public class HomePage extends GenericActions {
    By welcomeMessage = By.cssSelector("div#leftPanel b");
    By transferAmount = By.id("amount");
    By fromAccountId = By.id("fromAccountId");
    By toAccountId = By.id("toAccountId");
    By transactionTable = By.cssSelector("table#transactionTable");
    By debitAmount = By.xpath("//a[text()='Funds Transfer Sent']//parent::td//following-sibling::td[1]");
    By creditAmount = By.xpath("//a[text()='Funds Transfer Received']//parent::td//following-sibling::td[2]");
    By loanAmount = By.id("amount");
    By downPayment = By.id("downPayment");


    public void verifyWelcomeMessageDisplayed() throws Exception {
        if (!verifyVisibilityOfElement(welcomeMessage)) {
            throw new Exception("Welcome message not displayed.");
        }
        String actualMessage = getElement(welcomeMessage).getText();
        String expectedMessage = "Welcome";
        if (!expectedMessage.equals(actualMessage)) {
            throw new Exception(
                    String.format("Expected %s. Actual %s.", expectedMessage, actualMessage));
        }
    }

    public void enterTransferDetails(TransferFundsDataObject object) throws Exception {
        validateVisibilityOfElement(transferAmount, "Transfer Amount field not displayed.");
        enterText(transferAmount, object.AMOUNT, "Failed to enter amount in Transfer Amount field.");

        validateVisibilityOfElement(fromAccountId, "From Account field not displayed.");
        selectListOption(fromAccountId, object.FROM_ACCOUNT,
                String.format("Unable to select option %s in From Account field.", object.FROM_ACCOUNT));

        validateVisibilityOfElement(toAccountId, "To Account field not displayed.");
        selectListOption(toAccountId, object.TO_ACCOUNT,
                String.format("Unable to select option %s in From Account field.", object.TO_ACCOUNT));

        executeSteps();
    }

    public void validateTransferDetails(TransferFundsDataObject object) throws Exception {
        validateVisibilityOfElement(transactionTable, "Transaction table not displayed.");

        String actualDebitAmount = getText(debitAmount);
        String actualCreditAmount = getText(creditAmount);

        if (actualCreditAmount.equals(object.AMOUNT) && actualDebitAmount.equals(object.AMOUNT)) {
            System.out.println("Transfer details validated successfully.");
        } else {
            throw new Exception(String.format("Expected debit amount %s and credit amount %s. " +
                    "Actual debit amount %s and credit amount %s.",
                    object.AMOUNT, object.AMOUNT, actualDebitAmount, actualCreditAmount));
        }
    }

    public void enterLoanDetails(RequestLoanDataObject object) throws Exception {
        validateVisibilityOfElement(loanAmount, "Loan Amount field not displayed.");
        enterText(loanAmount, object.LOAN_AMOUNT, "Failed to enter amount in Loan Amount field.");

        validateVisibilityOfElement(downPayment, "Down Payment field not displayed.");
        enterText(downPayment, object.DOWN_PAYMENT, "Failed to enter amount in Down Payment field.");

        executeSteps();
    }
}
