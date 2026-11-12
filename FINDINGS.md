# CrunchyCode Task 1 – Collections and Streams

## 1. Project Overview

The objective of this project is to process transaction records using Java 21, Collections Framework and Streams API.

A reproducible transaction dataset was generated containing 20,000 and 200,000 records.

## 2. Technologies Used

- Java 21
- Maven
- Collections Framework
- Java Streams API
- IntelliJ IDEA

## 3. Implementation

The project compares two approaches:

1. Traditional loops
2. Java Streams API

Both approaches are implemented to solve the same four transaction queries.

## 4. Collection Selection Justification

**Q1 – Top Customers (HashMap)**

HashMap stores customer IDs as keys and their total transaction amounts as values. It provides efficient lookup and updating of customer totals.

**Q2 – Monthly Totals (TreeMap)**

TreeMap maintains monthly totals in sorted order, making chronological output easier.

**Q3 – Duplicate Transactions (HashSet + ArrayList)**

HashSet identifies previously encountered transaction IDs, while ArrayList stores duplicate records.

**Q4 – Anomaly Detection (ArrayList)**

ArrayList stores transactions that satisfy the anomaly condition and allows easy iteration.

## 5. Performance Benchmark Results

### Dataset: 20,000 Records

| Query | Loops (ms) | Streams (ms) |
|---|---:|---:|
| Top Customers | 2.477 | 1.330 |
| Monthly Totals | 2.613 | 3.624 |
| Duplicates | 2.851 | 3.086 |
| Anomalies | 0.629 | 0.290 |

### Dataset: 200,000 Records

| Query | Loops (ms) | Streams (ms) |
|---|---:|---:|
| Top Customers | 12.059 | 11.018 |
| Monthly Totals | 21.285 | 22.833 |
| Duplicates | 18.683 | 61.045 |
| Anomalies | 5.645 | 4.403 |

## 6. Result Verification

All four implemented queries returned matching results between loops and streams.

- Query 1: true
- Query 2: true
- Query 3: true
- Query 4: true

## 7. Findings

- Streams performed better for top customer analysis in the 200K run.
- Loops performed better for monthly totals.
- Loops performed significantly better for duplicate detection.
- Streams performed better for anomaly detection.
- The results show that Streams are not always faster than traditional loops.
- Performance depends on the operation and implementation.

These observations are based on the recorded benchmark runs and may vary with JVM warm-up and system load.

## 8. Conclusion

This project helped demonstrate the practical use of Java Collections Framework and Streams API.

By processing 20,000 and 200,000 transaction records, we compared loops and streams and observed their performance differences.

The project shows that choosing the right collection and measuring actual execution time are important for efficient Java programming.