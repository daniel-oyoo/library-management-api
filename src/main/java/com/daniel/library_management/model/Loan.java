package com.daniel.library_management.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Represents a Book Loan transaction in the library system.
 * 
 * <p>Loans track when members borrow books, including due dates
 * and return information. Each loan has a unique UUID identifier
 * and maintains the complete history of borrowing activities.</p>
 * 
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Standard loan period is 14 days</li>
 *   <li>Fine of $0.50 per day for overdue books</li>
 *   <li>A book cannot be borrowed twice simultaneously</li>
 *   <li>Returned loans are kept for historical records</li>
 * </ul>
 * 
 * @author Daniel
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    
    /** Default loan period in days */
    public static final int DEFAULT_LOAN_DAYS = 14;
    
    /** Daily fine rate in dollars for overdue books */
    public static final double DAILY_FINE_RATE = 0.50;
    
    /**
     * Unique identifier for the loan transaction.
     */
    private String id;
    
    /**
     * ID of the book being borrowed.
     */
    private String bookId;
    
    /**
     * ID of the member borrowing the book.
     */
    private String memberId;
    
    /**
     * Date when the book was borrowed.
     */
    private LocalDate borrowDate;
    
    /**
     * Date by which the book should be returned.
     * Calculated as borrowDate + DEFAULT_LOAN_DAYS.
     */
    private LocalDate dueDate;
    
    /**
     * Date when the book was actually returned.
     * Null if the book has not been returned yet.
     */
    private LocalDate returnDate;
    
    /**
     * Indicates whether the book has been returned.
     */
    @Builder.Default
    private boolean returned = false;
    
    /**
     * Fine amount calculated for overdue returns.
     * Amount in dollars (USD).
     */
    @Builder.Default
    private Double fineAmount = 0.0;
    
    /**
     * Creates a new loan for borrowing a book.
     * Automatically generates UUID and sets due date.
     * 
     * @param bookId ID of the book to borrow
     * @param memberId ID of the member borrowing the book
     */
    public Loan(String bookId, String memberId) {
        this.id = UUID.randomUUID().toString();
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(DEFAULT_LOAN_DAYS);
        this.returned = false;
        this.fineAmount = 0.0;
    }
    
    /**
     * Calculates the fine for an overdue book.
     * 
     * @return The fine amount based on days overdue
     */
    public double calculateFine() {
        if (returned && returnDate != null && returnDate.isAfter(dueDate)) {
            long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
            return daysOverdue * DAILY_FINE_RATE;
        } else if (!returned && LocalDate.now().isAfter(dueDate)) {
            long daysOverdue = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
            return daysOverdue * DAILY_FINE_RATE;
        }
        return 0.0;
    }
    
    /**
     * Returns the book and updates fine if applicable.
     */
    public void returnBook() {
        this.returned = true;
        this.returnDate = LocalDate.now();
        this.fineAmount = calculateFine();
    }
}