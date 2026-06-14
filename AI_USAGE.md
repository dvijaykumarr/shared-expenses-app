# AI_USAGE.md

# AI Usage Report

## Overview

AI assistance was used during the development of the Expense Splitter backend project for:

* architecture brainstorming
* debugging support
* CSV import workflow design
* anomaly validation ideas
* documentation drafting
* code refinement guidance

The AI was used as an engineering assistant rather than a code replacement system.

All generated outputs were:

* manually reviewed
* tested locally
* adapted to project requirements
* corrected when necessary
* integrated incrementally

---

# AI Tools Used

| Tool    | Purpose                                                                                               |
| ------- | ----------------------------------------------------------------------------------------------------- |
| ChatGPT | Architecture guidance, debugging assistance, validation workflow brainstorming, documentation support |

---

# Areas Where AI Was Used

## Backend Architecture

AI assistance was used for:

* layered architecture organization
* DTO/Mapper separation
* service structure guidance
* import workflow planning

---

## CSV Import Engine

AI assistance was used for:

* anomaly validation ideas
* import workflow sequencing
* financial validation rules
* staged import pipeline design

---

## Debugging Assistance

AI was used to help diagnose:

* Spring Security issues
* JWT authentication issues
* Optional<User> handling problems
* CSV parsing issues
* import persistence issues

---

# Key Prompts Used

Examples of prompts used during development:

* "Implement CSV anomaly validation workflow in Spring Boot"
* "Design clean backend architecture for expense splitter"
* "How to validate imported CSV expense records"
* "How to handle unknown users during import"
* "Explain why JWT authentication is failing"
* "Design import classification system with VALID/WARNING/ERROR states"

---

# Cases Where AI Produced Incorrect or Problematic Suggestions

## Case 1 — JWT Authentication Configuration Issues

### AI Suggestion

AI initially suggested a JWT configuration approach that caused repeated:

* 401 Unauthorized
* 403 Forbidden

errors during API testing.

---

### Problem Identified

The authentication filter chain and provider configuration created conflicts within the Spring Security setup.

The implementation complexity was consuming significant development time relative to assignment priorities.

---

### How It Was Caught

Issues were identified during:

* Postman testing
* endpoint validation
* repeated authorization failures

---

### Final Decision

JWT implementation was removed from the final assignment scope.

The focus shifted toward:

* CSV ingestion
* anomaly detection
* backend workflow quality

which were more central to assignment evaluation.

---

# Case 2 — Import Workflow Stopping Prematurely

### AI Suggestion

AI initially suggested date validation logic that used:

```java
continue;
```

inside warning handling.

---

### Problem Identified

This caused all rows to stop processing before database persistence.

No expenses were being imported.

---

### How It Was Caught

Detected by:

* observing empty database tables
* reviewing import responses
* analyzing validation flow behavior

---

### Fix Applied

The validation workflow was redesigned to:

* safely parse dates
* preserve parsed values
* continue valid import execution

This restored successful expense persistence.

---

# Case 3 — Incorrect Handling of Optional<User>

### AI Suggestion

AI-generated repository usage incorrectly assumed:

```java
findByEmail()
```

returned a direct `User` object.

---

### Problem Identified

The actual repository method returned:

```java
Optional<User>
```

causing compilation errors.

---

### How It Was Caught

Detected through compiler errors during implementation.

---

### Fix Applied

The logic was corrected using:

```java
.orElse(null)
```

to safely extract nullable user references.

---

# Case 4 — Initial Import Failure for Unknown Users

### AI Suggestion

AI initially recommended blocking rows when imported users were not found in the database.

---

### Problem Identified

This prevented large portions of CSV data from importing successfully.

---

### How It Was Caught

Observed during CSV testing where most rows became:

```text
NEEDS_REVIEW
```

instead of importing.

---

### Fix Applied

The importer was redesigned to:

* auto-create lightweight placeholder users
* continue import workflow safely
* support later reconciliation

This significantly improved importer robustness.

---

# Engineering Validation Process

Every AI-generated suggestion was validated through:

* local testing
* Postman API testing
* database verification
* compiler checks
* import workflow verification
* iterative debugging

No AI-generated code was accepted blindly.

---

# Final Reflection

AI significantly accelerated:

* brainstorming
* debugging
* documentation
* architecture iteration

However, successful implementation still required:

* manual reasoning
* debugging
* testing
* adaptation
* architectural decisions

The final system reflects a collaborative AI-assisted engineering workflow rather than fully AI-generated implementation.
