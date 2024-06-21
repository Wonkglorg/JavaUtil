package com.wonkglorg.util.programms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents all parameters for a command line program (excluding the start program path itself)
 */
public class ProgrammString {
    private final List<Map.Entry<String, String>> parameters = new ArrayList<>();


    /**
     * Adds a parameter without a value
     *
     * @param key the parameter key
     * @return the builder
     */
    public ProgrammString put(String key) {
        parameters.add(Map.entry(key, ""));

        return this;
    }

    /**
     * Adds a parameter with a value
     *
     * @param key
     * @param value
     * @return
     */
    public ProgrammString put(String key, String value) {
        parameters.add(Map.entry(key, value));
        return this;
    }


    /**
     * Returns a user-friendly string representation
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters) {
            builder.append(" ");
            builder.append(entry.getKey());
            if (!entry.getValue().isEmpty()) {
                builder.append(" ");
                builder.append(entry.getValue());
            }

        }
        return builder.toString();
    }

    public List<Map.Entry<String, String>> getParameters() {
        return parameters;
    }

    public String removeLast() {
        if (parameters.size() > 0) {
            parameters.remove(parameters.size() - 1);
        }
        return this.toString();
    }

    public String removeFirst() {
        if (parameters.size() > 0) {
            parameters.remove(0);
        }
        return this.toString();
    }

    public String remove(int index) {
        if (parameters.size() > index) {
            parameters.remove(index);
        }
        return this.toString();
    }

    public String changeValue(int index, String value) {
        if (parameters.size() > index) {
            parameters.set(index, Map.entry(parameters.get(index).getKey(), value));
        }
        return this.toString();
    }

    /**
     * Changes the key of a parameter with the given index
     *
     * @param index the index of the parameter
     * @param key   the new key
     */
    public void changeKey(int index, String key) {
        if (parameters.size() > index) {
            parameters.set(index, Map.entry(key, parameters.get(index).getValue()));
        }
    }

    /**
     * Changes the value of a parameter with the given key
     *
     * @param key   the key of the parameter
     * @param value the new value
     */
    public void change(String key, String value) {
        for (int i = 0; i < parameters.size(); i++) {
            if (parameters.get(i).getKey().equals(key)) {
                parameters.set(i, Map.entry(key, value));
            }
        }
    }
}
