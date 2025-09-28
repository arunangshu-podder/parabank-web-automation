package com.parasoft.parabank.utility;

import static com.parasoft.parabank.controller.PropertiesController.Config;

public class Constants {
    public static final Browser BROWSER_TYPE = Browser.valueOf(
            System.getProperty("run.browser", Config.getProperty("run.browser")).toUpperCase());
    public static final String ENVIRONMENT = System.getProperty("app.env", Config.getProperty("app.env"));
    public static final String APP_URL = Config.getProperty(String.format("app.%s.url", ENVIRONMENT.toLowerCase()));

    public static final String TEST_DATA_FILE_PATH = Config.getProperty("data.filePath");

    public static final int IMPLICIT_WAIT = Integer.parseInt(Config.getProperty("wait.implicit"));
    public static final int WAIT = Integer.parseInt(Config.getProperty("wait.constant"));
    public static final int PAGELOAD_WAIT = Integer.parseInt(Config.getProperty("wait.pageload"));

    public static final String DATA = "data";

    public static final String RELEASE_VERSION = Config.getProperty("release.version");

    public static boolean S3_UPLOAD_ENABLED = Boolean.parseBoolean(
            System.getProperty("enable.analytics", Config.getProperty("enable.s3.upload")));
    public static String AWS_S3_BUCKET = Config.getProperty("aws.s3.bucket");
    public static String AWS_ACCESS_KEY = Config.getProperty("aws.s3.accessKey");
    public static String AWS_SECRET = Config.getProperty("aws.s3.secret");
    public static boolean ANALYTICS_ENABLED = Boolean.parseBoolean(
            System.getProperty("enable.analytics", Config.getProperty("enable.analytics")));

    public static boolean GRID_ENABLED = Boolean.parseBoolean(
            System.getProperty("enable.grid", Config.getProperty("enable.grid")));
    public static String GRID_URL = Config.getProperty("grid.url");
}
