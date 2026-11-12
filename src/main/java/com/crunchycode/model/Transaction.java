package com.crunchycode.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private final String transactionId;
    private final String customerId;
    private final BigDecimal amount;
    private final LocalDate date;
    private final String category;

    public Transaction(String transactionId, String customerId,
                       BigDecimal amount, LocalDate date, String category) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public String getTransactionId() { return transactionId; }
    public String getCustomerId() { return customerId; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getCategory() { return category; }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", category='" + category + '\'' +
                '}';
    }
}
