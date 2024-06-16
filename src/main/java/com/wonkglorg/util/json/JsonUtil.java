package com.wonkglorg.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;

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


}
