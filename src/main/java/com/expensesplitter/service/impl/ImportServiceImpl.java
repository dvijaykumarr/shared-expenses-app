package com.expensesplitter.service.impl;

import com.expensesplitter.enums.ImportStatus;
import com.expensesplitter.response.ImportRowResult;
import com.expensesplitter.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    @Override
    public List<ImportRowResult> importCSV(
            MultipartFile file
    ) {

        List<ImportRowResult> results =
                new ArrayList<>();

        Set<String> duplicateTracker =
                new HashSet<>();

        try {

            Reader reader =
                    new InputStreamReader(
                            file.getInputStream()
                    );

            CSVParser csvParser =
                    new CSVParser(
                            reader,
                            CSVFormat.DEFAULT
                                    .withFirstRecordAsHeader()
                                    .withIgnoreHeaderCase()
                                    .withTrim()
                    );

            System.out.println(
                    csvParser.getHeaderMap().keySet()
            );

            int rowNumber = 1;

            for (CSVRecord record : csvParser) {

                String description =
                        record.get("description");

                String lowerDescription =
                        description.toLowerCase();

                if (lowerDescription.contains("paid back") ||
                        lowerDescription.contains("settled") ||
                        lowerDescription.contains("reimbursement")) {

                    results.add(
                            ImportRowResult.builder()
                                    .rowNumber(rowNumber)
                                    .status(ImportStatus.NEEDS_REVIEW)
                                    .message("Possible settlement transaction detected")
                                    .build()
                    );

                    rowNumber++;

                    continue;
                }

                String amount =
                        record.get("amount");

                String paidBy =
                        record.get("paid_by");

                if (paidBy == null ||
                        paidBy.isBlank()) {

                    results.add(
                            ImportRowResult.builder()
                                    .rowNumber(rowNumber)
                                    .status(ImportStatus.ERROR)
                                    .message("Missing paid_by value")
                                    .build()
                    );

                    rowNumber++;

                    continue;
                }

                String date =
                        record.get("date");

                String splitType =
                        record.get("split_type");

                String splitDetails =
                        record.get("split_details");

                String splitWith =
                        record.get("split_with");

                if (splitWith == null ||
                        splitWith.isBlank()) {

                    results.add(
                            ImportRowResult.builder()
                                    .rowNumber(rowNumber)
                                    .status(ImportStatus.ERROR)
                                    .message("Missing split participants")
                                    .build()
                    );

                    rowNumber++;

                    continue;
                }

                if ((splitType.equalsIgnoreCase("EXACT")
                        || splitType.equalsIgnoreCase("PERCENTAGE"))
                        &&
                        (splitDetails == null ||
                                splitDetails.isBlank())) {

                    results.add(
                            ImportRowResult.builder()
                                    .rowNumber(rowNumber)
                                    .status(ImportStatus.ERROR)
                                    .message("Missing split details")
                                    .build()
                    );

                    rowNumber++;

                    continue;
                }

                String currency =
                        record.get("currency");

                String expenseDate =
                        record.get("date");

                try {

                    DateTimeFormatter formatter =
                            DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    LocalDate.parse(expenseDate, formatter);

                } catch (DateTimeParseException e) {

                    results.add(
                            ImportRowResult.builder()
                                    .rowNumber(rowNumber)
                                    .status(ImportStatus.WARNING)
                                    .message("Unrecognized date format")
                                    .build()
                    );

                    rowNumber++;

                    continue;
                }

                if (currency == null ||
                        currency.isBlank()) {

                    results.add(
                            ImportRowResult.builder()
                                    .rowNumber(rowNumber)
                                    .status(ImportStatus.ERROR)
                                    .message("Missing currency")
                                    .build()
                    );

                    rowNumber++;

                    continue;
                }

                if (!currency.equalsIgnoreCase("INR")
                        && !currency.equalsIgnoreCase("USD")
                        && !currency.equalsIgnoreCase("EUR")) {

                    results.add(
                            ImportRowResult.builder()
                                    .rowNumber(rowNumber)
                                    .status(ImportStatus.ERROR)
                                    .message("Unsupported currency: " + currency)
                                    .build()
                    );

                    rowNumber++;

                    continue;
                }

                if (!splitType.equalsIgnoreCase("EQUAL")
                        && !splitType.equalsIgnoreCase("EXACT")
                        && !splitType.equalsIgnoreCase("PERCENTAGE")) {

                    results.add(
                            ImportRowResult.builder()
                                    .rowNumber(rowNumber)
                                    .status(ImportStatus.ERROR)
                                    .message("Unsupported split type: " + splitType)
                                    .build()
                    );

                    rowNumber++;

                    continue;
                }

                if (description == null ||
                        description.isBlank()) {

                    results.add(
                            ImportRowResult.builder()
                                    .rowNumber(rowNumber)
                                    .status(ImportStatus.ERROR)
                                    .message("Missing description")
                                    .build()
                    );

                    rowNumber++;

                    continue;
                }

                if (amount == null ||
                        amount.isBlank()) {

                    results.add(
                            ImportRowResult.builder()
                                    .rowNumber(rowNumber)
                                    .status(ImportStatus.ERROR)
                                    .message("Missing amount")
                                    .build()
                    );

                    rowNumber++;

                    continue;
                }

                try {

                    amount = amount.replace(",", "");

                    BigDecimal parsedAmount =
                            new BigDecimal(amount);

                    if (parsedAmount.scale() > 2) {

                        results.add(
                                ImportRowResult.builder()
                                        .rowNumber(rowNumber)
                                        .status(ImportStatus.WARNING)
                                        .message("Amount has more than 2 decimal places")
                                        .build()
                        );

                        rowNumber++;

                        continue;
                    }

                    if (parsedAmount.compareTo(BigDecimal.ZERO) == 0) {

                        results.add(
                                ImportRowResult.builder()
                                        .rowNumber(rowNumber)
                                        .status(ImportStatus.WARNING)
                                        .message("Zero amount expense")
                                        .build()
                        );

                        rowNumber++;

                        continue;
                    }

                    if (parsedAmount.compareTo(BigDecimal.ZERO) < 0) {

                        results.add(
                                ImportRowResult.builder()
                                        .rowNumber(rowNumber)
                                        .status(ImportStatus.WARNING)
                                        .message("Negative amount detected (possible refund)")
                                        .build()
                        );

                        rowNumber++;

                        continue;
                    }

                } catch (NumberFormatException e) {

                    results.add(
                            ImportRowResult.builder()
                                    .rowNumber(rowNumber)
                                    .status(ImportStatus.ERROR)
                                    .message("Invalid amount format")
                                    .build()
                    );

                    rowNumber++;

                    continue;
                }

                String duplicateKey =
                        description.toLowerCase().trim()
                                + amount.trim()
                                + paidBy.toLowerCase().trim()
                                + date.trim();

                if (duplicateTracker.contains(duplicateKey)) {

                    results.add(
                            ImportRowResult.builder()
                                    .rowNumber(rowNumber)
                                    .status(ImportStatus.NEEDS_REVIEW)
                                    .message("Possible duplicate expense detected")
                                    .build()
                    );

                    rowNumber++;

                    continue;
                }

                duplicateTracker.add(duplicateKey);

                results.add(
                        ImportRowResult.builder()
                                .rowNumber(rowNumber)
                                .status(ImportStatus.VALID)
                                .message("Row is valid")
                                .build()
                );

                rowNumber++;
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to process CSV file"
            );
        }

        return results;
    }
}