
package com.crunchycode.service;

import com.crunchycode.model.Transaction;
import com.crunchycode.util.CsvReader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Benchmark {

    private static final int WARMUP_RUNS = 2;
    private static final int MEASURE_RUNS = 5;

    public static void main(String[] args) throws IOException {

        Path csvPath = args.length > 0
                ? Path.of(args[0])
                : Path.of("src/main/resources/transactions.csv");

        // Load dataset
        List<Transaction> transactions =
                new CsvReader().read(csvPath);

        System.out.println("\n===== PERFORMANCE BENCHMARK =====");
        System.out.println("Dataset size: " + transactions.size());

        LoopProcessor loopProcessor = new LoopProcessor();
        StreamProcessor streamProcessor = new StreamProcessor();

        // Verify that both approaches return matching results
        System.out.println("\n===== RESULT VERIFICATION =====");

        System.out.println("Query 1: " +
                loopProcessor.findTopCustomers(transactions)
                        .equals(streamProcessor.findTopCustomers(transactions)));

        System.out.println("Query 2: " +
                loopProcessor.calculateMonthlyTotals(transactions)
                        .equals(streamProcessor.calculateMonthlyTotals(transactions)));

        System.out.println("Query 3: " +
                loopProcessor.findDuplicateTransactions(transactions)
                        .equals(streamProcessor.findDuplicateTransactions(transactions)));

        System.out.println("Query 4: " +
                loopProcessor.findAnomalies(transactions)
                        .equals(streamProcessor.findAnomalies(transactions)));


        // Query 1 benchmark
        benchmark("Q1 - Top Customers (Loops)",
                () -> loopProcessor.findTopCustomers(transactions));

        benchmark("Q1 - Top Customers (Streams)",
                () -> streamProcessor.findTopCustomers(transactions));


        // Query 2 benchmark
        benchmark("Q2 - Monthly Totals (Loops)",
                () -> loopProcessor.calculateMonthlyTotals(transactions));

        benchmark("Q2 - Monthly Totals (Streams)",
                () -> streamProcessor.calculateMonthlyTotals(transactions));


        // Query 3 benchmark
        benchmark("Q3 - Duplicates (Loops)",
                () -> loopProcessor.findDuplicateTransactions(transactions));

        benchmark("Q3 - Duplicates (Streams)",
                () -> streamProcessor.findDuplicateTransactions(transactions));


        // Query 4 benchmark
        benchmark("Q4 - Anomalies (Loops)",
                () -> loopProcessor.findAnomalies(transactions));

        benchmark("Q4 - Anomalies (Streams)",
                () -> streamProcessor.findAnomalies(transactions));
    }


    private static void benchmark(String name, Runnable task) {

        // Warm-up
        for (int i = 0; i < WARMUP_RUNS; i++) {
            task.run();
        }

        long totalTime = 0;

        // Actual measurements
        for (int i = 0; i < MEASURE_RUNS; i++) {

            long start = System.nanoTime();

            task.run();

            long end = System.nanoTime();

            totalTime += (end - start);
        }

        double averageMs =
                totalTime / (double) MEASURE_RUNS / 1_000_000.0;

        System.out.printf(
                "%-38s : %.3f ms%n",
                name,
                averageMs
        );
    }
}
