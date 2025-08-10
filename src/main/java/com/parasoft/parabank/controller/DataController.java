package com.parasoft.parabank.controller;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.parasoft.parabank.data.RequestLoanDataObject;
import com.parasoft.parabank.data.TransferFundsDataObject;
import com.parasoft.parabank.data.UserRegistrationDataObject;
import com.parasoft.parabank.utility.Constants;
import com.parasoft.parabank.utility.ScenarioContext;

import static com.parasoft.parabank.utility.Constants.DATA;

public class DataController {

    /**
     * Reads test data for the specified test case and module, and stores it in the provided ScenarioContext.
     * Logs and throws an exception if no matching record is found.
     *
     * @param testCaseId      the ID of the test case to read data for
     * @param moduleName      the name of the module (e.g., "Transfer_Funds", "User_Registration")
     * @param scenarioContext the ScenarioContext to store the data object in
     * @throws FilloException if there is an error reading from the data source or no record is found
     */
    public void readData(String testCaseId, String moduleName, ScenarioContext scenarioContext) throws FilloException {
        Connection connection = null;
        Recordset recordset = null;
        String strQuery = String.format("Select * from %s where TEST_CASE_ID='%s'", moduleName, testCaseId);
        try {
            connection = new Fillo().getConnection(System.getProperty("user.dir") + "/" + Constants.TEST_DATA_FILE_PATH);
            recordset = connection.executeQuery(strQuery);
            if (!recordset.next()) {
                LogController.error("No data found for TEST_CASE_ID: " + testCaseId + " in module: " + moduleName);
                throw new FilloException("No data found for TEST_CASE_ID: " + testCaseId + " in module: " + moduleName);
            }
            if (moduleName.equalsIgnoreCase("Transfer_Funds")) {
                TransferFundsDataObject dataObject = getTransferFundsData(recordset);
                scenarioContext.set(DATA, (TransferFundsDataObject)dataObject);
            } else if (moduleName.equalsIgnoreCase("User_Registration")) {
                UserRegistrationDataObject dataObject = getUserRegistrationData(recordset);
                scenarioContext.set(DATA, (UserRegistrationDataObject)dataObject);
            } else if (moduleName.equalsIgnoreCase("Request_Loan")) {
                RequestLoanDataObject dataObject = getRequestLoanData(recordset);
                scenarioContext.set(DATA, (RequestLoanDataObject)dataObject);
            } else {
                LogController.error("Unknown module name: " + moduleName);
                throw new FilloException("Unknown module name: " + moduleName);
            }
        } catch (FilloException e) {
            LogController.error("Error reading data: " + e.getMessage());
            throw e;
        } finally {
            if (recordset != null) {
                try {
                    recordset.close();
                } catch (Exception e) {
                    LogController.error("Error closing Recordset: " + e.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    LogController.error("Error closing Connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Reads transfer funds data from the given Fillo Recordset and
     * maps it to a TransferFundsDataObject.
     *
     * @param record Fillo record containing transfer fund test data
     * @return populated TransferFundsDataObject
     * @throws FilloException if reading from the record fails
     */
    private TransferFundsDataObject getTransferFundsData(Recordset record) throws FilloException {
        TransferFundsDataObject object = new TransferFundsDataObject();
        object.USER = record.getField("USER");
        object.PASSWORD = record.getField("PASSWORD");
        object.AMOUNT = record.getField("AMOUNT");
        object.FROM_ACCOUNT = record.getField("FROM_ACCOUNT");
        object.TO_ACCOUNT = record.getField("TO_ACCOUNT");

        return object;
    }

    /**
     * Reads request loan data from the given Fillo Recordset and
     * maps it to a RequestLoanDataObject.
     *
     * @param record Fillo record containing transfer fund test data
     * @return populated RequestLoanDataObject
     * @throws FilloException if reading from the record fails
     */
    private RequestLoanDataObject getRequestLoanData(Recordset record) throws FilloException {
        RequestLoanDataObject object = new RequestLoanDataObject();
        object.USER = record.getField("USER");
        object.PASSWORD = record.getField("PASSWORD");
        object.LOAN_AMOUNT = record.getField("LOAN_AMOUNT");
        object.DOWN_PAYMENT = record.getField("DOWN_PAYMENT");

        return object;
    }

    /**
     * Reads user registration data from the given Fillo Recordset and
     * maps it to a UserRegistrationDataObject.
     *
     * @param record Fillo record containing user registration test data
     * @return populated UserRegistrationDataObject
     * @throws FilloException if reading from the record fails
     */
    private UserRegistrationDataObject getUserRegistrationData(Recordset record) throws FilloException {
        UserRegistrationDataObject object = new UserRegistrationDataObject();
        object.FIRST_NAME = record.getField("FIRST_NAME");
        object.LAST_NAME = record.getField("LAST_NAME");
        object.ADDRESS = record.getField("ADDRESS");
        object.CITY = record.getField("CITY");
        object.STATE = record.getField("STATE");
        object.ZIP_CODE = record.getField("ZIP_CODE");
        object.PHONE = record.getField("PHONE");
        object.SSN = record.getField("SSN");
        object.USERNAME = record.getField("USERNAME");
        object.PASSWORD = record.getField("PASSWORD");

        return object;
    }

}
