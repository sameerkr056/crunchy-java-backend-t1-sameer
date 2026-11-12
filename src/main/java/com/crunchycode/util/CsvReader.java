package com.crunchycode.util;

import com.crunchycode.model.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads transaction CSV files with this header:
 * transaction_id,customer_id,amount,date,category
 *
 * This project-generated CSV keeps values simple, but the parser also supports
 * quoted CSV fields and commas inside quoted values.
 */
public class CsvReader {

    private static final String EXPECTED_HEADER =
            "transaction_id,customer_id,amount,date,category";

    public List<Transaction> read(Path csvPath) throws IOException {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader =
                     Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {

            String header = reader.readLine();

            if (header == null) {
                throw new IllegalArgumentException("CSV file is empty: " + csvPath);
            }

            // Remove a UTF-8 BOM if the file was saved with one.
            header = header.replace("\uFEFF", "").trim();

            if (!EXPECTED_HEADER.equalsIgnoreCase(header)) {
                throw new IllegalArgumentException(
                        "Unexpected CSV header. Expected: " + EXPECTED_HEADER +
                        " but found: " + header);
            }

            String line;
            long lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                try {
                    List<String> columns = parseCsvLine(line);

                    if (columns.size() != 5) {
                        throw new IllegalArgumentException(
                                "Expected 5 columns but found " + columns.size());
                    }

                    String transactionId = columns.get(0).trim();
                    String customerId = columns.get(1).trim();
                    BigDecimal amount = new BigDecimal(columns.get(2).trim());
                    LocalDate date = LocalDate.parse(columns.get(3).trim());
                    String category = columns.get(4).trim();

                    if (transactionId.isEmpty() || customerId.isEmpty()
                            || category.isEmpty()) {
                        throw new IllegalArgumentException(
                                "Transaction ID, customer ID and category cannot be blank");
                    }

                    transactions.add(new Transaction(
                            transactionId, customerId, amount, date, category));

                } catch (RuntimeException exception) {
                    throw new IllegalArgumentException(
                            "Invalid data at CSV line " + lineNumber + ": " +
                            exception.getMessage(), exception);
                }
            }
        }

        return transactions;
    }

    /**
     * Splits one CSV row while respecting double-quoted values.
     * A doubled quote inside a quoted value ("") becomes one quote.
     */
    private List<String> parseCsvLine(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char current = line.charAt(i);

            if (current == '"') {
                if (insideQuotes && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {
                    value.append('"');
                    i++;
                } else {
                    insideQuotes = !insideQuotes;
                }
            } else if (current == ',' && !insideQuotes) {
                columns.add(value.toString());
                value.setLength(0);
            } else {
                value.append(current);
            }
        }

        if (insideQuotes) {
            throw new IllegalArgumentException("Unclosed quoted CSV value");
        }

        columns.add(value.toString());
        return columns;
    }
}
