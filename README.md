# CrunchyCode Task 1 — Collections and Streams

Java 21 + Maven starter project.

## Generate the reproducible 200,000-row dataset

Run `com.crunchycode.util.DatasetGenerator` from IntelliJ, or run:

```bash
mvn clean compile
java -cp target/classes com.crunchycode.util.DatasetGenerator
```

The default output is `src/main/resources/transactions.csv`.
The generator uses a fixed random seed (`20261110L`), so the same version
of the generator creates the same dataset on each run.

To generate a smaller 20,000-row dataset:

```bash
java -cp target/classes com.crunchycode.util.DatasetGenerator 20000 src/main/resources/transactions-20000.csv
```

## Read the generated CSV

Run `com.crunchycode.Main` from IntelliJ, or:

```bash
java -cp target/classes com.crunchycode.Main
```

The CSV schema is:

`transaction_id,customer_id,amount,date,category`

The generated file intentionally contains repeated complete rows and unusually
large amounts so duplicate-detection and anomaly-analysis logic can be tested.
The anomaly threshold must be defined from the assignment brief, not assumed
from the generator.

## Build

```bash
mvn clean compile
```

## Important
The screenshots provided so far show four query categories. The exact Q5 and Q6
requirements still need to be confirmed from the full CrunchyCode task brief.
