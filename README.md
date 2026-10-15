# Inventory Management REST API

Spring Boot REST API for CRUD operations on inventory items, with validation,
API key security, pagination/sorting and unit tests.

**Tech:** Java 17, Spring Boot, Spring Data JPA, MySQL, Spring Security (API key filter),
Bean Validation, JUnit 5 + MockMvc, Maven

## Quick Start

**Requirements:** Java 17 and MySQL 8 (Maven is NOT needed, the project includes the Maven wrapper)

1. **Create the database, table and 30 sample items**
   - MySQL Workbench: open `schema.sql` and click Run, or
   - Command line: `mysql -u root -p < schema.sql`
2. **Set your MySQL password** (only needed if your root user has a password).
   Windows PowerShell:

       $env:DB_PASSWORD="your_mysql_password"

   Mac/Linux:

       export DB_PASSWORD=your_mysql_password

3. **Start the app**

   Windows: `.\mvnw.cmd spring-boot:run`
   Mac/Linux: `./mvnw spring-boot:run`

   App runs on http://localhost:8080

4. **Try it** (default API key is `my-secret-key`):

       curl http://localhost:8080/items -H "X-API-KEY: my-secret-key"

## Configuration (optional)
All values have defaults, so only change what you need
(environment variables, or edit `src/main/resources/application.properties`):

| Variable | Meaning | Default |
|----------|---------|---------|
| DB_URL | JDBC url | jdbc:mysql://localhost:3306/inventory_db |
| DB_USERNAME | MySQL user | root |
| DB_PASSWORD | MySQL password | (empty) |
| INVENTORY_API_KEY | API key | my-secret-key |

## Authentication
Every request must send the header `X-API-KEY: <key>`.
Missing or wrong key returns `401 Invalid API Key`.

## Endpoints
| Method | URL | Description |
|--------|-----|-------------|
| POST   | /items | Create item (201) |
| GET    | /items | Get all items |
| GET    | /items/pagination?page=0&size=5&sort=id,asc | Paginated + sorted list |
| GET    | /items/{id} | Get item by id (404 if not found) |
| PUT    | /items/{id} | Update item |
| DELETE | /items/{id} | Delete item (204) |

**Validation:** `name` required (max 100), `description` max 500,
`quantity` required and >= 0, `price` required, > 0, max 2 decimals.
Invalid input returns `400 Bad Request`.

## Sample requests

    curl -X POST http://localhost:8080/items \
      -H "Content-Type: application/json" -H "X-API-KEY: my-secret-key" \
      -d '{"name":"Keyboard","description":"Mechanical","quantity":10,"price":499.99}'

    curl "http://localhost:8080/items/pagination?page=0&size=5&sort=id,asc" \
      -H "X-API-KEY: my-secret-key"

    curl http://localhost:8080/items/1 -H "X-API-KEY: my-secret-key"

## Run tests (no database needed)

    ./mvnw test        (Windows: .\mvnw.cmd test)

- `ItemControllerTest`: endpoints and validation (service mocked)
- `ApiKeySecurityTest`: API key filter (401 / 200)

## Database
`schema.sql` creates database `inventory_db`, table `items`, and inserts 30 sample items.
Running it again resets the table to the sample data.

## Troubleshooting
- **Access denied for user 'root'**: set `DB_PASSWORD` (step 2).
- **Unknown database**: run `schema.sql` first (step 1).
- **401 Invalid API Key**: add header `X-API-KEY: my-secret-key`.
-