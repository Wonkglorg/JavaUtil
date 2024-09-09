package com.wonkglorg.util.web;

import org.jsoup.nodes.Document;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.wonkglorg.util.console.ConsoleColor.*;
import static com.wonkglorg.util.string.StringUtils.format;

public abstract class Downloader {
    private final Map<Status, Integer> downloadStatuses = new HashMap<>();
    protected final Logger logger = Logger.getLogger(Downloader.class.getName());
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public Downloader() {
        for (Status status : Status.values()) {
            downloadStatuses.put(status, 0);
        }
    }


    protected void incrementStatus(Status status) {
        downloadStatuses.computeIfPresent(status, (status1, count) -> count + 1);
    }

    protected DownloadResult downloadFile(String fileUrl, File filePath, boolean extraInfo) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fileUrl)).build();
            HttpResponse<InputStream> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
            Files.copy(response.body(), filePath.toPath());
            return new DownloadResult(fileUrl, filePath, Status.SUCCESS, "Downloaded " + fileUrl);
        } catch (FileAlreadyExistsException e) {
            if (extraInfo) {
                logger.log(Level.INFO, "File already exists: " + filePath);
            }
            return new DownloadResult(fileUrl, filePath, Status.SKIPPED, "File already exists");

        } catch (IllegalArgumentException e) {
            if (extraInfo) {
                logger.log(Level.INFO, "Invalid URL: " + fileUrl);
            }
            return new DownloadResult(fileUrl, filePath, Status.ERROR, "Invalid URL");

        } catch (Exception e) {
            if (extraInfo) {
                logger.log(Level.SEVERE, "Error downloading " + fileUrl + ": " + e.getMessage(), e);
            }
            return new DownloadResult(fileUrl, filePath, Status.ERROR, "Error downloading file", e);
        }
    }

    /**
     * Download a file from the given URL and save it to the target path.
     *
     * @param fileUrl  The URL to download the file from.
     * @param filePath The path to save the downloaded file to.
     * @return The downloaded / existing file or an empty optional if the download failed.
     */
    protected DownloadResult downloadFile(String fileUrl, File filePath) {
        return downloadFile(fileUrl, filePath, false);
    }


    /**
     * Get a document from the given URL.
     *
     * @param url       The URL to get the document from.
     * @param extraInfo Whether to log extra information.
     * @return The document or null if the document could not be retrieved.
     */
    protected Document getDocument(String url, boolean extraInfo) {
        try {
            return org.jsoup.Jsoup.connect(url).get();
        } catch (Exception e) {
            if (extraInfo) {
                logger.log(Level.SEVERE, "Error getting document from " + url + ": " + e.getMessage(), e);
            }
            return null;
        }
    }

    /**
     * Get a document from the given URL.
     *
     * @param url The URL to get the document from.
     * @return The document or null if the document could not be retrieved.
     */
    protected Document getDocument(String url) {
        return getDocument(url, false);
    }


    /**
     * Formats a console message with the current status progress of the processed files, skipped files and errors.
     * uses the values defined in {@link #downloadStatuses}
     *
     * @return
     */
    protected String formattedProgress() {
        return formattedProgress(downloadStatuses.get(Status.SUCCESS), downloadStatuses.get(Status.SKIPPED), downloadStatuses.get(Status.ERROR));
    }

    /**
     * Formats a console message with the current status progress of the processed files, skipped files and errors.
     * uses the values defined in {@link #downloadStatuses}
     *
     * @param timeTaken the time taken since start of download in ms
     * @return the formatted progress message
     */
    protected String formattedProgress(long timeTaken) {
        return formattedProgress(downloadStatuses.get(Status.SUCCESS), downloadStatuses.get(Status.SKIPPED), downloadStatuses.get(Status.ERROR), timeTaken);
    }

    /**
     * Formats a console message with the current status progress of the processed files, skipped files and errors.
     * uses the values defined in {@link #downloadStatuses}
     *
     * @param timeTaken   the time taken since start of download in ms
     * @param downloading the current file being downloaded
     * @return the formatted progress message
     */
    protected String formattedProgress(long timeTaken, String downloading) {
        return formattedProgress(downloadStatuses.get(Status.SUCCESS), downloadStatuses.get(Status.SKIPPED), downloadStatuses.get(Status.ERROR), timeTaken, downloading);
    }

    /**
     * Formats a console message with the current status progress of the processed files, skipped files and errors.
     * uses the values defined in {@link #downloadStatuses}
     *
     * @param success the number of successful downloads
     * @param skipped the number of skipped downloads
     * @param error   the number of failed downloads
     * @return the formatted progress message
     */
    protected String formattedProgress(int success, int skipped, int error) {
        String template = GREEN + "Success" + RESET + ": {0} " + BLUE + " Skipped" + RESET + ": {1} " + RED + "Error" + RESET + ": {2} ";
        return format(template, success, skipped, error);
    }


    /**
     * Formats a console message with the current status progress of the processed files, skipped files and errors.
     * uses the values defined in {@link #downloadStatuses}
     *
     * @param success       the number of successful downloads
     * @param skipped       the number of skipped downloads
     * @param error         the number of failed downloads
     * @param timeTakenInMS the time taken since start of download in ms
     * @return the formatted progress message
     */
    protected String formattedProgress(int success, int skipped, int error, long timeTakenInMS) {
        String template = GREEN + "Success" + RESET + ": {0} " + BLUE + " Skipped" + RESET + ": {1} " + RED + "Error" + RESET + ": {2} " + CYAN + "Time taken" + RESET + ": {3}s  " + RESET;
        return format(template, success, skipped, error, timeTakenInMS / 1000);
    }

    /**
     * Formats a console message with the current status progress of the processed files, skipped files and errors.
     * uses the values defined in {@link #downloadStatuses}
     *
     * @param success     the number of successful downloads
     * @param skipped     the number of skipped downloads
     * @param error       the number of failed downloads
     * @param timeTaken   the time taken since start of download in ms
     * @param downloading the current file being downloaded
     * @return the formatted progress message
     */
    protected String formattedProgress(int success, int skipped, int error, long timeTaken, String downloading) {
        String template = GREEN + "Success" + RESET + ": {0} " + BLUE + " Skipped" + RESET + ": {1} " + RED + "Error" + RESET + ": {2} " + CYAN + "Time taken" + RESET + ": {3}s  " + GRAY + "Downloading: {4} " + RESET;
        return format(template, success, skipped, error, timeTaken / 1000, downloading);
    }


}
