package org.wonkglorg.util.files.scanner;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unused")
public abstract class Scanner<T extends Scanner<T>> {
    protected String rootPath;
    protected int depthLimit;
    protected List<Function<File, Boolean>> fileFilter;
    protected ScanType scanType;
    protected List<String> pathsToSearch;
    protected int maxFileCount;
    protected boolean scanAll = false;

    public Scanner() {
        this.depthLimit = 20;
        this.rootPath = "";
        fileFilter = new ArrayList<>();
        scanType = ScanType.WIDTH;
        pathsToSearch = new ArrayList<>();
        maxFileCount = -1;
    }

    /**
     * Sets the maximum number of files it can go deep into (default: -1)
     *
     * @param depth -1 = no limit
     * @return
     */
    public T depth(int depth) {
        this.depthLimit = depth;
        return (T) this;
    }

    /**
     * Adds a filter to the scanner
     *
     * @param filter
     * @return
     */
    public T filter(Function<File, Boolean> filter) {
        this.fileFilter.add(filter);
        return (T) this;
    }

    /**
     * Scans all drives on pc
     *
     * @return
     */
    public T scanAll() {
        this.scanAll = true;
        return (T) this;
    }

    public T searchPath(String... path) {
        Collections.addAll(this.pathsToSearch, path);
        return (T) this;
    }


    /**
     * Sets the type of scan to be performed, (default: width first)
     *
     * @param scanType
     * @return
     */
    public T scanType(ScanType scanType) {
        this.scanType = scanType;
        return (T) this;
    }


    protected List<File> createFilePaths() {
        List<File> roots;
        if (scanAll) {
            roots = List.of(File.listRoots());
        } else {
            roots = pathsToSearch.stream().map(File::new).toList();
        }
        return roots;

    }


    protected void wideFirstSearch(List<File> paths, List<Path> results) {
        Queue<Map.Entry<Integer, File>> queue = new LinkedList<>();
        int found = 0;
        for (File path : paths) {
            queue.offer(Map.entry(0, path));
        }

        while (!queue.isEmpty()) {
            var entry = queue.poll();
            File currentFile = entry.getValue();
            int depth = entry.getKey();
            if (matchesCriteria(currentFile)) {
                if (found >= maxFileCount && maxFileCount != -1) {
                    break;
                }
                results.add(currentFile.toPath());
                found++;
            }

            if (!currentFile.isDirectory()) continue;

            if (depthLimit != -1 && depth > depthLimit) continue;

            File[] children = currentFile.listFiles();

            if (children == null) {
                continue;
            }

            for (File child : children) {
                queue.offer(Map.entry(depth + 1, child));
            }


        }
    }

    protected void depthFirstSearch(List<File> paths, List<Path> results) {
        Stack<Map.Entry<Integer, File>> stack = new Stack<>();
        int found = 0;
        for (File path : paths) {
            stack.push(Map.entry(0, path));
        }

        while (!stack.isEmpty()) {
            var entry = stack.pop();
            File currentFile = entry.getValue();
            int depth = entry.getKey();
            if (matchesCriteria(currentFile)) {
                if (found <= maxFileCount || maxFileCount != -1) {
                    break;
                }
                results.add(currentFile.toPath());
                found++;
            }

            if (!currentFile.isDirectory()) continue;

            if (depthLimit != -1 && depth > depthLimit) continue;

            File[] children = currentFile.listFiles();

            if (children == null) continue;

            for (File child : children) {
                stack.push(Map.entry(depth + 1, child));
            }
        }
    }

    private boolean matchesCriteria(File file) {
        for (var filter : fileFilter) {
            if (!filter.apply(file)) {
                return false;
            }
        }
        return true;
    }
}
