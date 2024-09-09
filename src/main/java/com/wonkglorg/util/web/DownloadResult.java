package com.wonkglorg.util.web;

import java.io.File;

public record DownloadResult(String url, File file, Status status, String message, Exception exception) {

    public DownloadResult(String url, File file, Status status, String message) {
        this(url, file, status, message, null);
    }

    public DownloadResult(String url, File file, Status status) {
        this(url, file, status, null, null);
    }

    boolean wasSuccessful() {
        return status == Status.SUCCESS || status == Status.SKIPPED;
    }
}
