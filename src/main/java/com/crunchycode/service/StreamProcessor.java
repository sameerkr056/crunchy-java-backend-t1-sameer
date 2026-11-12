
package com.crunchycode.service;

import com.crunchycode.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class StreamProcessor {

    // Query 1: Find Top 10 Customers using Streams
    public List<Map.Entry<String, BigDecimal>> findTopCustomers(
            List<Transaction> transactions) {

        return transactions.stream()

                // Step 1: Group transactions by customer ID
                .collect(Collectors.groupingBy(
                        Transaction::getCustomerId,

                        // Step 2: Calculate total amount
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ))

                // Step 3: Convert Map into Stream
                .entrySet()
                .stream()

                // Step 4: Sort in descending order
                .sorted(
                        Map.Entry.<String, BigDecimal>comparingByValue()
                                .reversed()
                                .thenComparing(Map.Entry.comparingByKey())
                )

                // Step 5: Get Top 10 customers
                .limit(10)

                // Step 6: Collect results
                .toList();
    }

    // Query 2: Monthly Totals using Streams
    public Map<String, BigDecimal> calculateMonthlyTotals(
            List<Transaction> transactions) {

        return transactions.stream()

                // Group transactions by year-month
                .collect(Collectors.groupingBy(

                        transaction -> transaction.getDate()
                                .toString()
                                .substring(0, 7),

                        // Calculate total amount for each month
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));
    }

    // Query 3: Find Duplicate Transactions using Streams
    public List<Transaction> findDuplicateTransactions(
            List<Transaction> transactions) {

        return transactions.stream()

                // Group identical transaction records
                .collect(Collectors.groupingBy(transaction ->
                        new DuplicateKey(
                                transaction.getTransactionId(),
                                transaction.getCustomerId(),
                                transaction.getAmount(),
                                transaction.getDate(),
                                transaction.getCategory()
                        )
                ))

                // Keep only records appearing more than once
                .values()
                .stream()
                .filter(group -> group.size() > 1)

                // Get one representative duplicate record
                .map(group -> group.get(1))

                // Sort by transaction ID
                .sorted(Comparator.comparing(
                        Transaction::getTransactionId))

                .toList();
    }

    // Key representing the complete transaction record
    private record DuplicateKey(
            String transactionId,
            String customerId,
            BigDecimal amount,
            LocalDate date,
            String category
    ) {}

    // Query 4: Anomaly Detection using Streams
    public List<Transaction> findAnomalies(
            List<Transaction> transactions) {

        BigDecimal threshold = new BigDecimal("200000");

        return transactions.stream()

                // Filter transactions above threshold
                .filter(transaction ->
                        transaction.getAmount().compareTo(threshold) > 0)

                // Collect matching transactions
                .toList();
    }


}
