package com.wonkglorg.util.web;

import java.util.Map;

/**
 * Represents information about a URL.
 *
 * @param url         The raw url string unmodified
 * @param host        The host of the URL
 * @param websiteName The name of the website
 * @param args        The arguments sent with the url
 * @param port        The port of the URL
 * @param path
 * @param protocol    The protocol
 * @param filename    The filename or null if it does not point to a file
 * @param extension   The extension of the file or null if it does not point to a file
 */
public record UrlInfo(String url, String host, String websiteName, Map<String, String> args, int port, String path,
                      String protocol, String filename, String extension) {


    /**
     * Returns true if the URL is a file.
     *
     * @return True if the URL is a file, false otherwise.
     */
    public boolean isFile() {
        return filename != null && extension != null;
    }


    @Override
    public String toString() {
        return "UrlInfo{" + "url='" + url + '\'' + ", host='" + host + '\'' + ", websiteName='" + websiteName + '\'' + ", args=" + args + ", port=" + port + ", path='" + path + '\'' + ", protocol='" + protocol + '\'' + '}';
    }
}
