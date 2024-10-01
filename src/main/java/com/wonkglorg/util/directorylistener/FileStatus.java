package com.wonkglorg.util.directorylistener;

public class FileStatus {
    private long fileSize;
    private long lastModifiedTime;

    public FileStatus(long fileSize, long lastModifiedTime) {
        this.fileSize = fileSize;
        this.lastModifiedTime = lastModifiedTime;
    }

    public void setValues(long fileSize, long lastModifiedTime) {
        this.fileSize = fileSize;
        this.lastModifiedTime = lastModifiedTime;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FileStatus other) {
            return fileSize == other.fileSize && lastModifiedTime == other.lastModifiedTime;
        }
        return false;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "FileStatus{" +
                "fileSize=" + fileSize +
                ", lastModifiedTime=" + lastModifiedTime +
                '}';
    }
}