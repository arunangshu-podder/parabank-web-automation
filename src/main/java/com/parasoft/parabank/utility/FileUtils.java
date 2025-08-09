package com.parasoft.parabank.utility;

import com.parasoft.parabank.controller.LogController;

import java.io.File;

/**
 * Utility class for file operations.
 */
public class FileUtils {

    /**
     * Deletes all files in the specified folder.
     * Logs information if the folder path is invalid or already empty.
     * Only files are deleted; subdirectories are ignored.
     * Logs errors for files that fail to delete.
     *
     * @param folderPath the path to the folder to clear
     */
    public static void clearFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            LogController.info("Invalid folder path: " + folderPath);
            return;
        }
        File[] files = folder.listFiles(File::isFile);
        if (files == null || files.length == 0) {
            LogController.info("Folder is already empty.");
            return;
        }
        boolean allDeleted = true;
        for (File file : files) {
            if (!file.delete()) {
                LogController.error("Failed to delete file: " + file.getAbsolutePath());
                allDeleted = false;
            }
        }
        if (allDeleted) {
            LogController.info("All files deleted successfully in folder: " + folderPath);
        } else {
            LogController.error("Some files could not be deleted in folder: " + folderPath);
        }
    }
}
