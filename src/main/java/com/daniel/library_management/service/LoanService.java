package com.daniel.library_management.service;

import com.daniel.library_management.exception.BusinessRuleViolationException;
import com.daniel.library_management.exception.ResourceNotFoundException;
import com.daniel.library_management.model.Book;
import com.daniel.library_management.model.Loan;
import com.daniel.library_management.model.Member;
import com.daniel.library_management.repository.LoanRepository;
import com.daniel.library_management.repository.impl.dao.LRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Loan business logic.
 * 
 * <p>Handles all book borrowing and returning operations with full
 * transaction support. Ensures data consistency between book availability,
 * member borrowing limits, and loan records.</p>
 * 
 * @author Daniel
 * @version 1.0.0
 */
@Service
public class LoanService {
    
    /** Maximum number of books a member can borrow simultaneously */
    private static final int MAX_BORROW_LIMIT = 5;
    
    @Autowired
    private LRepository loanRepository;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private MemberService memberService;
    
    /**
     * Borrows a book for a member.
     * 
     * <p>This operation is performed within a single transaction to ensure
     * that the book availability, member borrowing limit, and loan record
     * are all updated consistently.</p>
     * 
     * @param bookId The book ID to borrow
     * @param memberId The member ID borrowing the book
     * @return The created loan record
     * @throws ResourceNotFoundException if book or member not found
     * @throws BusinessRuleViolationException if borrowing rules are violated
     */
    @Transactional(rollbackFor = Exception.class)
    public Loan borrowBook(String bookId, String memberId) {
        // Validate book exists and is available
        Book book = bookService.getBookById(bookId);
        if (!book.isAvailable()) {
            throw new BusinessRuleViolationException("Book is currently not available for borrowing");
        }
        
        // Validate member exists and is active
        Member member = memberService.getMemberById(memberId);
        if (!member.isActive()) {
            throw new BusinessRuleViolationException("Member account is inactive. Cannot borrow books.");
        }
        
        // Check member's borrowing limit
        int activeLoans=7;
        // // = loanRepository.getActiveLoanCountForMember(memberId);
        if (activeLoans >= MAX_BORROW_LIMIT) {
            throw new BusinessRuleViolationException(
                "Member has reached maximum borrowing limit of " + MAX_BORROW_LIMIT + " books"
            );
        }
        
        // Create and save the loan
        Loan loan = new Loan(bookId, memberId);
        Loan savedLoan = loanRepository.save(loan);
        
        // Update book availability
        bookService.updateBook(bookId, Book.builder()
            .title(book.getTitle())
            .author(book.getAuthor())
            .isbn(book.getIsbn())
            .publicationYear(book.getPublicationYear())
            .available(false)
            .build());
        
        return savedLoan;
    }
    
    /**
     * Returns a borrowed book.
     * 
     * <p>This operation calculates any overdue fines and updates
     * both the loan record and book availability within a transaction.</p>
     * 
     * @param loanId The loan ID
     * @return The updated loan with fine amount
     * @throws ResourceNotFoundException if loan not found
     * @throws BusinessRuleViolationException if book already returned
     */
    @Transactional(rollbackFor = Exception.class)
    public Loan returnBook(String loanId) {
        Loan loan = loanRepository.findById(loanId)
            .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));
        
        if (loan.isReturned()) {
            throw new BusinessRuleViolationException("Book has already been returned");
        }
        
        // Return the book and calculate fine
        loan.returnBook();
        LocalDate returnDate = LocalDate.now();
        double fineAmount = loan.calculateFine();
        
        // Update loan record
        //loanRepository.returnBook(loanId, returnDate, fineAmount);
        
        // Update book availability
        Book book = bookService.getBookById(loan.getBookId());
        bookService.updateBook(loan.getBookId(), Book.builder()
            .title(book.getTitle())
            .author(book.getAuthor())
            .isbn(book.getIsbn())
            .publicationYear(book.getPublicationYear())
            .available(true)
            .build());
        
        return loan;
    }
    
    /**
     * Retrieves all active loans.
     * 
     * @return List of active loans
     */
    @Transactional(readOnly = true)
    public List<Loan> getActiveLoans() {
        return loanRepository.findAll();
        //return loanRepository.findActiveLoans();
    }
    
    /**
     * Retrieves a loan by ID.
     * 
     * @param loanId The loan ID
     * @return The loan
     */
    @Transactional(readOnly = true)
    public Loan getLoanById(String loanId) {
        return loanRepository.findById(loanId)
            .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));
    }
    
    /**
     * Retrieves all loans for a member.
     * 
     * @param memberId The member ID
     * @return List of loans
     */
    @Transactional(readOnly = true)
    public Optional<Loan> getMemberLoans(String memberId) {
        return loanRepository.findById(memberId);
    }
    
    /**
     * Retrieves active loans for a member.
     * 
     * @param memberId The member ID
     * @return List of active loans
     */
    @Transactional(readOnly = true)
    public List<Loan> getMemberActiveLoans(String memberId) {
        return loanRepository.findAll();
        //return loanRepository.findActiveLoansByMemberId(memberId);
    }
    
    /**
     * Retrieves all overdue loans.
     * 
     * @return List of overdue loans
     */
    @Transactional(readOnly = true)
    public List<Loan> getOverdueLoans() {
        return loanRepository.findAll();
        //return loanRepository.findOverdueLoans();
    }
}