package com.parasoft.parabank.controller;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Provides thread-safe logging functionality using Log4j2.
 * Handles info, error, and debug level logging.
 */
public class LogController {
    private static final ThreadLocal<Logger> loggerThreadLocal = ThreadLocal.withInitial(() -> {
        try {
            return LogManager.getLogger(Thread.currentThread().getName());
        } catch (Exception e) {
            System.err.println("Logger initialization failed: " + e.getMessage());
            return null;
        }
    });

    /**
     * Gets the logger instance for the current thread.
     *
     * @return Logger object for the current thread, or null if initialization failed
     */
    public static Logger getLogger() {
        return loggerThreadLocal.get();
    }

    /**
     * Logs an info-level message.
     * Use this to log general information like "Test Started", "Page Loaded", etc.
     *
     * @param message The message to be logged
     */
    public static void info(String message) {
        getLogger().info(message);
    }

    /**
     * Logs an error-level message.
     * Use this when something goes wrong or a test fails.
     *
     * @param message The error message to be logged
     */
    public static void error(String message) {
        getLogger().error(message);
    }

    /**
     * Logs a debug-level message.
     * Use this for detailed information helpful during debugging.
     *
     * @param message The debug message to be logged
     */
    public static void debug(String message) {
        getLogger().debug(message);
    }
}
