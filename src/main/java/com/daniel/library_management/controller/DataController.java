package com.daniel.library_management.controller;

import com.daniel.library_management.service.BookService;
import com.daniel.library_management.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for generating test data.
 * 
 * <p>This controller provides endpoints to generate large amounts
 * of test data for performance testing and demonstration.</p>
 * 
 * @author Daniel
 * @version 1.0.0
 */
@RestController
@RequestMapping({"/api/v1/data", "/api/data"})
@Tag(name = "Data Generation", description = "Endpoints for generating test data")
public class DataController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private MemberService memberService;
    
    /**
     * Generates random books for testing.
     * 
     * @param count Number of books to generate (default: 1000, max: 100000)
     * @return Response with generation statistics
     */
    @PostMapping("/books")
    @Operation(summary = "Generate random books", description = "Generates specified number of random books for testing")
    public ResponseEntity<Map<String, Object>> generateBooks(
            @RequestParam(defaultValue = "1000") int count) {
        
        int maxCount = 100000;
        if (count > maxCount) {
            count = maxCount;
        }
        
        long startTime = System.currentTimeMillis();
        int generated = bookService.generateRandomBooks(count);
        long duration = System.currentTimeMillis() - startTime;
        
        Map<String, Object> response = new HashMap<>();
        response.put("generated", generated);
        response.put("duration_ms", duration);
        response.put("message", "Successfully generated " + generated + " books");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Generates random members for testing.
     * 
     * @param count Number of members to generate (default: 1000, max: 100000)
     * @return Response with generation statistics
     */
    @PostMapping("/members")
    @Operation(summary = "Generate random members", description = "Generates specified number of random members for testing")
    public ResponseEntity<Map<String, Object>> generateMembers(
            @RequestParam(defaultValue = "1000") int count) {
        
        int maxCount = 100000;
        if (count > maxCount) {
            count = maxCount;
        }
        
        long startTime = System.currentTimeMillis();
        int generated = memberService.generateRandomMembers(count);
        long duration = System.currentTimeMillis() - startTime;
        
        Map<String, Object> response = new HashMap<>();
        response.put("generated", generated);
        response.put("duration_ms", duration);
        response.put("message", "Successfully generated " + generated + " members");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Gets database statistics.
     * 
     * @return Statistics about the database
     */
    @GetMapping("/stats")
    @Operation(summary = "Get database statistics", description = "Returns counts of books, members, and loans")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total_books", bookService.getBookCount());
        stats.put("total_members", memberService.getMemberCount());
        stats.put("available_books", bookService.getAvailableBooks().size());
        
        return ResponseEntity.ok(stats);
    }
}