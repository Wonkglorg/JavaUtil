package com.wonkglorg.util.files;

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
}
