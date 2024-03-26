package org.wonkglorg.files.scanner;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileScanner extends Scanner<FileScanner> {

    /**
     * Finds multiple results
     *
     * @return MultiResultScanner
     */
    public MultiResultScanner findMultiple() {
        return new MultiResultScanner(this);
    }

    /**
     * Finds a single result
     *
     * @return
     */
    public SingleResultScanner findSingle() {
        return new SingleResultScanner(this);
    }


    public static class MultiResultScanner extends Scanner<MultiResultScanner> {
        private int maxFileCount;

        public MultiResultScanner() {
            this.maxFileCount = -1;
        }

        public MultiResultScanner(Scanner scanner) {
            this.depthLimit = scanner.depthLimit;
            this.fileFilter = scanner.fileFilter;
            this.maxFileCount = -1;
            this.scanType = scanner.scanType;
            this.pathsToSearch = scanner.pathsToSearch;
            this.scanAll = scanner.scanAll;
        }

        public MultiResultScanner limit(int limit) {
            this.maxFileCount = limit;
            return this;
        }

        public List<Path> find() {
            List<File> paths = createFilePaths();
            List<Path> results = new ArrayList<>();


            switch (scanType) {
                case WIDTH -> wideFirstSearch(paths, results);
                case DEPTH -> depthFirstSearch(paths, results);
            }

            return results;
        }


    }

    public class SingleResultScanner extends Scanner<SingleResultScanner> {

        public SingleResultScanner(Scanner scanner) {
            this.depthLimit = scanner.depthLimit;
            this.fileFilter = scanner.fileFilter;
            this.scanType = scanner.scanType;
            this.pathsToSearch = scanner.pathsToSearch;
            this.scanAll = scanner.scanAll;
        }


        public Path find() {
            List<File> paths = createFilePaths();
            List<Path> results = new ArrayList<>();

            switch (scanType) {
                case WIDTH -> wideFirstSearch(paths, results);
                case DEPTH -> depthFirstSearch(paths, results);
            }

            if (results.isEmpty()) {
                return null;
            }

            return results.get(0);
        }
    }
}
