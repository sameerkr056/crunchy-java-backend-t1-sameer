
package com.crunchycode.service;

import com.crunchycode.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class LoopProcessor {

    // Query 1: Find Top 10 Customers using Loops
    public List<Map.Entry<String, BigDecimal>> findTopCustomers(
            List<Transaction> transactions) {

        // Step 1: Store customer ID and total amount
        Map<String, BigDecimal> customerTotals = new HashMap<>();

        // Step 2: Calculate total amount for each customer
        for (Transaction transaction : transactions) {

            String customerId = transaction.getCustomerId();
            BigDecimal amount = transaction.getAmount();

            customerTotals.put(
                    customerId,
                    customerTotals.getOrDefault(
                            customerId, BigDecimal.ZERO
                    ).add(amount)
            );
        }

        // Step 3: Convert Map entries into a List
        List<Map.Entry<String, BigDecimal>> result =
                new ArrayList<>(customerTotals.entrySet());

        // Step 4: Sort by total amount (highest first)
        result.sort(
                Map.Entry.<String, BigDecimal>comparingByValue()
                        .reversed()
                        .thenComparing(Map.Entry.comparingByKey())
        );

        // Step 5: Return Top 10 customers
        return result.subList(0, Math.min(10, result.size()));
    }

    // Query 2: Monthly Totals using Loops
    public Map<String, BigDecimal> calculateMonthlyTotals(
            List<Transaction> transactions) {

        Map<String, BigDecimal> monthlyTotals = new HashMap<>();

        for (Transaction transaction : transactions) {

            // Extract year and month (e.g. 2024-01)
            String month = transaction.getDate()
                    .toString()
                    .substring(0, 7);

            BigDecimal amount = transaction.getAmount();

            // Add transaction amount to monthly total
            monthlyTotals.put(
                    month,
                    monthlyTotals.getOrDefault(month, BigDecimal.ZERO)
                            .add(amount)
            );
        }

        return monthlyTotals;
    }



    // Query 3: Find Duplicate Transactions using Loops
    public List<Transaction> findDuplicateTransactions(
            List<Transaction> transactions) {

        // Store records that have already appeared
        Set<DuplicateKey> seen = new HashSet<>();

        // Store duplicate records
        Map<DuplicateKey, Transaction> duplicates = new HashMap<>();

        for (Transaction transaction : transactions) {

            DuplicateKey key = new DuplicateKey(
                    transaction.getTransactionId(),
                    transaction.getCustomerId(),
                    transaction.getAmount(),
                    transaction.getDate(),
                    transaction.getCategory()
            );

            // If record already exists, it is a duplicate
            if (!seen.add(key)) {
                duplicates.putIfAbsent(key, transaction);
            }
        }

        // Return duplicates in transaction ID order
        return duplicates.values()
                .stream()
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




    // Query 4: Anomaly Detection using Loops
    public List<Transaction> findAnomalies(
            List<Transaction> transactions) {

        List<Transaction> anomalies = new ArrayList<>();

        // Define anomaly threshold
        BigDecimal threshold = new BigDecimal("200000");

        // Check every transaction
        for (Transaction transaction : transactions) {

            if (transaction.getAmount().compareTo(threshold) > 0) {

                anomalies.add(transaction);
            }
        }

        return anomalies;
    }

}
