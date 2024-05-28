package com.wonkglorg.util.converter.date;

/**
 * Enum for common date types
 */
public enum DateType {
    MILLISECOND("ms", 1),
    SECOND("s", 1000L),
    MINUTE("m", 1000L * 60),
    HOUR("h", 1000L * 60 * 60),
    DAY("d", 1000L * 60 * 60 * 24),
    WEEK("W", 1000L * 60 * 60 * 24 * 7),
    MONTH("M", 1000L * 60 * 60 * 24 * 7 * 30),
    YEAR("Y", 1000L * 60 * 60 * 24 * 7 * 30 * 365);

    private final String preset;
    private final long toMilliseconds;

    DateType(String preset, long toMilliseconds) {
        this.preset = preset;
        this.toMilliseconds = toMilliseconds;
    }

    public String getPreset() {
        return preset;
    }

    public long getMilliseconds() {
        return toMilliseconds;
    }
}
