package com.daniel.library_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Library Management System Application
 * 
 * <p>This is the main entry point for the Library Management System.
 * The application provides REST APIs for managing books, members, and book loans.
 * It uses JDBC for database operations with support for both H2 (in-memory)
 * and MySQL databases.</p>
 * 
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li>CRUD operations for books and members</li>
 *   <li>Borrowing and returning books with transaction support</li>
 *   <li>UUID-based identifiers for all entities</li>
 *   <li>Comprehensive error handling and validation</li>
 *   <li>Support for generating large datasets (50k+ records)</li>
 *   <li>ACID transactions for data integrity</li>
 * </ul>
 * 
 * @author Daniel
 * @version 1.0.0
 * @since 2024-01-01
 */
@EnableFeignClients
@SpringBootApplication
public class LibraryManagementApplication {
    
    /**
     * The main method that starts the Spring Boot application.
     * 
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementApplication.class, args);
        
        // Display startup information
        System.out.println("\n========================================");
        System.out.println("LIBRARY MANAGEMENT SYSTEM STARTED");
        System.out.println("========================================");
        System.out.println("Access endpoints at: http://localhost:8080");
        System.out.println("Swagger UI: http://localhost:8081/swagger-ui.html");
        System.out.println("H2 Console: http://localhost:8081/h2-console");
        System.out.println("========================================\n");
    }
}