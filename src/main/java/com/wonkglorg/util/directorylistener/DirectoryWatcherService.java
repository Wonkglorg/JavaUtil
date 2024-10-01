package com.wonkglorg.util.directorylistener;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static java.nio.file.StandardWatchEventKinds.*;

public class DirectoryWatcherService {

    private final Path directoryToWatch;
    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduler;
    private Consumer<Exception> exceptionHandler = e -> e.printStackTrace();
    /**
     * A set of files that are being monitored for stability (after a file has been created it is monitored until it is stable (no longer changing in size and last modified time)) to determin when to call the file created method, also prevents  modify calls being made for the file as part of its creation process
     */
    private final Map<Path, FileStatus> fileStabilityMonitorMap = new ConcurrentHashMap<>();
    private final Map<WatchEvent.Kind<Path>, Consumer<Path>> eventConsumerMap = new ConcurrentHashMap<>();
    private long checkInterval = 500L;

    public DirectoryWatcherService(Path directoryToWatch, long checkInterval) {
        this.directoryToWatch = directoryToWatch;
        this.executorService = Executors.newSingleThreadExecutor();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.checkInterval = checkInterval;
    }

    public DirectoryWatcherService(Path directoryToWatch) {
        this(directoryToWatch, 500L);
    }

    /**
     * Starts watching the directory for file creation, modification and deletion events
     */
    @SuppressWarnings("unchecked")
    public void startWatching() {
        if (!executorService.isTerminated()) {
            return;
        }
        monitorFilesUntilStable();
        executorService.submit(() -> {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                directoryToWatch.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

                while (true) {
                    WatchKey key = watchService.take();

                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();

                        if (kind == null || kind == OVERFLOW) continue;

                        Path fileName = directoryToWatch.resolve(((WatchEvent<Path>) event).context());

                        if (kind == ENTRY_CREATE) {
                            fileStabilityMonitorMap.put(fileName, getFileStatus(fileName));
                        } else if (kind == ENTRY_MODIFY) {
                            //skips the event call cause its still being monitored for stability
                            if (fileStabilityMonitorMap.containsKey(fileName)) continue;
                            executeEventHandler(ENTRY_MODIFY, fileName);
                        } else if (kind == ENTRY_DELETE) {
                            executeEventHandler(ENTRY_DELETE, fileName);
                        }
                    }

                    key.reset();
                }
            } catch (Exception e) {
                exceptionHandler.accept(e);
            }
        });
    }


    /**
     * Monitors files until they are stable (no longer changing in size and last modified time) and then calls the file created event
     */
    private void monitorFilesUntilStable() {
        scheduler.scheduleAtFixedRate(() ->//
                fileStabilityMonitorMap.forEach((fileName, currentStatus) -> {
                    FileStatus newStatus = getFileStatus(fileName);
                    if (currentStatus.equals(newStatus)) {
                        fileStabilityMonitorMap.remove(fileName);
                        executeEventHandler(ENTRY_CREATE, fileName);
                    } else {
                        currentStatus.setValues(newStatus.getFileSize(), newStatus.getLastModifiedTime());
                    }

                    if (!Files.exists(fileName)) {
                        fileStabilityMonitorMap.remove(fileName);
                    }
                }), 0, checkInterval, TimeUnit.MILLISECONDS);  // Run the check at a regular interval
    }

    public void stopWatching() throws InterruptedException {
        executorService.awaitTermination(3, TimeUnit.SECONDS);
        scheduler.awaitTermination(3, TimeUnit.SECONDS);
    }

    public void cancel() {
        executorService.shutdownNow();
        scheduler.shutdownNow();
    }

    /**
     * Get the file status for a file
     *
     * @param fileName the file to get the status for
     * @return the file status
     */
    private FileStatus getFileStatus(Path fileName) {
        try {
            long fileSize = Files.size(fileName);
            BasicFileAttributes attributes = Files.readAttributes(fileName, BasicFileAttributes.class);
            long lastModifiedTime = attributes.lastModifiedTime().toMillis();
            return new FileStatus(fileSize, lastModifiedTime);
        } catch (IOException e) {
            exceptionHandler.accept(e);
            return null;
        }
    }

    /**
     * Register a handler for file creation event gets called once when a file is finished creating inside the directory being watched (also called as part of a rename operation=
     *
     * @param handler the handler to run when a file is created
     * @return the DirectoryWatcherService instance
     */
    public DirectoryWatcherService onFileCreated(Consumer<Path> handler) {
        onEvent(ENTRY_CREATE, handler);
        return this;
    }

    /**
     * Register a handler for file modification events, only gets called when a file is changed(contents of a file, renaming does not count as modifying but calls delete followed by create instead) and not when created (which also triggers multiple modification events)
     *
     * @param handler the handler to run when a file is modified
     * @return the DirectoryWatcherService instance
     */
    public DirectoryWatcherService onFileModified(Consumer<Path> handler) {
        onEvent(ENTRY_MODIFY, handler);
        return this;
    }

    /**
     * Register a handler for file deletion events, gets called once when a file is deleted (or as part of a rename operation)
     *
     * @param handler the handler to run when a file is deleted
     * @return the DirectoryWatcherService instance
     */
    public DirectoryWatcherService onFileDeleted(Consumer<Path> handler) {
        onEvent(ENTRY_DELETE, handler);
        return this;
    }

    public DirectoryWatcherService onException(Consumer<Exception> handler) {
        exceptionHandler = handler;
        return this;
    }

    private void executeEventHandler(WatchEvent.Kind<Path> kind, Path fileName) {
        Consumer<Path> handler = eventConsumerMap.get(kind);
        if (handler != null) {
            handler.accept(fileName);
        }
    }

    private void onEvent(WatchEvent.Kind<Path> eventKind, Consumer<Path> eventHandler) {
        eventConsumerMap.put(eventKind, eventHandler);
    }

}
