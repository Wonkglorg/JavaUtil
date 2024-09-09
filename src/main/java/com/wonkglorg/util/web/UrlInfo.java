package com.wonkglorg.util.web;

import java.util.Map;

public record UrlInfo(String url, String host, String websiteName, Map<String, String> args, int port, String path,
                      String protocol) {
    @Override
    public String toString() {
        return "UrlInfo{" +
                "url='" + url + '\'' +
                ", host='" + host + '\'' +
                ", websiteName='" + websiteName + '\'' +
                ", args=" + args +
                ", port=" + port +
                ", path='" + path + '\'' +
                ", protocol='" + protocol + '\'' +
                '}';
    }
}
