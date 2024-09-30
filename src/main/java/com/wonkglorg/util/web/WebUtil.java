package com.wonkglorg.util.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import static com.wonkglorg.util.console.ConsoleColor.*;
import static com.wonkglorg.util.string.StringUtils.format;

public class WebUtil {
    private static final Logger LOGGER = Logger.getLogger(WebUtil.class.getName());
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    /**
     * Gets a stream from the given URL.
     *
     * @param url The URL
     * @return The stream or null if the stream could not be opened.
     */
    public static HttpResponse<InputStream> getStream(String url, boolean extraInfo) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
        } catch (IOException e) {
            if (extraInfo) {
                LOGGER.log(LOGGER.getLevel(), e.getMessage(), e);
            }
            return null;
        } catch (InterruptedException e) {
            if (extraInfo) {
                LOGGER.log(LOGGER.getLevel(), e.getMessage(), e);
            }
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * Gets a stream from the given URL.
     *
     * @param url The URL
     * @return The stream or null if the stream could not be opened.
     */
    public static HttpResponse<InputStream> getStream(String url) {
        return getStream(url, false);
    }

    /**
     * Downloads a file from the given URL and saves it to the target path.
     *
     * @param url        The URL to download the file from.
     * @param targetPath The path to save the downloaded file to.
     * @param options    The options to use when copying the file.
     * @return The downloaded / existing file or an empty optional if the download failed.
     */
    public static Optional<File> downloadFile(String url, String targetPath, boolean extraInfo, StandardCopyOption... options) {
        try (InputStream in = getStream(url).body()) {
            if (in == null) {
                return Optional.empty();
            }
            Path outputPath = Path.of(targetPath);
            Files.copy(in, outputPath, options);
            return Optional.of(outputPath.toFile());
        } catch (IOException e) {
            if (extraInfo) {
                LOGGER.log(LOGGER.getLevel(), e.getMessage(), e);
            }
            return Optional.empty();
        }
    }

    /**
     * Downloads a file from the given URL and saves it to the target path.
     *
     * @param url        The URL to download the file from.
     * @param targetPath The path to save the downloaded file to.
     * @param options    The options to use when copying the file.
     * @return The downloaded / existing file or an empty optional if the download failed.
     */
    public static Optional<File> downloadFile(String url, String targetPath, StandardCopyOption... options) {
        return downloadFile(url, targetPath, false, options);
    }

    /**
     * Downloads a file from the given URL and saves it to the target path. If the file already exists nothing happens
     *
     * @param url        The URL to download the file from.
     * @param targetPath The path to save the downloaded file to.
     * @return The downloaded file / existing file if it already exists or an empty optional if the download failed.
     */
    public static Optional<File> downloadFile(String url, String targetPath) {
        if (Files.exists(Path.of(targetPath))) {
            return Optional.of(new File(targetPath));
        }
        return downloadFile(url, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }


    /**
     * Checks if the given URL points to a file. this check is not 100% accurate but a good estimate, estimates based on the url structure and does not send any requests.
     *
     * @param url The URL
     * @return True if the URL points to a file, false otherwise.
     */
    public static boolean doesLinkPointToFile(String url) {
        try {
            return getUrlInfo(url).isFile();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Returns a {@link UrlInfo} object with structured information from the URL.
     *
     * @param urlString The URL
     * @return The structured information
     */
    public static UrlInfo getUrlInfo(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);

        String host = url.getHost();
        String protocol = url.getProtocol();
        String path = url.getPath();
        String query = url.getQuery();
        int port = url.getPort();

        String websiteName = getWebsiteName(host);
        String fileName = extractFileName(path);
        String extension = extractExtension(fileName);

        return new UrlInfo(urlString, host, websiteName, parseQueryString(query), port, path, protocol, fileName, extension);
    }

    private static String getWebsiteName(String host) {
        String[] parts = host.split("\\.");
        return parts.length > 2 ? parts[parts.length - 2] : parts[0];
    }

    private static String extractFileName(String path) {
        if (path == null || path.isEmpty()) return null;
        String fileName = path.substring(path.lastIndexOf('/') + 1);
        return fileName.isEmpty() || fileName.equals("/") ? null : fileName;
    }

    private static String extractExtension(String fileName) {
        return fileName != null && fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.') + 1) : null;
    }

    private static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> resultMap = new HashMap<>();
        if (queryString == null || queryString.isEmpty()) return resultMap;

        for (String pair : queryString.split("&")) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) resultMap.put(keyValue[0], keyValue[1]);
        }

        return resultMap;
    }

    /**
     * @param success how many files were downloaded successfully
     * @param skipped how many files were skipped
     * @param error   how many files failed to download
     * @return a formatted string with the download progress
     */
    public static String formattedProgress(int success, int skipped, int error) {
        String template = GREEN + "Success" + RESET + ": {0} " + BLUE + " Skipped" + RESET + ": {1} " + RED + "Error" + RESET + ": {2} " + RESET;
        return format(template, success, skipped, error);
    }
}


