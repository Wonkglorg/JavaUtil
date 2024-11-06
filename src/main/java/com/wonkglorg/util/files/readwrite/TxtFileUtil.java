package com.wonkglorg.util.files.readwrite;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class TxtFileUtil {
	private final File file;
	private final Charset charset;

	private TxtFileUtil(File file, Charset charset) {
		this.file = file;
		this.charset = charset;
	}

	private List<String> readAll() {
		try (Stream<String> stream = Files.lines(file.toPath(), charset)) {
			return stream.toList();
		} catch (IOException e) {
			throw new RuntimeException("Error reading file: " + e.getMessage(), e);
		}
	}

	private List<String> read(int amount) {
		try (Stream<String> stream = Files.lines(file.toPath(), charset)) {
			return stream.limit(amount).toList();
		} catch (IOException e) {
			throw new RuntimeException("Error reading file: " + e.getMessage(), e);
		}
	}

	private void write(WriteType type, String... text) {
		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file, type == WriteType.APPEND), charset))) {
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
