package com.wonkglorg.util.csv;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    /**
     * Parses a CSV file and returns a list of string arrays
     *
     * @param filePath  The path to the CSV file
     * @param seperator The seperator used in the CSV file
     * @return A list of string arrays
     * @throws IOException If an I/O error occurs
     */
    public static @NotNull List<String[]> parseCSV(@NotNull String filePath, @NotNull String seperator) throws IOException {
        List<String[]> csvData = new ArrayList<>();
        try (var br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                var values = line.split(seperator);
                csvData.add(values);
            }
        }
        return csvData;
    }
}
