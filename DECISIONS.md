# DECISIONS.md

# Engineering Decision Log

This document records major architectural and implementation decisions made during the development of the Expense Splitter backend system.

The purpose of this log is to explain:

* Significant technical decisions
* Alternative approaches considered
* Final choices made
* Engineering reasoning behind those choices

---

# 1. Layered Backend Architecture

## Decision

Use a layered backend architecture:

controller → service → repository → database

---

## Alternatives Considered

### Option 1

Direct controller-to-repository interaction

### Option 2

Layered architecture with service abstraction

---

## Chosen Approach

Layered architecture with service layer separation.

---

## Reason

This approach improves:

* Code maintainability
* Separation of concerns
* Scalability
* Testing capability
* Business logic organization

It also follows standard Spring Boot enterprise practices.

---

# 2. DTO and Mapper Usage

## Decision

Use DTOs and mapper classes instead of directly exposing entities.

---

## Alternatives Considered

### Option 1

Expose entity classes directly in APIs

### Option 2

Use DTO/request/response abstraction

---

## Chosen Approach

DTO + Mapper based architecture.

---

## Reason

This prevents:

* Entity leakage
* Tight API coupling
* Uncontrolled field exposure

It also improves API stability and maintainability.

---

# 3. Password Encryption

## Decision

Use BCrypt password hashing.

---

## Alternatives Considered

### Option 1

Store plain text passwords

### Option 2

Use BCrypt hashing

---

## Chosen Approach

BCrypt hashing.

---

## Reason

BCrypt is a standard industry approach for password security and protects against plain text credential exposure.

---

# 4. JWT Authentication Removal

## Decision

JWT authentication implementation was initially attempted but later removed from the final implementation.

---

## Alternatives Considered

### Option 1

Continue debugging JWT configuration

### Option 2

Prioritize core assignment backend workflows

---

## Chosen Approach

Focus on assignment-critical backend functionality instead of spending excessive time on framework-level JWT debugging.

---

## Reason

The assignment emphasized:

* import workflows
* anomaly detection
* backend logic
* CSV ingestion

The core engineering value of the project was in financial workflow handling rather than authentication complexity.

---

# 5. Intelligent CSV Validation Workflow

## Decision

Implement staged CSV validation before database persistence.

---

## Alternatives Considered

### Option 1

Directly import all CSV rows

### Option 2

Validate rows before import

---

## Chosen Approach

Validation-first import pipeline.

---

## Reason

This protects:

* data integrity
* balance calculations
* financial consistency

The importer was designed to simulate real-world ingestion systems rather than simple uploads.

---

# 6. Import Classification Strategy

## Decision

Classify rows into:

* VALID
* WARNING
* ERROR
* NEEDS_REVIEW

---

## Alternatives Considered

### Option 1

Binary valid/invalid classification

### Option 2

Multi-level anomaly classification

---

## Chosen Approach

Multi-level classification system.

---

## Reason

Real-world financial ingestion systems frequently require:

* partial imports
* manual reconciliation
* anomaly visibility
* operational review workflows

This approach provides more realistic import handling.

---

# 7. Auto User Creation During Import

## Decision

Automatically create placeholder users for unknown imported participants.

---

## Alternatives Considered

### Option 1

Reject rows with unknown users

### Option 2

Auto-create lightweight imported users

---

## Chosen Approach

Auto-create placeholder users.

---

## Reason

This prevents unnecessary import failures and simulates real-world onboarding workflows where imported external users may not yet exist in the system.

---

# 8. Financial Validation Rules

## Decision

Implement validation rules for:

* missing currency
* invalid amounts
* negative amounts
* zero amounts
* unsupported split types
* invalid dates
* missing participants

---

## Alternatives Considered

### Option 1

Minimal validation

### Option 2

Strict anomaly-aware financial validation

---

## Chosen Approach

Anomaly-aware validation workflow.

---

## Reason

Financial systems require stronger data integrity guarantees compared to standard CRUD applications.

---

# 9. Split Type Restriction During Import

## Decision

Initial import workflow primarily supports EQUAL split persistence.

---

## Alternatives Considered

### Option 1

Implement all split algorithms immediately

### Option 2

Prioritize stable equal-split workflow first

---

## Chosen Approach

Implement stable equal-split import handling first.

---

## Reason

This reduced complexity during core ingestion implementation while still demonstrating import workflow capabilities.

The architecture remains extensible for future split strategies.

---

# 10. Database Design Choice

## Decision

Separate Expense and ExpenseParticipant entities.

---

## Alternatives Considered

### Option 1

Store participant data directly in Expense table

### Option 2

Use normalized participant relationship table

---

## Chosen Approach

Normalized participant entity design.

---

## Reason

This improves:

* normalization
* scalability
* participant-level tracking
* future split extensions

It also aligns with relational database best practices.

---

# 11. Commit Strategy

## Decision

Use feature-based meaningful commits.

---

## Alternatives Considered

### Option 1

Single bulk commit

### Option 2

Incremental feature-based commits

---

## Chosen Approach

Incremental meaningful commit history.

---

## Reason

This improves:

* development traceability
* reviewer understanding
* engineering professionalism
* debugging visibility

---

# 12. AI-Assisted Development

## Decision

Use AI assistance during development while manually validating all implementation logic.

---

## Alternatives Considered

### Option 1

Avoid AI completely

### Option 2

Use AI as engineering assistance

---

## Chosen Approach

AI-assisted iterative development.

---

## Reason

AI was used for:

* architecture brainstorming
* debugging support
* implementation refinement
* documentation assistance

All generated outputs were manually reviewed, tested, corrected, and integrated into the final system.

---

# Conclusion

The project decisions prioritized:

* backend architecture quality
* financial data integrity
* anomaly visibility
* realistic ingestion workflows
* maintainability
* extensibility

The implementation intentionally focused on simulating practical backend engineering workflows rather than only CRUD-based functionality.
