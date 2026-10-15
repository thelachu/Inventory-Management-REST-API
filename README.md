# Inventory Management REST API

Spring Boot REST API for CRUD operations on inventory items, with validation,
API key security, pagination/sorting and unit tests.

**Tech:** Java 17, Spring Boot, Spring Data JPA, MySQL, Spring Security (API key filter),
Bean Validation, JUnit 5 + MockMvc, Maven

## Setup
Requirements: Java 17, Maven, MySQL 8

1. Create the database and table: `mysql -u root -p < schema.sql`
2. Set environment variables (or edit the defaults in `src/main/resources/application.properties`):

   | Variable | Meaning | Default |
      |----------|---------|---------|
   | DB_URL | JDBC url | jdbc:mysql://localhost:3306/inventory_db |
   | DB_USERNAME | MySQL user | root |
   | DB_PASSWORD | MySQL password | (empty) |
   | INVENTORY_API_KEY | API key | my-secret-key |

3. Run the app: `mvn spring-boot:run` (starts on http://localhost:8080)

## Authentication
Every request must send the header `X-API-KEY: <key>`.
Missing or wrong key returns `401 Invalid API Key`.
The key is read from the property `inventory.api-key`.

## Endpoints
| Method | URL | Description |
|--------|-----|-------------|
| POST   | /items | Create item (201) |
| GET    | /items | Get all items |
| GET    | /items/pagination?page=0&size=5&sort=id,asc | Paginated + sorted list |
| GET    | /items/{id} | Get item by id |
| PUT    | /items/{id} | Update item |
| DELETE | /items/{id} | Delete item (204) |

**Validation:** `name` required (max 100), `description` max 500,
`quantity` required and >= 0, `price` required, > 0, max 2 decimals.
Invalid input returns `400 Bad Request`.

## Run tests (no database needed)

    mvn test

- `ItemControllerTest`: endpoints and validation (service mocked)
- `ApiKeySecurityTest`: API key filter (401 / 200)

## Sample requests

    curl -X POST http://localhost:8080/items \
      -H "Content-Type: application/json" -H "X-API-KEY: my-secret-key" \
      -d '{"name":"Keyboard","description":"Mechanical","quantity":10,"price":499.99}'

    curl http://localhost:8080/items -H "X-API-KEY: my-secret-key"

    curl "http://localhost:8080/items/pagination?page=0&size=5&sort=id,asc" \
      -H "X-API-KEY: my-secret-key"

## Database
MySQL schema is in `schema.sql` (table `items`).