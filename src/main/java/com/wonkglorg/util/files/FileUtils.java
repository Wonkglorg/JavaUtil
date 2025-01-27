package com.wonkglorg.util.files;

import com.wonkglorg.util.Platform;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {

	/**
	 * Create a symbolic link to a target file.
	 *
	 * @param file the link file
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
	 * @param fileName the file name to sanitize
	 * @param platform the platform to use
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
			fileName = fileName.replaceAll(Pattern.quote(c), replacement);

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

	/**
	 * Opens a file chooser dialog to select a file.
	 *
	 * @param title The title of the dialog
	 * @param defaultDir The default directory
	 * @param filter The file filter
	 * @return The selected file or null if no file was selected
	 */
	public static File selectFile(String title, File defaultDir, FileNameExtensionFilter filter) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(title);
		fileChooser.setCurrentDirectory(defaultDir);
		fileChooser.setFileFilter(filter);
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}

	/**
	 * Opens a directory chooser dialog to select a directory.
	 *
	 * @param title The title of the dialog
	 * @param defaultDir The default directory
	 * @return The selected directory or null if no directory was selected
	 */
	public static File selectDirectory(String title, File defaultDir) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(title);
		fileChooser.setCurrentDirectory(defaultDir);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}

	/**
	 * Create a shortcut file resolves all needed paths (does not overwrite existing files).
	 *
	 * @param path The path to create the shortcut in
	 * @param filename the name of the shortcut file
	 * @param url The URL to link to
	 * @return true if the shortcut was created, false otherwise
	 */
	public static boolean createShortCutFile(Path path, String filename, String url) {
		String shortcutContent = "[InternetShortcut]\nURL=" + url + "\n";

		if (!filename.endsWith(".url")) {
			filename += ".url";
		}
		File file = path.resolve(filename).toFile();

		if (file.exists()) {
			return false;
		}
		if (file.getParentFile() != null && !file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			file.createNewFile();
			writer.write(shortcutContent);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	/**
	 * Gets the extension of a file
	 *
	 * @param file the file to get the extension from
	 * @return null or a valid extension
	 */
	public static String getExtension(File file) {
		String fileName = file.getName();
		int index = fileName.lastIndexOf('.');
		if (index == -1) {
			return null;
		}
		return fileName.substring(index + 1);
	}

	/**
	 * Changes the extension of a file to a new one
	 *
	 * @param f the file to change extension for
	 * @param newExtension the new extension to use
	 * @return the new rewritten file
	 */
	public static File changeExtension(File f, String newExtension) {
		int i = f.getName().lastIndexOf('.');
		String name = f.getName().substring(0, i == -1 ? f.getName().length() - 1 : i);
		return new File(f.getParent(), name + "." + newExtension);
	}

	/**
	 * Zips a File
	 *
	 * @param zipFile the outputZipFile
	 * @param filesToZip the content to zip (can be files or directories)
	 * @return the zipped file
	 */
	public static File zipFile(File zipFile, File... filesToZip) {
		try (FileOutputStream fos = new FileOutputStream(zipFile);
				ZipOutputStream zipOut = new ZipOutputStream(fos)) {

			for (File fileToZip : filesToZip) {
				addFileToZip(fileToZip, fileToZip.getName(), zipOut);
			}

		} catch (IOException e) {
			throw new RuntimeException("Error zipping files.", e);
		}
		return zipFile;
	}

	private static void addFileToZip(File fileToZip, String fileName, ZipOutputStream zipOut)
			throws IOException {
		if (fileToZip.isHidden()) {
			return; // Skip hidden files
		}

		if (fileToZip.isDirectory()) {
			if (fileName.endsWith("/")) {
				zipOut.putNextEntry(new ZipEntry(fileName));
				zipOut.closeEntry();
			} else {
				zipOut.putNextEntry(new ZipEntry(fileName + "/"));
				zipOut.closeEntry();
			}
			File[] children = fileToZip.listFiles();
			if (children != null) {
				for (File childFile : children) {
					addFileToZip(childFile, fileName + "/" + childFile.getName(), zipOut);
				}
			}
		} else {
			try (FileInputStream fis = new FileInputStream(fileToZip)) {
				ZipEntry zipEntry = new ZipEntry(fileName);
				zipOut.putNextEntry(zipEntry);

				byte[] bytes = new byte[1024];
				int length;
				while ((length = fis.read(bytes)) >= 0) {
					zipOut.write(bytes, 0, length);
				}
			}
		}
	}

}
