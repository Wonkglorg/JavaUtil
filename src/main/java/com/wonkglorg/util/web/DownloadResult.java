package com.wonkglorg.util.web;

public record DownloadResult(String url, String filename, Status status) {
    boolean wasSuccessful() {
        return status == Status.SUCCESS || status == Status.SKIPPED;
    }
}
