package com.daniel.library_management.service;

import com.daniel.library_management.model.Book;
import com.daniel.library_management.model.Loan;
import com.daniel.library_management.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class LoanService {
    @Autowired  // Spring automatically injects these services
    private BookService bookService;
    
    @Autowired
    private MemberService memberService;
    
    private Map<Long, Loan> loans = new HashMap<>();
    private Long nextLoanId = 1L;
    
    // Borrow a book
    public Loan borrowBook(Long bookId, Long memberId) {
        Book book = bookService.getBookById(bookId);
        Member member = memberService.getMemberById(memberId);
        
        if (book == null || member == null || !book.isAvailable()) {
            return null; // Cannot borrow
        }
        
        // Create loan record
        Loan loan = new Loan(bookId, memberId);
        loan.setId(nextLoanId++);
        loans.put(loan.getId(), loan);
        
        // Update book status
        book.setAvailable(false);
        
        // Update member's borrowed books list
        if (member.getBorrowedBooks() == null) {
            member.setBorrowedBooks(new ArrayList<>());
        }
        member.getBorrowedBooks().add(bookId);
        
        return loan;
    }
    
    // Return a book
    public boolean returnBook(Long loanId) {
        Loan loan = loans.get(loanId);
        if (loan == null || loan.isReturned()) {
            return false;
        }
        
        // Update loan record
        loan.setReturned(true);
        loan.setReturnDate(java.time.LocalDate.now());
        
        // Update book status
        Book book = bookService.getBookById(loan.getBookId());
        if (book != null) {
            book.setAvailable(true);
        }
        
        // Update member's borrowed books list
        Member member = memberService.getMemberById(loan.getMemberId());
        if (member != null && member.getBorrowedBooks() != null) {
            member.getBorrowedBooks().remove(loan.getBookId());
        }
        
        return true;
    }
    
    // Get all active loans
    public List<Loan> getActiveLoans() {
        return loans.values().stream()
            .filter(loan -> !loan.isReturned())
            .toList();
    }
}