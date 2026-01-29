
# Library Management API - Help Guide

This guide will help you get started with the Library Management API and troubleshoot common issues.

## Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Git

### Running the Application
```bash
# Clone the repository
git clone https://github.com/daniel-oyoo/library-management-api.git
cd library-management-api

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The API will start at: http://localhost:8080

## Common Issues and Solutions

### 1. UTF-8 BOM Error in Test File
**Error:** `illegal character: '\ufeff'` or `illegal character: '\u0000'`

**Cause:** PowerShell sometimes adds Byte Order Mark (BOM) characters when creating files, which Java can't read.

**Solution:**
```powershell
# Delete the corrupted test file
Remove-Item "src\test\java\com\daniel\library_management\LibraryManagementApplicationTests.java" -Force

# Or skip tests when building
mvn clean install -DskipTests
mvn spring-boot:run
```

### 2. JAVA_HOME Not Set Correctly
**Error:** `The JAVA_HOME environment variable is not defined correctly`

**Solution:**
```powershell
# Set JAVA_HOME (adjust path to your Java installation)
$env:JAVA_HOME = "C:\Program Files\Zulu\zulu-21"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

To find your Java installation path:
```powershell
# Look for Java installations
Get-ChildItem "C:\Program Files\Java\" -Directory
```

### 3. Lombok Version Missing
**Error:** `Resolution of annotationProcessorPath dependencies failed: version can neither be null, empty nor blank`

**Solution:** Ensure your `pom.xml` includes the Lombok version (already fixed in this repository):
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.32</version>
    <optional>true</optional>
</dependency>
```

### 4. Port 8080 Already in Use
**Error:** `Web server failed to start. Port 8080 was already in use.`

**Solution:**
```bash
# Find and kill the process using port 8080
netstat -ano | findstr :8080
taskkill /PID [PID] /F

# Or change port in application.properties
echo "server.port=8081" >> src/main/resources/application.properties
```

### 5. Maven Wrapper Issues
**Error:** `Cannot start maven from wrapper` or similar wrapper errors

**Solution:** Use system Maven instead:
```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

Or regenerate the wrapper:
```bash
mvn -N io.takari:maven:wrapper
./mvnw clean install
```

## Quick Fix Commands

If you encounter build issues, use these reliable commands:

```powershell
# Complete fix sequence
cd library-management-api

# Fix Java if needed
$env:JAVA_HOME = "C:\Program Files\Zulu\zulu-21"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Delete corrupted test files
Remove-Item "src\test\java\com\daniel\library_management\LibraryManagementApplicationTests.java" -Force -ErrorAction SilentlyContinue

# Build skipping tests
mvn clean install -DskipTests

# Run the application
mvn spring-boot:run
```

## API Testing

### Using PowerShell
```powershell
$baseUrl = "http://localhost:8080/api"

# Test book creation
$book = @{
    title = "The Great Gatsby"
    author = "F. Scott Fitzgerald"
    isbn = "9780743273565"
    publicationYear = 1925
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "$baseUrl/books" -Method Post -Body $book -ContentType "application/json"
Write-Host "Created book: $($response.title)"
```

### Using cURL
```bash
# Create a book
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "1984",
    "author": "George Orwell",
    "isbn": "9780451524935",
    "publicationYear": 1949
  }'

# Get all books
curl http://localhost:8080/api/books
```

## Project Structure

```
library-management-api/
├── src/main/java/com/daniel/library_management/
│   ├── LibraryManagementApplication.java     # Main application class
│   ├── controller/                           # REST controllers
│   ├── model/                                # Data models
│   └── service/                              # Business logic
├── src/main/resources/
│   └── application.properties                # Configuration
├── pom.xml                                   # Maven dependencies
├── README.md                                 # Project documentation
└── HELP.md                                   # This help file
```

## Development Tips

### Adding New Features
1. Create model class in `model/` package
2. Create service in `service/` package  
3. Create controller in `controller/` package
4. Update README.md if adding new endpoints

### Testing Changes
```powershell
# After making changes
mvn clean compile
mvn spring-boot:run

# In another terminal, test endpoints
curl http://localhost:8080/api/books
```

## Learning Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Maven Getting Started](https://maven.apache.org/guides/getting-started/)
- [REST API Best Practices](https://restfulapi.net/)
- [GitHub Guides](https://guides.github.com/)

## Need More Help?

1. Check the [README.md](README.md) for detailed documentation
2. Look at existing code examples in the project
3. Search for error messages online
4. Create an issue on GitHub if you found a bug

---
