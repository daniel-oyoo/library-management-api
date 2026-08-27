package com.daniel.library_management.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.daniel.library_management.model.Loan;

public class LoanDomain {

    private final Loan loan;


    public LoanDomain(Loan loan){
     this.loan=loan;
    }

        /**
     * Calculates the fine for an overdue book.
     * 
     * @return The fine amount based on days overdue
     */
    public double calculateFine() {
        if (
            loan.isReturned()&&
             loan.getReturnDate()!= null &&
              loan.getReturnDate().isAfter(loan.getDueDate())
        ) {
            long daysOverdue = ChronoUnit.DAYS.between(loan.getDueDate(),  loan.getReturnDate());
            return daysOverdue * Loan.DAILY_FINE_RATE;
        } else if (! loan.isReturned() && LocalDate.now().isAfter(loan.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now());
            return daysOverdue * Loan.DAILY_FINE_RATE;
        }
        return 0.0;
    }
    
    /**
     * Returns the book and updates fine if applicable.
     */
    public void returnBook() {
        this.loan.setReturned(true);
        this.loan.setReturnDate(LocalDate.now());
        this.loan.setFineAmount(calculateFine());
    }
}
