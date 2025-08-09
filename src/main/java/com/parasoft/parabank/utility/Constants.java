package com.parasoft.parabank.utility;

import static com.parasoft.parabank.controller.PropertiesController.Config;

public class Constants {
    public static final String BROWSER_TYPE = Config.getProperty("run.browser");
    public static final String RUN_MODE = Config.getProperty("run.mode");
    public static final String ENVIRONMENT = Config.getProperty("app.env");
    public static final String APP_URL = Config.getProperty(String.format("app.%s.url", ENVIRONMENT.toLowerCase()));

    public static final String TEST_DATA_FILE_PATH = Config.getProperty("data.filePath");

    public static final int IMPLICIT_WAIT = Integer.parseInt(Config.getProperty("wait.implicit"));
    public static final int WAIT = Integer.parseInt(Config.getProperty("wait.constant"));
    public static final int PAGELOAD_WAIT = Integer.parseInt(Config.getProperty("wait.pageload"));

    public static final String DATA = "data";
}
