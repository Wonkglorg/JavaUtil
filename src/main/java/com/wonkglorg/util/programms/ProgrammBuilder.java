package com.wonkglorg.util.programms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Helper class to build a command line program with parameters and execute them
 */
public class ProgrammBuilder {
    private final String programName;

    public ProgrammBuilder(ProcessValue processValue, String programName) {
        switch (processValue) {
            case ENVIROMENT -> this.programName = System.getenv(programName);
            default -> this.programName = programName;
        }
    }


    /**
     * Returns an array of strings that can be used as arguments for a ProcessBuilder
     *
     * @return
     */
    public String[] buildArgumentArray(ProgrammString programmString) {
        List<String> list = new ArrayList<>();
        list.add(programName);
        for (Map.Entry<String, String> entry : programmString.getParameters()) {
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
        return programName;
    }


    /**
     * Executes the command
     * <p>
     * Returns a map with the process and the threads that are running the output streams (if non are specified the thread will wait for the process to finish, (otherwise {@link Process#waitFor()} should be called to ensure the process fully finished,
     */
    public Map.Entry<Process, Map<OutputType, Thread>> execute(ProgrammString programmString, Set<OutputType> outputTypes) throws IOException {
        System.out.println("Executing: " + this);
        ProcessBuilder processBuilder = new ProcessBuilder(buildArgumentArray(programmString));
        Process process = processBuilder.start();

        HashMap<OutputType, Thread> threadMap = new HashMap<>();


        if (outputTypes == null || outputTypes.isEmpty()) {
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }


        for (var outputType : outputTypes) {
            threadMap.put(outputType, startThread(outputType.getInputStream(process), outputType.getAction()));
        }

        return Map.entry(process, threadMap);
    }

    /**
     * Executes the command without a console output as a return
     *
     * @param outputTypes
     * @throws IOException
     */
    public void executeWithoutConsole(ProgrammString programmString, Set<OutputType> outputTypes) throws IOException, InterruptedException {
        System.out.println("Executing: " + this);
        System.out.println("-----------------------------------------");
        var result = execute(programmString, outputTypes);
        result.getKey().waitFor();
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
