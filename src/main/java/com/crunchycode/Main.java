
package com.crunchycode;

import com.crunchycode.model.Transaction;
import com.crunchycode.service.LoopProcessor;
import com.crunchycode.service.StreamProcessor;
import com.crunchycode.util.CsvReader;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {

        // Step 1: Read CSV file
        Path csvPath = Path.of("src/main/resources/transactions.csv");

        CsvReader csvReader = new CsvReader();

        List<Transaction> transactions = csvReader.read(csvPath);

        System.out.println("Total Records Loaded: " + transactions.size());


        // Step 2: Create LoopProcessor object
        LoopProcessor loopProcessor = new LoopProcessor();

        // Step 3: Execute Query 1 using Loops
        var loopResult =
                loopProcessor.findTopCustomers(transactions);

        System.out.println("\n===== TOP 10 CUSTOMERS USING LOOPS =====");

        for (Map.Entry<String, BigDecimal> entry : loopResult) {

            System.out.println(
                    entry.getKey() + " : " + entry.getValue()
            );
        }


        // Step 4: Create StreamProcessor object
        StreamProcessor streamProcessor = new StreamProcessor();

        // Step 5: Execute Query 1 using Streams
        var streamResult =
                streamProcessor.findTopCustomers(transactions);

        System.out.println("\n===== TOP 10 CUSTOMERS USING STREAMS =====");

        streamResult.forEach(entry ->
                System.out.println(
                        entry.getKey() + " : " + entry.getValue()
                )
        );

// ==========================================
// QUERY 2: MONTHLY TRANSACTION TOTALS
// ==========================================

// Loop approach
        Map<String, BigDecimal> loopMonthlyTotals =
                loopProcessor.calculateMonthlyTotals(transactions);

        System.out.println("\n===== MONTHLY TOTALS USING LOOPS =====");

        loopMonthlyTotals.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        System.out.println(
                                entry.getKey() + " : " + entry.getValue()
                        )
                );


// Stream approach
        Map<String, BigDecimal> streamMonthlyTotals =
                streamProcessor.calculateMonthlyTotals(transactions);

        System.out.println("\n===== MONTHLY TOTALS USING STREAMS =====");

        streamMonthlyTotals.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        System.out.println(
                                entry.getKey() + " : " + entry.getValue()
                        )
                );

// ==========================================
// QUERY 3: DUPLICATE TRANSACTIONS
// ==========================================

// Loop approach
        List<Transaction> loopDuplicates =
                loopProcessor.findDuplicateTransactions(transactions);

        System.out.println("\n===== DUPLICATES USING LOOPS =====");

        System.out.println("Total duplicate records: "
                + loopDuplicates.size());

        loopDuplicates.stream()
                .limit(10)
                .forEach(System.out::println);


// Stream approach
        List<Transaction> streamDuplicates =
                streamProcessor.findDuplicateTransactions(transactions);

        System.out.println("\n===== DUPLICATES USING STREAMS =====");

        System.out.println("Total duplicate records: "
                + streamDuplicates.size());

        streamDuplicates.stream()
                .limit(10)
                .forEach(System.out::println);


// Verify both approaches
        System.out.println("\nQuery 3 Results Match: "
                + loopDuplicates.equals(streamDuplicates));


// ==========================================
// QUERY 4: ANOMALY DETECTION
// ==========================================

// Loop approach
        List<Transaction> loopAnomalies =
                loopProcessor.findAnomalies(transactions);

        System.out.println("\n===== ANOMALIES USING LOOPS =====");

        System.out.println("Total anomalies: " + loopAnomalies.size());

        loopAnomalies.stream()
                .limit(10)
                .forEach(System.out::println);


// Stream approach
        List<Transaction> streamAnomalies =
                streamProcessor.findAnomalies(transactions);

        System.out.println("\n===== ANOMALIES USING STREAMS =====");

        System.out.println("Total anomalies: " + streamAnomalies.size());

        streamAnomalies.stream()
                .limit(10)
                .forEach(System.out::println);


// Verify both approaches
        System.out.println("\nQuery 4 Results Match: "
                + loopAnomalies.equals(streamAnomalies));

    }

}
