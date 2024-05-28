package com.wonkglorg.util.json;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonUtil {


    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads a jspn file and populates the class with data
     *
     * @param path
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromFile(String path, Class<T> clazz) {

        File file = new File(path);
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, clazz);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    /**
     * Writes the object to a json file
     *
     * @param path
     * @param object
     * @param overwrite
     * @return true if the file was written successfully, false if the file already exists and overwrite is false
     */
    public static boolean toFile(String path, Object object, boolean overwrite) {
        try {
            File file = checkFile(path, overwrite);
            if (file == null) {
                return false;
            }
            objectMapper.writeValue(file, object);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Error writing object to file: " + e.getMessage(), e);
        }
    }

    public static boolean toFile(String path, String jsonString, boolean overwrite) {
        try {
            File file = checkFile(path, overwrite);
            if (file == null) {
                return false;
            }

            objectMapper.writeValue(file, jsonString);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON string to file: " + e.getMessage(), e);
        }
    }

    private static File checkFile(String path, boolean overwrite) throws IOException {
        File file = new File(path);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            throw new IOException("Unable to create directories for file: " + file.getParentFile());
        }
        if (!overwrite && file.exists()) {
            return null;
        }
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Unable to create file: " + file);
        }
        return file;
    }

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
