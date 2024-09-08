package com.wonkglorg.util.files;

import com.wonkglorg.util.Platform;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    /**
     * Create a symbolic link to a target file.
     *
     * @param file   the link file
     * @param target the target to point to
     * @return true if the link was created, false otherwise
     */
    public static boolean createSymbolicLink(File file, Path target) {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } else {
                return false;
            }

            Files.createSymbolicLink(file.toPath(), target);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sanitize a file name by replacing disallowed characters with underscores.
     *
     * @param fileName    the file name to sanitize
     * @param platform    the platform to use
     * @param replacement the replacement character
     * @return the sanitized file name
     */
    public static String sanitizeFileName(String fileName, Platform platform, String replacement) {
        if (fileName == null) {
            return null;
        }

        if (fileName.isEmpty()) {
            return fileName;
        }

        String[] disallowedFileChars = platform.getDisallowedFileChars();

        for (String c : disallowedFileChars) {
            fileName = fileName.replaceAll(c, replacement);
        }

        return fileName;
    }

    /**
     * Sanitize a file name by replacing disallowed characters with underscores.
     *
     * @param fileName the file name to sanitize
     * @param platform the platform to use
     * @return the sanitized file name
     */
    public static String sanitizeFileName(String fileName, Platform platform) {
        return sanitizeFileName(fileName, platform, "_");
    }

    /**
     * Sanitize a file name by replacing disallowed characters with underscores.
     *
     * @param fileName the file name to sanitize
     * @return the sanitized file name
     */
    public static String sanitizeFileName(String fileName) {
        return sanitizeFileName(fileName, Platform.getPlatform());
    }
}
