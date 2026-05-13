# 📚 Book Management REST API

A production-ready **Book Management REST API** built with **Spring Boot 3.5.14**, **Spring Data JPA**, **MySQL**, **Lombok**, and **Bean Validation**. Designed with clean layered architecture and real-world enterprise backend engineering practices.

---

## 🧑‍💻 Author

**Parikhsit** — `com.parikhsit`  
Built with Java 21 · Spring Boot 3.5.14 · Maven

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.14 |
| ORM | Spring Data JPA + Hibernate |
| Database | MySQL 8.x |
| Validation | Jakarta Bean Validation |
| Boilerplate reduction | Lombok |
| Build tool | Maven |
| API testing | Postman |

---

## 📁 Project Structure

```
src/main/java/com/parikhsit/bookmanagement/
│
├── BookManagementApplication.java        ← Spring Boot entry point
│
├── entity/
│   └── Book.java                         ← JPA entity → maps to `books` table
│
├── dto/
│   ├── BookRequestDTO.java               ← Inbound DTO with Bean Validation
│   └── BookResponseDTO.java              ← Outbound DTO (response shape)
│
├── repository/
│   └── BookRepository.java               ← JpaRepository + custom search queries
│
├── service/
│   ├── BookService.java                  ← Service interface (contract)
│   └── impl/
│       └── BookServiceImpl.java          ← Business logic implementation
│
├── controller/
│   └── BookController.java               ← REST endpoints (@RestController)
│
└── exception/
    ├── ResourceNotFoundException.java    ← Custom 404 exception
    ├── DuplicateResourceException.java   ← Custom 409 exception
    └── GlobalExceptionHandler.java       ← Centralized error handling (@RestControllerAdvice)

src/main/resources/
└── application.properties               ← Server, DB, JPA, logging config
```

---

## 🗄️ Database Schema

Table name: `books`

| Column | Type | Constraint |
|---|---|---|
| id | BIGINT | Primary Key, Auto Increment |
| title | VARCHAR(255) | NOT NULL |
| author | VARCHAR(150) | NOT NULL |
| isbn | VARCHAR(17) | NOT NULL, UNIQUE |
| published_year | INT | NOT NULL |
| available | BIT(1) | NOT NULL, Default: 1 |

> The table is **auto-created by Hibernate** on first run (`ddl-auto=update`). You only need to create the database manually.

---

## 📡 API Endpoints

Base URL: `http://localhost:8080/api/v1`

| Method | Endpoint | Description | Status Codes |
|---|---|---|---|
| `GET` | `/books` | Get all books | 200 |
| `GET` | `/books/{id}` | Get book by ID | 200, 404 |
| `POST` | `/books` | Create a new book | 201, 400, 409 |
| `PUT` | `/books/{id}` | Update a book (full replace) | 200, 400, 404, 409 |
| `DELETE` | `/books/{id}` | Delete a book | 204, 404 |
| `GET` | `/books/search` | Search by author and/or title | 200 |

### Search Query Parameters

```
GET /books/search?author=martin           → partial, case-insensitive author search
GET /books/search?title=clean             → partial, case-insensitive title search
GET /books/search?author=martin&title=code → AND search (both must match)
GET /books/search                         → returns all books
```

---

## 📦 Request & Response Examples

### Create a book — `POST /books`

**Request body:**
```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0-13-235088-4",
  "publishedYear": 2008,
  "available": true
}
```

**Response — 201 Created:**
```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0-13-235088-4",
  "publishedYear": 2008,
  "available": true
}
```

### Validation error — `POST /books` with empty body

**Response — 400 Bad Request:**
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed. Check 'errors' for details.",
  "errors": {
    "title": "Title is required and must not be blank",
    "author": "Author name is required and must not be blank",
    "isbn": "ISBN is required and must not be blank",
    "publishedYear": "Published year is required",
    "available": "Availability status is required (true or false)"
  }
}
```

### Not found — `GET /books/999`

**Response — 404 Not Found:**
```json
{
  "timestamp": "2024-01-15T10:31:00",
  "status": 404,
  "error": "Not Found",
  "message": "Book not found with id: '999'"
}
```

### Duplicate ISBN — `POST /books`

**Response — 409 Conflict:**
```json
{
  "timestamp": "2024-01-15T10:32:00",
  "status": 409,
  "error": "Conflict",
  "message": "Book already exists with isbn: '978-0-13-235088-4'"
}
```

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- MySQL 8.x
- Postman (for API testing)
- Eclipse IDE (with Lombok plugin installed)

### Step 1 — Create the MySQL database

```sql
CREATE DATABASE IF NOT EXISTS book_management_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

### Step 2 — Configure database credentials

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 3 — Build and run

```bash
# Using Maven
mvn clean install
mvn spring-boot:run
```

Or in Eclipse: right-click `BookManagementApplication.java` → **Run As → Spring Boot App**

### Step 4 — Verify it's running

```
Console output:
==========================================
  Book Management API is up and running!
  Base URL : http://localhost:8080/api/v1
==========================================
```

---

## ✅ Bean Validation Rules

| Field | Rules |
|---|---|
| `title` | Not blank, 1–255 characters |
| `author` | Not blank, 2–150 characters |
| `isbn` | Not blank, valid ISBN-10 or ISBN-13 format |
| `publishedYear` | Not null, between 1000 and 2100 |
| `available` | Not null, true or false |

---

## 🏛️ Architecture Overview

```
HTTP Request
     ↓
BookController          ← Handles routing, @Valid, HTTP status codes
     ↓
BookService             ← Interface for loose coupling
     ↓
BookServiceImpl         ← Business logic, @Transactional
     ↓
BookRepository          ← JPA queries, Spring Data
     ↓
MySQL Database
```

### Key Design Decisions

| Decision | Reason |
|---|---|
| Constructor injection | Immutable deps, testable without Spring context |
| Service interface + impl | Loose coupling, Open/Closed Principle, mockable |
| Separate Request/Response DTOs | Decouples API from DB schema, hides internals |
| `@Transactional(readOnly=true)` at class level | Performance optimization for reads |
| `@RestControllerAdvice` | Centralized, consistent JSON error responses |
| Custom exceptions | Clear semantics — 404 vs 409 vs 500 |

---

## 🔴 HTTP Status Codes Used

| Code | Meaning | When |
|---|---|---|
| 200 | OK | Successful GET, PUT |
| 201 | Created | Successful POST |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Validation failure, malformed JSON |
| 404 | Not Found | Book ID doesn't exist |
| 409 | Conflict | Duplicate ISBN |
| 500 | Internal Server Error | Unexpected exceptions |

---

## 🔧 Configuration Reference

Key settings in `application.properties`:

```properties
# Server
server.port=8080
server.servlet.context-path=/api/v1

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/book_management_db
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Jackson
spring.jackson.serialization.indent-output=true
spring.jackson.default-property-inclusion=non_null
```

---

## 📬 Postman Testing

Import the included `BookManagementAPI.postman_collection.json` into Postman.

**Recommended test order:**
1. Run all requests in **Seed Data** folder to populate the database
2. Test **GET /books** — verify all books are returned
3. Test **GET /books/{id}** — valid ID and invalid ID (999)
4. Test **PUT /books/{id}** — update a book's fields
5. Test **DELETE /books/{id}** — delete then try again to verify 404
6. Test **Search** — by author, by title, by both, by neither
7. Test **Validation errors** — empty body, invalid year, blank title
8. Test **Duplicate ISBN** — POST same ISBN twice

---

## 📋 Dependencies (pom.xml)

```xml
spring-boot-starter-web         <!-- REST API + Tomcat -->
spring-boot-starter-data-jpa    <!-- ORM + Hibernate -->
spring-boot-starter-validation  <!-- Bean Validation -->
mysql-connector-j               <!-- MySQL JDBC driver -->
lombok                          <!-- Boilerplate reduction -->
spring-boot-devtools            <!-- Auto-restart on save -->
```

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
