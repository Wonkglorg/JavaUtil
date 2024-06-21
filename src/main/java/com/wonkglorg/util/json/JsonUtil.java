package com.wonkglorg.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("unused")
public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads a jspn file and populates the class with data the class should be annotated like {@link ExampleJsonData}
     *
     * @param path  the path to the file
     * @param clazz the class to populate
     * @param <T>   the class type
     * @return the populated class
     */
    public static synchronized <T> T fromFile(String path, Class<T> clazz) {

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
     * @param path      the path to the file
     * @param object    the object to write
     * @param overwrite true to overwrite the file if it exists
     * @return true if the file was written successfully, false if the file already exists and overwrite is false
     */
    public static synchronized boolean toFile(String path, Object object, boolean overwrite) {
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


    /**
     * Writes a JSON string to a file
     *
     * @param path       the path to the file
     * @param jsonString the JSON string to write
     * @param overwrite  true to overwrite the file if it exists
     * @return true if the file was written successfully, false if the file already exists and overwrite is false
     */
    public static synchronized boolean toFile(String path, String jsonString, boolean overwrite) {
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

    /**
     * Checks if the file exists and creates it if it does not
     *
     * @param path      the path to the file
     * @param overwrite true to overwrite the file if it exists
     * @return the file
     * @throws IOException
     */
    private static synchronized File checkFile(String path, boolean overwrite) throws IOException {
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


    /**
     * Converts a Json object to a string
     *
     * @param object the object to convert
     * @return the JSON string
     */
    public static synchronized String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Registers a module to the object mapper
     *
     * @param module the module to register
     */
    public static synchronized void registerModule(SimpleModule module) {
        objectMapper.registerModule(module);
    }

    /**
     * Registers a module to the object mapper
     *
     * @param module the module to register
     */
    public static synchronized void registerModule(com.fasterxml.jackson.databind.Module module) {
        objectMapper.registerModule(module);
    }

    /**
     * Registers a serializer and deserializer for a class
     *
     * @param serializer
     * @param deserializer
     * @param clazz
     * @param <T>
     */
    public static synchronized <T> void registerModule(JsonSerializer<T> serializer, JsonDeserializer<T> deserializer, Class<T> clazz) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(clazz, serializer);
        module.addDeserializer(clazz, deserializer);
        objectMapper.registerModule(module);
    }

    public static synchronized ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
