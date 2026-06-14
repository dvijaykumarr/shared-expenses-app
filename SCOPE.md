# SCOPE.md

# Expense Splitter — CSV Anomaly Scope and Data Handling Log

## Project Scope

This backend system was designed to manage shared group expenses and support intelligent CSV-based financial expense imports with anomaly detection and validation workflows.

The project focuses on:

* Expense management
* Group member management
* Balance calculation
* CSV ingestion workflows
* Financial anomaly detection
* Data integrity validation
* Import reporting

---

# CSV Import Scope

The import engine processes uploaded CSV expense sheets and performs:

1. CSV parsing
2. Data validation
3. Financial anomaly detection
4. User reconciliation
5. Expense persistence
6. Import report generation

The importer follows a staged validation workflow:

CSV → Validation → Anomaly Detection → Import Decision → Database Persistence

---

# Anomalies Identified and Handling Strategy

## 1. Missing Amount

### Problem

Some rows contained empty amount fields.

### Handling

Rows were marked as:

```text id="7vw0wd"
ERROR
```

and skipped from import.

---

## 2. Invalid Amount Format

### Problem

Some rows contained malformed numeric values.

Examples:

```text id="0i0lmu"
abc
12x
```

### Handling

Rows were classified as:

```text id="7ck7i0"
ERROR
```

and excluded from persistence.

---

## 3. Negative Amount Detection

### Problem

Negative expense amounts may represent refunds or reversals.

### Handling

Rows were marked as:

```text id="e8n72x"
WARNING
```

with message:

```text id="1bdr7f"
Negative amount detected (possible refund)
```

---

## 4. Zero Amount Expense

### Problem

Expenses with amount `0` may indicate invalid or incomplete records.

### Handling

Rows were classified as:

```text id="39j1v9"
WARNING
```

---

## 5. Unsupported Currency

### Problem

Some CSV rows contained unsupported currencies.

Supported currencies:

* INR
* USD
* EUR

### Handling

Unsupported currencies were marked as:

```text id="g2q9gz"
ERROR
```

and skipped.

---

## 6. Missing Currency

### Problem

Some rows did not contain currency values.

### Handling

Rows were rejected with:

```text id="f5npsn"
ERROR
```

---

## 7. Invalid Date Format

### Problem

CSV rows contained inconsistent date formats.

### Handling

Rows were classified as:

```text id="h1c1fr"
WARNING
```

with import skipped until corrected.

---

## 8. Missing Payer

### Problem

Some rows did not contain `paid_by` values.

### Handling

Rows were marked as:

```text id="ytq0zy"
ERROR
```

because payer information is mandatory for financial consistency.

---

## 9. Unknown Users During Import

### Problem

CSV rows referenced users not present in the database.

### Handling

The system automatically creates lightweight placeholder users during import.

This prevents complete import failure and supports later reconciliation.

---

## 10. Unsupported Split Types

### Problem

Some rows contained unsupported split types.

Supported split types:

* EQUAL
* EXACT
* PERCENTAGE

### Handling

Unsupported split types were classified as:

```text id="86c6x6"
ERROR
```

and skipped.

---

## 11. Missing Split Details

### Problem

Rows using `EXACT` or `PERCENTAGE` splits sometimes lacked participant share details.

### Handling

Rows were rejected with:

```text id="9v0kk7"
ERROR
```

---

## 12. Missing Participants

### Problem

Some expenses lacked participant information.

### Handling

Rows were classified as:

```text id="hjlwmk"
ERROR
```

because participant allocation is required for balance calculation.

---

## 13. Decimal Precision Validation

### Problem

Some monetary values contained excessive decimal precision.

Example:

```text id="8q4nq0"
899.999
```

### Handling

Rows were marked as:

```text id="c9c5dy"
WARNING
```

---

## 14. Duplicate Expense Heuristics

### Problem

Potential duplicate expenses may exist in uploaded CSV files.

### Handling

The system flags suspicious duplicates for review using heuristic checks based on:

* amount
* payer
* description
* date proximity

---

## 15. Settlement Transaction Detection

### Problem

Certain rows appeared to represent settlement or repayment transactions instead of actual expenses.

### Handling

Such rows were identified and classified separately during anomaly analysis.

---

# Import Classification Strategy

Each row is classified into one of four categories:

| Status       | Meaning                                      |
| ------------ | -------------------------------------------- |
| VALID        | Safe to import                               |
| WARNING      | Potential issue but not structurally invalid |
| ERROR        | Invalid data, import blocked                 |
| NEEDS_REVIEW | Requires manual verification                 |

---

# Database Schema Overview

## Main Entities

### User

Stores application users.

Fields include:

* id
* name
* email
* password

---

### Group

Represents expense groups.

Fields include:

* id
* name
* createdAt

---

### GroupMember

Tracks membership relationships.

Fields include:

* id
* group_id
* user_id
* joined_at
* left_at
* active

---

### Expense

Stores expense records.

Fields include:

* id
* title
* description
* amount
* normalizedAmount
* currency
* exchangeRate
* splitType
* expenseDate
* paidBy
* group_id

---

### ExpenseParticipant

Stores participant share allocations.

Fields include:

* id
* expense_id
* user_id
* shareAmount
* percentage

---

# Scope Limitations

The current implementation focuses primarily on backend workflow integrity and anomaly-aware CSV ingestion.

The following are outside current scope:

* Frontend UI
* OAuth authentication
* Advanced fraud detection
* Async batch import processing
* Distributed processing
* Machine learning anomaly classification

---

# Conclusion

The project prioritizes:

* Financial data integrity
* Structured import workflows
* Anomaly visibility
* Backend architecture quality
* Real-world CSV ingestion handling

The importer was intentionally designed to simulate realistic financial ingestion pipelines rather than simple CRUD-based uploads.
