package com.wonkglorg.util.test;


import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.wonkglorg.util.console.ConsoleUtil.println;
import static com.wonkglorg.util.console.ConsoleUtil.printlnFormated;
import static com.wonkglorg.util.string.StringUtils.*;

public class TimingReport {
    private final String name;
    private final Map<Long, Timing> timings;
    private long shortestExecutionInNs;
    private long longestExecutionInNs;
    private long averageExecutionInNs;
    private long totalExecutionTimeInNs;

    public TimingReport(final String name, Map<Long, Timing> timings) {
        this.name = name;
        this.timings = timings;
        averageExecutionInNs = calculateAverageExecutionTime();
        shortestExecutionInNs = calculateShortestExecutionTime();
        longestExecutionInNs = calculateLongestExecutionTime();
        totalExecutionTimeInNs = calculateTotalDuration();
    }


    public String getName() {
        return name;
    }

    public Map<Long, Timing> getTimings() {
        return timings;
    }

    public long calculateAverageExecutionTime() {
        return timings.values().stream().mapToLong(Timing::duration).reduce(0L, Long::sum)
                / timings.size();
    }

    public long calculateShortestExecutionTime() {
        return timings.values().stream().mapToLong(Timing::duration).min().getAsLong();
    }


    public long calculateLongestExecutionTime() {
        return timings.values().stream().mapToLong(Timing::duration).max().getAsLong();
    }

    public long calculateTotalDuration() {
        return timings.values().stream().mapToLong(Timing::duration).sum();
    }

    public long getShortestExecutionInNs() {
        return shortestExecutionInNs;
    }

    public long getLongestExecutionInNs() {
        return longestExecutionInNs;
    }

    public double getAverageExecutionInNs() {
        return averageExecutionInNs;
    }

    public long getTotalExecutionTimeInNs() {
        return totalExecutionTimeInNs;
    }

    /**
     * Compares multiple timing reports in a datatable view:
     * <pre>
     *   Name    |  Total  |  Average  |  Min  |  Max
     *   --------------------------------------------
     *   Name 1  |         |           |       |
     *   Name 2  |         |           |       |
     *   Name 3  |         |           |       |
     * </pre>
     *
     * @param reports the reports to compare
     */
    public static void printComparison(List<TimingReport> reports) {

        int totalTimeStringLength = Math.max(13, String.valueOf(
                        reports.stream().mapToLong(report -> report.totalExecutionTimeInNs).max().orElse(0))
                .length() + 3);

        int averageTimeStringLength = Math.max(13, String.valueOf(
                        reports.stream().mapToDouble(report -> report.averageExecutionInNs).max().orElse(0))
                .length() + 3);

        int shortestTimeStringLength = Math.max(13, String.valueOf(
                reports.stream().mapToLong(report -> report.shortestExecutionInNs).max().orElse(0)).length()
                + 3);

        int longestTimeStringLength = Math.max(13, String.valueOf(
                reports.stream().mapToLong(report -> report.longestExecutionInNs).max().orElse(0)).length()
                + 3);

        int maxTimingNameLength =
                5 + reports.stream().max(Comparator.comparingInt((report -> report.name.length())))
                        .orElseGet(() -> new TimingReport("          ", null)).name.length();

        String nameTitle = padCenter("Name", maxTimingNameLength);
        String totalTitle = padCenter("Total", totalTimeStringLength);
        String averageTitle = padCenter("Average", averageTimeStringLength);
        String minTitle = padCenter("Min", shortestTimeStringLength);
        String maxTitle = padCenter("Max", longestTimeStringLength);
        printlnFormated("{0}|{1}|{2}|{3}|{4}", nameTitle, totalTitle, averageTitle, minTitle,
                maxTitle);

        int totalTableWidth =
                totalTimeStringLength + averageTimeStringLength + shortestTimeStringLength
                        + longestTimeStringLength + maxTimingNameLength;
        println(padCenter("-", totalTableWidth, '-'));

        for (TimingReport report : reports) {
            String name = padCenter(report.name, maxTimingNameLength);
            String total = padLeft(report.totalExecutionTimeInNs, totalTimeStringLength - 4);
            String average = padLeft(report.averageExecutionInNs, averageTimeStringLength - 4);
            String shortest = padLeft(report.shortestExecutionInNs, shortestTimeStringLength - 4);
            String longest = padLeft(report.longestExecutionInNs, longestTimeStringLength - 4);
            printlnFormated("{0}|{1} ns |{2} ns |{3} ns |{4} ns", name, total, average, shortest,
                    longest);
        }
        println(padCenter("-", totalTableWidth, '-'));
    }


    /**
     * Prints a nicer table view of the report
     * <pre>
     *   ------------------------------------Name----------------------------------
     *   Total		|
     *   Average	|
     *   Min			|
     *   Max			|
     *   ------------------------------------------------------------------
     *   Timings	|
     *  Iteration	|	Duration	|
     *         01 | 	 301 ms |
     *   -----------------------------------------------------------------------------
     * </pre>
     */
    public void printReport(boolean showAllTimings) {
        int descriptionColumnWidth = 15;
        int valueColumnWidth = 14;

        int maxLengthFound =
                Math.max(valueColumnWidth, String.valueOf(totalExecutionTimeInNs).length());

        println(padCenter(name, 70, '-'));
        printKeyVal("Total", descriptionColumnWidth, totalExecutionTimeInNs, maxLengthFound);
        printKeyVal("Average", descriptionColumnWidth, averageExecutionInNs, maxLengthFound);
        printKeyVal("Shortest", descriptionColumnWidth, shortestExecutionInNs, maxLengthFound);
        printKeyVal("Longest", descriptionColumnWidth, longestExecutionInNs, maxLengthFound);

        if (showAllTimings) {
            println();
            String iterationString = padLeft("Iteration", descriptionColumnWidth);
            String timeStringValue = padLeft("Time", descriptionColumnWidth);
            printlnFormated("{0}|{1}", iterationString, timeStringValue);

            for (Timing timing : timings.values()) {
                String iteration = padLeft(timing.iteration, descriptionColumnWidth);
                String duration = padLeft(timing.duration, descriptionColumnWidth);
                printlnFormated("{0}|{1} ns", iteration, duration);
            }
        }
        println(padCenter("", 70, '-'));
    }

    private void printKeyVal(String key, int keyWidth, Object val, int valueWidth) {
        String formattedKey = padLeft(key, keyWidth);
        String formattedValue = padLeft(val, valueWidth);
        printlnFormated("{0}|{1} ns", formattedKey, formattedValue);
    }


    public record Timing(long iteration, long duration) {
        @Override
        public String toString() {
            return format("Timing{iteration={0}ns, duration={1}ns", iteration, duration);
        }
    }

    @Override
    public String toString() {
        return format(
                "TimingReport{name={0}, min={1}ns, average={2}ns, max={3}ns, duration={4}ns, timings={5}}",
                name, calculateShortestExecutionTime(), calculateAverageExecutionTime(),
                calculateLongestExecutionTime(), calculateTotalDuration(), timings);
    }
}
