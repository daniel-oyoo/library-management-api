package com.daniel.library_management.controller;

import com.daniel.library_management.model.Loan;
import com.daniel.library_management.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    
    @Autowired
    private LoanService loanService;
    
    // POST /loans/borrow - Borrow a book (your /members/borrow/{id})
    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@RequestParam Long bookId, @RequestParam Long memberId) {
        Loan loan = loanService.borrowBook(bookId, memberId);
        if (loan == null) {
            return ResponseEntity.badRequest().body("Cannot borrow book. Check availability.");
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Book borrowed successfully");
        response.put("loanId", loan.getId());
        response.put("dueDate", loan.getDueDate());
        return ResponseEntity.ok(response);
    }
    
    // PUT /loans/return - Return a book (your /members/return/{id})
    @PutMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam Long loanId) {
        boolean returned = loanService.returnBook(loanId);
        if (!returned) {
            return ResponseEntity.badRequest().body("Cannot return book. Invalid loan ID.");
        }
        return ResponseEntity.ok("Book returned successfully");
    }
    
    // GET /loans/active - View all active loans
    @GetMapping("/active")
    public ResponseEntity<List<Loan>> getActiveLoans() {
        List<Loan> activeLoans = loanService.getActiveLoans();
        return ResponseEntity.ok(activeLoans);
    }
    
    // GET /loans/member/{memberId} - View member's active loans
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Loan>> getMemberLoans(@PathVariable Long memberId) {
        List<Loan> memberLoans = loanService.getActiveLoans().stream()
            .filter(loan -> loan.getMemberId().equals(memberId))
            .toList();
        return ResponseEntity.ok(memberLoans);
    }
}