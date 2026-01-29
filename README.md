# Library Management API

A complete RESTful API for managing library operations, built with Spring Boot and Java. This API allows you to manage books, members, and book loans in a library system.

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.2-brightgreen)
![License](https://img.shields.io/badge/License-MIT-green)

## Features

- Book Management: CRUD operations for books
- Member Management: Register, update, and manage library members
- Loan System: Track book borrowing and returns
- Search Functionality: Search books by title or author
- Validation: Input validation for all endpoints
- In-Memory Storage: No database required for testing
- API Documentation: Complete endpoint documentation

## Quick Start

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

5. **Test all end-points at once using powershell script .**
     **Navigate to where test-all-endpoints.ps1 is located open in command line and type**
      ```bash
   .\test-all-endpoints.ps1
   ```
      **Open the file and modify the values and see whats what .**
   

## API Endpoints

### Books

| Method | Endpoint | Description | Status Codes |
|--------|----------|-------------|--------------|
| GET | `/api/books` | Get all books | 200 OK |
| GET | `/api/books/{id}` | Get book by ID | 200 OK, 404 Not Found |
| POST | `/api/books` | Add new book | 201 Created, 400 Bad Request |
| PUT | `/api/books/{id}` | Update book | 200 OK, 404 Not Found |
| DELETE | `/api/books/{id}` | Delete book | 204 No Content, 404 Not Found |
| GET | `/api/books/search` | Search books by keyword (`?q=keyword`) | 200 OK |

### Members

| Method | Endpoint | Description | Status Codes |
|--------|----------|-------------|--------------|
| GET | `/api/members` | Get all members | 200 OK |
| GET | `/api/members/{id}` | Get member by ID | 200 OK, 404 Not Found |
| POST | `/api/members` | Register new member | 201 Created |
| PUT | `/api/members/{id}` | Update member | 200 OK, 404 Not Found |
| DELETE | `/api/members/{id}` | Deactivate member | 204 No Content, 404 Not Found |
| POST | `/api/members/login` | Simple login simulation (`?email=user@example.com`) | 200 OK |

### Loans

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

## Sample API Test Results

Here's a demonstration of the API in action using PowerShell:

### Test Commands:
```powershell
# Test the API
$baseUrl = "http://localhost:8080/api"

# Test 1: Get all books (should be empty initially)
Invoke-RestMethod -Uri "$baseUrl/books" -Method Get

# Test 2: Create a book
$book = @{
    title = "The Great Gatsby"
    author = "F. Scott Fitzgerald"
    isbn = "9780743273565"
    publicationYear = 1925
} | ConvertTo-Json

Invoke-RestMethod -Uri "$baseUrl/books" -Method Post -Body $book -ContentType "application/json"

# Test 3: Get the book you just created
Invoke-RestMethod -Uri "$baseUrl/books/1" -Method Get

# Test 4: Create a member
$member = @{
    name = "John Smith"
    email = "john@example.com"
    phoneNumber = "555-1234"
} | ConvertTo-Json

Invoke-RestMethod -Uri "$baseUrl/members" -Method Post -Body $member -ContentType "application/json"

# Test 5: Borrow the book
Invoke-RestMethod -Uri "$baseUrl/loans/borrow?bookId=1&memberId=1" -Method Post

# Test 6: Check active loans
Invoke-RestMethod -Uri "$baseUrl/loans/active" -Method Get
```

### Expected Results:

**Book Created Successfully:**
```json
{
  "id": 1,
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "isbn": "9780743273565",
  "publicationYear": 1925,
  "available": true,
  "addedDate": "2026-01-27"
}
```

**Member Registered Successfully:**
```json
{
  "id": 1,
  "name": "John Smith",
  "email": "john@example.com",
  "phoneNumber": "555-1234",
  "joinDate": "2026-01-27",
  "borrowedBooks": [],
  "active": true
}
```

**Book Borrowed Successfully:**
```json
{
  "message": "Book borrowed successfully",
  "loanId": 1,
  "dueDate": "2026-02-10"
}
```

**Active Loan Record:**
```json
{
  "id": 1,
  "bookId": 1,
  "memberId": 1,
  "borrowDate": "2026-01-27",
  "dueDate": "2026-02-10",
  "returned": false
}
```

### Quick Test Script:

Create `quick-test.ps1`:
```powershell
Write-Host "Testing Library Management API"

$baseUrl = "http://localhost:8080/api"

# Create book
$book = @{
    title = "Test Book"
    author = "Test Author"
    isbn = "1234567890"
    publicationYear = 2024
} | ConvertTo-Json

$bookResult = Invoke-RestMethod -Uri "$baseUrl/books" -Method Post -Body $book -ContentType "application/json"
Write-Host "Created book: $($bookResult.title)"

# Create member  
$member = @{
    name = "Test User"
    email = "test@example.com"
    phoneNumber = "555-9999"
} | ConvertTo-Json

$memberResult = Invoke-RestMethod -Uri "$baseUrl/members" -Method Post -Body $member -ContentType "application/json"
Write-Host "Created member: $($memberResult.name)"

# Borrow book
$loanResult = Invoke-RestMethod -Uri "$baseUrl/loans/borrow?bookId=1&memberId=1" -Method Post
Write-Host "Borrowed book. Loan ID: $($loanResult.loanId)"

Write-Host "Test Complete"
Write-Host "API is working correctly!"
```

## Troubleshooting

If you experience build errors, please refer to the [HELP.md](HELP.md) file for common issues and solutions. Common problems include:

- UTF-8 BOM errors in test files
- JAVA_HOME environment variable not set correctly
- Port 8080 already in use
- Lombok version issues

For a quick fix, you can use:
```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

## Project Structure

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

## Testing the API

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

## Sample Data Flow

```mermaid
graph LR
    A[Client Request] --> B[Controller];
    B --> C[Service Layer];
    C --> D[Business Logic];
    D --> E[In-Memory Storage];
    E --> F[Response];
    F --> G[Client];
```

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit changes: `git commit -m 'Add some feature'`
4. Push to branch: `git push origin feature-name`
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

**Daniel Oyoo**  
- GitHub: [@Daniel Oyoo](https://github.com/daniel-oyoo)
- Project Link: [https://github.com/daniel-oyoo/library-management-api](https://github.com/daniel-oyoo/library-management-api)

## Acknowledgments

- Spring Boot documentation
- Introduction to API
- Stack Overflow community
- All contributors and testers

---
