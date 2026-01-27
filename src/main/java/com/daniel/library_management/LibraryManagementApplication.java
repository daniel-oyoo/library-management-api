package com.daniel.library_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // Enables Spring Boot auto-configuration and component scanning
public class LibraryManagementApplication {
    
    public static void main(String[] args) {
        // Starts the embedded Tomcat server on port 8080
        SpringApplication.run(LibraryManagementApplication.class, args);
        System.out.println("Library Management API is running!");
        System.out.println("Access endpoints at: http://localhost:8080");
    }
}