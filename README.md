# Inventory Management REST API

Spring Boot REST API for managing inventory items (CRUD), with validation,
API key security, pagination/sorting and unit tests.

**Tech:** Java 17, Spring Boot, Spring Data JPA, MySQL, Spring Security (API key filter),
Bean Validation, JUnit 5 + MockMvc, Maven

## Requirements
- Java 17 or higher
- MySQL 8 (with MySQL Workbench or the `mysql` command line)
- Postman (or curl) to try the API

Maven is **not** required. The project includes the Maven wrapper (`mvnw`).

## Setup (4 steps)

**1. Clone the project**

    git clone https://github.com/thelachu/Inventory-Management-REST-API.git
    cd Inventory-Management-REST-API

**2. Create the database**

Run `schema.sql` in MySQL. It creates the database `inventory_db`, the `items` table
and inserts 30 sample items.

- MySQL Workbench: File > Open SQL Script > `schema.sql` > click the lightning icon (Run)
- or command line: `mysql -u root -p < schema.sql`

**3. Set your MySQL password**

The password is never stored in the code. Set it as an environment variable
(skip this if your MySQL root user has no password).

Windows PowerShell:

    $env:DB_PASSWORD="your_mysql_password"

Mac / Linux:

    export DB_PASSWORD=your_mysql_password

**4. Start the application**

Windows:

    .\mvnw.cmd spring-boot:run

Mac / Linux:

    ./mvnw spring-boot:run

The API runs at **http://localhost:8080**.

## Run the tests
No database is needed for the tests.

Windows: `.\mvnw.cmd clean test`
Mac / Linux: `./mvnw clean test`

Expected result: `Tests run: 22, Failures: 0, Errors: 0` and `BUILD SUCCESS`.

| Test class | What it covers |
|------------|----------------|
| `ItemControllerTest` | All endpoints and validation rules (service is mocked) |
| `ApiKeySecurityTest` | API key filter: missing/wrong key returns 401, correct key passes |

## Authentication
Send this header with every request:

    X-API-KEY: lachu@123

Missing or wrong key returns `401 Invalid API Key`.

## Endpoints

| Method | URL | Description | Success |
|--------|-----|-------------|---------|
| POST | `/items` | Create an item | 201 |
| GET | `/items` | Get all items | 200 |
| GET | `/items/pagination?page=0&size=5&sort=id,asc` | Paginated and sorted list | 200 |
| GET | `/items/{id}` | Get one item (404 if not found) | 200 |
| PUT | `/items/{id}` | Update an item | 200 |
| DELETE | `/items/{id}` | Delete an item | 204 |

**Sample request body (POST / PUT):**

    {
      "name": "Keyboard",
      "description": "Mechanical keyboard",
      "quantity": 10,
      "price": 499.99
    }

**Validation rules:**
- `name`: required, max 100 characters
- `description`: max 500 characters
- `quantity`: required, 0 or more
- `price`: required, greater than 0, max 2 decimal places

Invalid input returns `400 Bad Request`.

## Quick check in Postman
Add the header `X-API-KEY: lachu@123`, then try:

| Request | Expected |
|---------|----------|
| GET `/items` | 200, 30 items |
| GET `/items/pagination?page=0&size=5&sort=id,asc` | 200, 5 items |
| GET `/items/1` | 200 |
| GET `/items/999` | 404 |
| POST with a valid body | 201 |
| POST with blank name or quantity -1 | 400 |
| PUT `/items/1` with a valid body | 200 |
| DELETE `/items/1` | 204 |
| GET `/items` without the header | 401 |

## Configuration (optional)
All values have defaults. Change them with environment variables only if needed.

| Variable | Meaning | Default |
|----------|---------|---------|
| `DB_URL` | JDBC URL | `jdbc:mysql://localhost:3306/inventory_db` |
| `DB_USERNAME` | MySQL user | `root` |
| `DB_PASSWORD` | MySQL password | (empty) |
| `INVENTORY_API_KEY` | API key | `lachu@123` |

## Troubleshooting
- **Access denied for user 'root'**: set `DB_PASSWORD` (step 3).
- **Unknown database 'inventory_db'**: run `schema.sql` first (step 2).
- **Port 8080 already in use**: stop the other app running on port 8080.
- **401 Invalid API Key**: add the header `X-API-KEY: lachu@123`.
