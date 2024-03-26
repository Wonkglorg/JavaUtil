package com.wonkglorg.util.programms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Helper class to build a command line program with parameters and execute them
 */
@SuppressWarnings("unused")
public class ProgrammBuilder {

    private final List<Map.Entry<String, String>> parameters = new ArrayList<>();
    private final String programName;

    public ProgrammBuilder(String programName) {
        this.programName = programName;
    }

    /**
     * Adds a parameter without a value
     *
     * @param key
     * @return
     */
    public ProgrammBuilder put(String key) {
        parameters.add(Map.entry(key, ""));
        return this;
    }

    /**
     * Adds a parameter with a value
     *
     * @param key
     * @param value
     * @return
     */
    public ProgrammBuilder put(String key, String value) {
        parameters.add(Map.entry(key, value));
        return this;
    }

    /**
     * Returns an array of strings that can be used as arguments for a ProcessBuilder
     *
     * @return
     */
    public String[] buildArgumentArray() {
        List<String> list = new ArrayList<>();
        list.add(programName);
        for (Map.Entry<String, String> entry : parameters) {
            list.add(entry.getKey());
            if (!entry.getValue().isEmpty()) {
                list.add(entry.getValue());
            }
        }
        return list.toArray(new String[0]);
    }


    /**
     * Returns a user-friendly string representation
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(programName);
        for (Map.Entry<String, String> entry : parameters) {
            builder.append(" ");
            builder.append(entry.getKey());
            if (!entry.getValue().isEmpty()) {
                builder.append(" ");
                builder.append(entry.getValue());
            }

        }
        return builder.toString();
    }


    private List<Thread> runningThreads = new ArrayList<>();

    /**
     * Executes the command
     */
    public void execute(Set<OutputType> outputTypes) throws InterruptedException, IOException {
        System.out.println("Executing: " + this);
        System.out.println("-----------------------------------------");
        ProcessBuilder processBuilder = new ProcessBuilder(buildArgumentArray());
        Process process = processBuilder.start();

        if (outputTypes != null) {
            outputTypes.forEach(outputType -> runningThreads.add(startThread(outputType.getInputStream(process), outputType.getAction())));
        }

        process.waitFor();

        runningThreads.forEach(Thread::interrupt);
        runningThreads.clear();
        System.out.println("-----------------------------------------");
    }

    /**
     * Starts a thread that reads the from a stream and runs an action for each line
     *
     * @param stream
     * @param action
     * @return
     */
    private Thread startThread(InputStream stream, Consumer<String> action) {
        Thread thread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    action.accept(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        return thread;
    }


    public enum OutputType {
        INFO(Process::getInputStream, System.out::println), ERROR(Process::getErrorStream, System.err::println);

        private final Function<Process, InputStream> streamSupplier;
        private final Consumer<String> action;

        OutputType(Function<Process, InputStream> streamSupplier, Consumer<String> action) {
            this.streamSupplier = streamSupplier;
            this.action = action;
        }

        public Consumer<String> getAction() {
            return action;
        }

        public InputStream getInputStream(Process process) {
            return streamSupplier.apply(process);
        }
    }
}
