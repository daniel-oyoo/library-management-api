package com.daniel.library_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryManagementApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementApplication.class, args);
        System.out.println("Library Management API is running!");
        System.out.println("Access endpoints at: http://localhost:8080");
        System.out.println("Swagger UI at: http://localhost:8080/swagger-ui.html");
    }
}