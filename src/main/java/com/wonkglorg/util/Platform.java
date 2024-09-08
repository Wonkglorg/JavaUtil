package com.wonkglorg.util;

public enum Platform {
    WINDOWS(new String[]{"\\", "/", ":", "*", "?", "\"", "<", ">", "|"}),
    LINUX(new String[]{"/"}),

    ;

    private final String[] disallowedFileChars;

    Platform(String[] disallowedFileChars) {
        this.disallowedFileChars = disallowedFileChars;
    }


    public String[] getDisallowedFileChars() {
        return disallowedFileChars;
    }

    public static Platform getPlatform() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return WINDOWS;
        } else if (os.contains("nix") || os.contains("nux")) {
            return LINUX;
        } else {
            throw new IllegalStateException("Unsupported platform: " + os);
        }
    }
}
