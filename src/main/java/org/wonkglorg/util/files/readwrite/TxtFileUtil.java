package org.wonkglorg.files.readwrite;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TxtFileUtil {


    private final File file;
    private final Charset charset;

    private TxtFileUtil(File file, Charset charset) {
        this.file = file;
        this.charset = charset;
    }

    private List<String> readAll() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> read(int amount) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
            List<String> lines = new ArrayList<>();
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null && count < amount) {
                lines.add(line);
                count++;
            }
            return lines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(WriteType type, String... text) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, type == WriteType.APPEND), charset))) {
            for (String line : text) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeToFile(String path, WriteType type, Charset charset, String... text) {
        new TxtFileUtil(new File(path), charset).write(type, text);
    }

    public static void writeToFile(File file, WriteType type, Charset charset, String... text) {
        new TxtFileUtil(file, charset).write(type, text);
    }

    public static void writeToFile(String path, WriteType type, Charset charset, List<String> text) {
        new TxtFileUtil(new File(path), charset).write(type, text.toArray(new String[0]));
    }

    public static List<String> readFromFile(String path, Charset charset) {
        return new TxtFileUtil(new File(path), charset).readAll();
    }

    public static List<String> readFromFile(File file, Charset charset) {
        return new TxtFileUtil(file, charset).readAll();
    }

    public static List<String> readFromFile(String path, Charset charset, int amount) {
        return new TxtFileUtil(new File(path), charset).read(amount);
    }
}
