package com.crunchycode.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Random;

/**
 * Creates a reproducible transaction dataset.
 * The fixed seed means the same program creates the same records each run.
 */
public class DatasetGenerator {

    private static final int DEFAULT_RECORD_COUNT = 200_000;
    private static final long RANDOM_SEED = 20261110L;
    private static final String[] CATEGORIES = {
            "BOOKS", "GROCERY", "ELECTRONICS",
            "CLOTHING", "TRAVEL", "FOOD"
    };

    public static void main(String[] args) throws IOException {
        int recordCount = args.length >= 1
                ? Integer.parseInt(args[0])
                : DEFAULT_RECORD_COUNT;

        Path outputPath = args.length >= 2
                ? Path.of(args[1])
                : Path.of("src/main/resources/transactions.csv");

        generate(recordCount, outputPath);
        System.out.println("Generated " + recordCount + " transaction rows.");
        System.out.println("File: " + outputPath.toAbsolutePath());
        System.out.println("Random seed: " + RANDOM_SEED);
    }

    public static void generate(int recordCount, Path outputPath)
            throws IOException {

        if (recordCount < 1) {
            throw new IllegalArgumentException("Record count must be positive.");
        }

        Path parent = outputPath.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        Random random = new Random(RANDOM_SEED);
        LocalDate firstDate = LocalDate.of(2024, 1, 1);
        String previousRow = null;

        try (BufferedWriter writer = Files.newBufferedWriter(
                outputPath, StandardCharsets.UTF_8)) {

            writer.write("transaction_id,customer_id,amount,date,category");
            writer.newLine();

            for (int i = 1; i <= recordCount; i++) {

                // Every 1,000th row repeats the previous complete row.
                // This deliberately creates exact duplicate records.
                if (i % 1_000 == 0 && previousRow != null) {
                    writer.write(previousRow);
                    writer.newLine();
                    continue;
                }

                String transactionId = String.format("TXN%06d", i);
                String customerId = String.format(
                        "CUST%04d", random.nextInt(500) + 1);

                // Most amounts are ordinary positive values.
                // Every 997th record is deliberately high to test anomaly logic.
                BigDecimal amount;
                if (i % 997 == 0) {
                    amount = BigDecimal.valueOf(
                            250_000 + random.nextInt(750_001))
                            .setScale(2, RoundingMode.HALF_UP);
                } else {
                    amount = BigDecimal.valueOf(
                            100 + random.nextInt(99_901))
                            .setScale(2, RoundingMode.HALF_UP);
                }

                LocalDate date = firstDate.plusDays(random.nextInt(730));
                String category = CATEGORIES[random.nextInt(CATEGORIES.length)];

                String row = transactionId + "," + customerId + "," +
                        amount.toPlainString() + "," + date + "," + category;

                writer.write(row);
                writer.newLine();
                previousRow = row;
            }
        }
    }
}
