package com.parasoft.parabank.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getCurrentTimestamp(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }
}
