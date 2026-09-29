# Library Management API

A secured REST API for managing books, members, and loans. Built with Java 21 and Spring Boot 3.2.3, backed by Spring Data JPA and an H2 database for local development.

![Java 21](https://img.shields.io/badge/Java-21-007396)
![Spring Boot 3.2.3](https://img.shields.io/badge/Spring%20Boot-3.2.3-6DB33F)
![Tests](https://img.shields.io/badge/tests-unit%20%2B%20integration-4CAF50)

## Features

- Book CRUD, availability, and title/author search
- Google Books primary lookup with a local library fallback
- Member registration, updates, and soft deactivation
- Borrowing and returning with due dates and fines
- Transactional service operations and centralized error responses
- HTTP Basic authentication for application endpoints
- Swagger UI and OpenAPI documentation
- Unit tests with Mockito and integration tests with MockMvc
- H2 in-memory database for local development

## Data Sources

The application now uses a two-layer lookup strategy for book search:

- Primary source: Google Books API
- Fallback source: the local database/library store

This makes search resilient when Google is unavailable, rate-limited, or returns no relevant matches. Google responses include a `source` value of `google`; local results use `source` as `local`.

### Optional Google API key

The Google Books integration is optional for local development. If you have a Google Books API key, set it before starting the app:

```powershell
$env:GOOGLE_BOOKS_API_KEY = "your-key-here"
.\mvnw.cmd spring-boot:run
```

If no key is configured, the app still starts and will transparently fall back to the local library data.

## Requirements

- Java 21
- Maven 3.9+ (or use the included Maven Wrapper)
- Git

## Run Locally

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

The API starts on `http://localhost:8081`.

The default local credentials are:

- Username: `library-admin`
- Password: `change-me`

Change them before sharing or deploying the application:

```powershell
$env:LIBRARY_API_USERNAME = "admin"
$env:LIBRARY_API_PASSWORD = "use-a-long-random-password"
.\mvnw.cmd spring-boot:run
```

All API requests require HTTP Basic authentication. Swagger documentation is available at `/swagger-ui.html`; the H2 console is at `/h2-console` and is also protected.

5. **Test all end-points at once using powershell script .**
     **Navigate to where test-all-endpoints.ps1 is located open in command line and type**
      ```bash
   .\test-all-endpoints.ps1
   ```
      **Open the file and modify the values and see whats what .**
   

## API Endpoints

All endpoints below are authenticated.

### Books

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/books` | List all books |
| GET | `/api/books/{id}` | Get one book |
| POST | `/api/books` | Create a book |
| PUT | `/api/books/{id}` | Update a book |
| DELETE | `/api/books/{id}` | Delete a book |
| GET | `/api/books/search?q=keyword` | Search by title or author (`source=auto|google|local`) |
| GET | `/api/books/available` | List available books |

### Members

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/members` | List members |
| GET | `/api/members/{id}` | Get one member |
| POST | `/api/members` | Register a member |
| PUT | `/api/members/{id}` | Update a member |
| DELETE | `/api/members/{id}` | Deactivate a member |

### Loans

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/loans/borrow?bookId={bookId}&memberId={memberId}` | Borrow a book |
| PUT | `/api/loans/return?loanId={loanId}` | Return a book |
| GET | `/api/loans/active` | List active loans |
| GET | `/api/loans/member/{memberId}` | List a member's active loans |

## Example Requests

Create a book with cURL:

```bash
curl -u library-admin:change-me -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Clean Code","author":"Robert C. Martin","isbn":"9780132350884","publicationYear":2008}'
```

List books:

```bash
curl -u library-admin:change-me http://localhost:8081/api/books
```

Search books through Google first, with automatic fallback:

```bash
curl -u library-admin:change-me "http://localhost:8081/api/books/search?q=harry+potter"
curl -u library-admin:change-me "http://localhost:8081/api/books/search?q=harry+potter&source=local"
```

Borrow a book:

```bash
curl -u library-admin:change-me -X POST "http://localhost:8081/api/loans/borrow?bookId={bookId}&memberId={memberId}"
```

## Testing

Run the complete suite:

```powershell
.\mvnw.cmd clean test
```

The test suite includes service unit tests for duplicate ISBN handling and default book state, plus Spring Boot integration tests covering application startup, authentication enforcement, and an authenticated book workflow.

## Configuration

Configuration is in `src/main/resources/application.properties`. The default profile uses an H2 in-memory database. For deployment, provide `LIBRARY_API_USERNAME` and `LIBRARY_API_PASSWORD` as environment variables and use a managed database with production credentials.

Do not commit passwords, tokens, or database credentials. The sample credential exists only to make local development straightforward.

## Project Structure

```text
src/main/java/com/daniel/library_management
├── config          # HTTP security configuration
├── controller      # REST endpoints
├── exception       # API exception types and handler
├── model           # JPA entities
├── repository      # Spring Data repositories
└── service         # Business rules and transactions
```

## License

See [LICENSE](LICENSE).
