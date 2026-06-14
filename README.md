# Expense Splitter Backend System

## Project Overview

Expense Splitter is a backend system designed to manage shared group expenses, member balances, and intelligent CSV-based expense imports with anomaly detection.

The system supports:

* Group-based expense management
* Expense participant tracking
* Balance simplification
* Intelligent CSV ingestion
* Automated anomaly detection
* Financial validation workflows
* Auto onboarding of imported users

This project was developed as part of a backend engineering assignment focused on real-world financial data handling and import workflow design.

---

# Tech Stack

| Technology         | Purpose               |
| ------------------ | --------------------- |
| Java 17            | Core language         |
| Spring Boot        | Backend framework     |
| Spring Data JPA    | ORM and persistence   |
| Hibernate          | Database interaction  |
| MySQL              | Relational database   |
| Maven              | Dependency management |
| Apache Commons CSV | CSV parsing           |
| Lombok             | Boilerplate reduction |
| BCrypt             | Password hashing      |

---

# Features

## Authentication Module

* User registration
* User login
* Password encryption using BCrypt
* DTO + Mapper based architecture

---

## Group Management Module

* Create groups
* Add members to groups
* Remove members from groups
* Retrieve group members
* Membership timeline tracking

---

## Expense Management Module

* Create expenses
* Split expenses among participants
* Group balance calculation
* Balance simplification logic

---

## Intelligent CSV Import Engine

The system includes an anomaly-aware CSV import workflow capable of:

### CSV Processing

* Multipart CSV upload
* Row-by-row parsing
* Structured import report generation

### Financial Validations

* Missing amount detection
* Invalid amount format detection
* Decimal precision validation
* Negative/refund transaction detection
* Zero amount detection
* Missing payer detection
* Missing participant detection
* Missing currency validation
* Unsupported currency validation
* Unsupported split type detection
* Missing split details validation
* Invalid date format handling

### Intelligent Anomaly Detection

* Settlement transaction detection
* Duplicate expense heuristics
* Unknown user detection
* Auto onboarding of imported users
* Classification into:

  * VALID
  * WARNING
  * ERROR
  * NEEDS_REVIEW

### Persistence Workflow

Only validated rows are imported into the database:

CSV → Validation → Anomaly Detection → User Resolution → Expense Creation → Participant Creation → Import Report

---

# Project Architecture

The project follows a layered backend architecture:

controller
→ service
→ repository
→ database

Additional layers:

* DTO layer
* Mapper layer
* Request/Response models
* Import validation engine

---

# Database Schema Overview

## Main Entities

### User

Stores application users.

### Group

Represents expense groups.

### GroupMember

Tracks group membership and membership timeline.

### Expense

Stores expenses and financial metadata.

### ExpenseParticipant

Stores participant share information.

---

# API Endpoints

## Authentication APIs

| Method | Endpoint           |
| ------ | ------------------ |
| POST   | /api/auth/register |
| POST   | /api/auth/login    |

---

## Group APIs

| Method | Endpoint                               |
| ------ | -------------------------------------- |
| POST   | /api/groups                            |
| POST   | /api/groups/{groupId}/members          |
| DELETE | /api/groups/{groupId}/members/{userId} |
| GET    | /api/groups/{groupId}/members          |

---

## Expense APIs

| Method | Endpoint                                |
| ------ | --------------------------------------- |
| POST   | /api/expenses                           |
| GET    | /api/groups/{groupId}/balances          |
| GET    | /api/groups/{groupId}/simplify-balances |

---

## CSV Import API

| Method | Endpoint        |
| ------ | --------------- |
| POST   | /api/import/csv |

Request Type:
multipart/form-data

Parameter:
file

---

# Setup Instructions

## Clone Repository

```bash
git clone <repository-url>
cd expense-splitter
```

---

## Configure Database

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/expense_splitter
spring.datasource.username=root
spring.datasource.password=your_password
```

---

## Install Dependencies

```bash
mvn clean install
```

---

## Run Application

```bash
mvn spring-boot:run
```

Application runs on:

```text
http://localhost:8080
```

---

# Sample Import Workflow

## Upload CSV

POST `/api/import/csv`

## System Processing

The importer:

1. Parses CSV rows
2. Detects anomalies
3. Validates financial consistency
4. Auto-creates missing users
5. Creates expenses and participants
6. Generates import report

---

# Example Import Report

```json
[
  {
    "rowNumber": 1,
    "status": "VALID",
    "message": "Expense imported successfully"
  },
  {
    "rowNumber": 11,
    "status": "ERROR",
    "message": "Unsupported split type: unequal"
  },
  {
    "rowNumber": 25,
    "status": "WARNING",
    "message": "Negative amount detected (possible refund)"
  }
]
```

---

# AI Usage

AI assistance was used during:

* Architecture brainstorming
* CSV validation workflow design
* Debugging assistance
* Import pipeline refinement
* Documentation drafting

All generated suggestions were manually reviewed, tested, adapted, and integrated into the final implementation.

Detailed AI collaboration notes are documented separately in `AI_USAGE.md`.

---

# Commit History

The repository maintains a structured and meaningful commit history with feature-based commits following industry-style conventions such as:

```text
feat(auth): implement authentication module
feat(group): implement group management workflows
feat(import): add intelligent CSV import engine
```

---

# Future Improvements

Potential future enhancements include:

* JWT authentication
* Swagger/OpenAPI documentation
* Async import processing
* Import history persistence
* Frontend dashboard
* Role-based access control
* Advanced duplicate similarity matching

---

# Author

Vijay Kumar

Backend Engineering Assignment Submission
