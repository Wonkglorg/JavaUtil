package com.wonkglorg.util.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.logging.Logger;

public class DownloaderUtil {
    private static final Logger LOGGER = Logger.getLogger(DownloaderUtil.class.getName());
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    /**
     * Gets a stream from the given URL.
     *
     * @param url The URL
     * @return The stream or null if the stream could not be opened.
     */
    public HttpResponse<InputStream> getStream(String url, boolean extraInfo) {
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
    public HttpResponse<InputStream> getStream(String url) {
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
    public Optional<File> downloadFile(String url, String targetPath, boolean extraInfo, StandardCopyOption... options) {
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
    public Optional<File> downloadFile(String url, String targetPath, StandardCopyOption... options) {
        return downloadFile(url, targetPath, false, options);
    }

    /**
     * Downloads a file from the given URL and saves it to the target path. If the file already exists nothing happens
     *
     * @param url        The URL to download the file from.
     * @param targetPath The path to save the downloaded file to.
     * @return The downloaded file / existing file if it already exists or an empty optional if the download failed.
     */
    public Optional<File> downloadFile(String url, String targetPath) {
        if (Files.exists(Path.of(targetPath))) {
            return Optional.of(new File(targetPath));
        }
        return downloadFile(url, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }
}
