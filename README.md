#  Library Management API

A complete RESTful API for managing library operations, built with Spring Boot and Java. This API allows you to manage books, members, and book loans in a library system.

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.2-brightgreen)
![License](https://img.shields.io/badge/License-MIT-green)


##  Features

-  Book Management: CRUD operations for books
-  Member Management: Register, update, and manage library members
-  Loan System: Track book borrowing and returns
-  Search Functionality: Search books by title or author
-  Validation: Input validation for all endpoints
- In-Memory Storage: No database required for testing
- API Documentation: Complete endpoint documentation

##  Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/daniel-oyoo/library-management-api.git
   cd library-management-api
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **The API will start at:** `http://localhost:8080`

##  API Endpoints

###  Books

| Method | Endpoint | Description | Status Codes |
|--------|----------|-------------|--------------|
| GET | `/api/books` | Get all books | 200 OK |
| GET | `/api/books/{id}` | Get book by ID | 200 OK, 404 Not Found |
| POST | `/api/books` | Add new book | 201 Created, 400 Bad Request |
| PUT | `/api/books/{id}` | Update book | 200 OK, 404 Not Found |
| DELETE | `/api/books/{id}` | Delete book | 204 No Content, 404 Not Found |
| GET | `/api/books/search` | Search books by keyword (`?q=keyword`) | 200 OK |

###  Members

| Method | Endpoint | Description | Status Codes |
|--------|----------|-------------|--------------|
| GET | `/api/members` | Get all members | 200 OK |
| GET | `/api/members/{id}` | Get member by ID | 200 OK, 404 Not Found |
| POST | `/api/members` | Register new member | 201 Created |
| PUT | `/api/members/{id}` | Update member | 200 OK, 404 Not Found |
| DELETE | `/api/members/{id}` | Deactivate member | 204 No Content, 404 Not Found |
| POST | `/api/members/login` | Simple login simulation (`?email=user@example.com`) | 200 OK |

###  Loans

| Method | Endpoint | Description | Status Codes |
|--------|----------|-------------|--------------|
| POST | `/api/loans/borrow` | Borrow a book (`?bookId=1&memberId=1`) | 200 OK, 400 Bad Request |
| PUT | `/api/loans/return` | Return a book (`?loanId=1`) | 200 OK, 400 Bad Request |
| GET | `/api/loans/active` | Get all active loans | 200 OK |
| GET | `/api/loans/member/{memberId}` | Get member's active loans | 200 OK |

## Usage Examples

### Using cURL

**Create a Book:**
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "isbn": "9780743273565",
    "publicationYear": 1925
  }'
```

**Search Books:**
```bash
curl "http://localhost:8080/api/books/search?q=gatsby"
```

**Register a Member:**
```bash
curl -X POST http://localhost:8080/api/members \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Smith",
    "email": "john@example.com",
    "phoneNumber": "555-1234"
  }'
```

**Borrow a Book:**
```bash
curl -X POST "http://localhost:8080/api/loans/borrow?bookId=1&memberId=1"
```

**Return a Book:**
```bash
curl -X PUT "http://localhost:8080/api/loans/return?loanId=1"
```

**Member Login Simulation:**
```bash
curl -X POST "http://localhost:8080/api/members/login?email=john@example.com"
```

### Using PowerShell

Create a test script `test-api.ps1`:

```powershell
$baseUrl = "http://localhost:8080/api"

# Test book creation
$book = @{
    title = "1984"
    author = "George Orwell"
    isbn = "9780451524935"
    publicationYear = 1949
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "$baseUrl/books" -Method Post -Body $book -ContentType "application/json"
Write-Host "Created book: $($response.title)"

# Test search
$searchResults = Invoke-RestMethod -Uri "$baseUrl/books/search?q=1984" -Method Get
Write-Host "Search found: $($searchResults.Count) books"

# Test borrowing
$borrowResponse = Invoke-RestMethod -Uri "$baseUrl/loans/borrow?bookId=1&memberId=1" -Method Post
Write-Host "Borrow response: $($borrowResponse.message)"
```

##  Project Structure

```
src/main/java/com/daniel/library_management/
├── LibraryManagementApplication.java     # Main application class
├── controller/                           # REST controllers
│   ├── BookController.java              # /api/books endpoints
│   ├── MemberController.java            # /api/members endpoints  
│   └── LoanController.java              # /api/loans endpoints
├── model/                               # Data models
│   ├── Book.java
│   ├── Member.java
│   └── Loan.java
└── service/                             # Business logic
    ├── BookService.java
    ├── MemberService.java
    └── LoanService.java
```

## Technologies Used

- **Spring Boot 3.2.2**: Framework for creating stand-alone Spring applications
- **Spring Web**: For building RESTful web services
- **Lombok**: To reduce boilerplate code
- **Maven**: Dependency management and build automation
- **Java 21**: Programming language

##  Testing the API

### Method 1: Using PowerShell (Recommended)
```powershell
# Run the complete test suite
.\run-tests.ps1
```

### Method 2: Manual Testing
1. Start the application: `mvn spring-boot:run`
2. Use Postman, cURL, or PowerShell to test endpoints
3. Access `http://localhost:8080/api/books` to verify it's working

### Method 3: Unit Testing (Future Enhancement)
```bash
mvn test
```

##  Sample Data Flow

```mermaid
graph LR
    A[Client Request] --> B[Controller];
    B --> C[Service Layer];
    C --> D[Business Logic];
    D --> E[In-Memory Storage];
    E --> F[Response];
    F --> G[Client];
```

##  Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit changes: `git commit -m 'Add some feature'`
4. Push to branch: `git push origin feature-name`
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

##  Author

**Daniel Oyoo**  
- GitHub: [@Daniel Oyoo](https://github.com/daniel-oyoo)
- Project Link: [https://github.com/daniel-oyoo/library-management-api](https://github.com/yourusername/library-management-api)

##  Acknowledgments

- Spring Boot documentation
- Introduction to API
- Stack Overflow community
- All contributors and testers

---
