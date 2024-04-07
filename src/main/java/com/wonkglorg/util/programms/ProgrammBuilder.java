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
public class ProgrammBuilder {

    private final List<Map.Entry<String, String>> parameters = new ArrayList<>();
    private final String programName;

    public ProgrammBuilder(ProcessValue processValue, String programName) {
        switch (processValue) {
            case ENVIROMENT -> this.programName = System.getenv(programName);
            default -> this.programName = programName;
        }
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


    /**
     * Executes the command
     */
    public Map.Entry<Process, Map<OutputType, Thread>> execute(Set<OutputType> outputTypes) throws IOException {
        List<Thread> runningThreads = new ArrayList<>();
        System.out.println("Executing: " + this);
        ProcessBuilder processBuilder = new ProcessBuilder(buildArgumentArray());
        Process process = processBuilder.start();


        if (outputTypes != null) {
            outputTypes.forEach(outputType -> runningThreads.add(startThread(outputType.getInputStream(process), outputType.getAction())));
        }

        return Map.entry(process, Map.of(OutputType.INFO, runningThreads.get(0), OutputType.ERROR, runningThreads.get(1)));
    }

    /**
     * Executes the command without a console output as a return
     *
     * @param outputTypes
     * @throws IOException
     */
    public void executeWithoutConsole(Set<OutputType> outputTypes) throws IOException {
        System.out.println("Executing: " + this);
        System.out.println("-----------------------------------------");
        var result = execute(outputTypes);
        result.getValue().values().forEach(Thread::interrupt);
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

    public enum ProcessValue {
        /**
         * The program name is taken from the environment variables
         */
        ENVIROMENT,

        /**
         * The program name is taken as a literal
         */
        PATH;

    }
}
