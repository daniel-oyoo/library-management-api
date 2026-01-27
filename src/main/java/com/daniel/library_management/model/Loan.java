package com.daniel.library_management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    private Long id;                    // Unique loan transaction ID
    private Long bookId;                // ID of borrowed book
    private Long memberId;              // ID of borrowing member
    private LocalDate borrowDate;       // When book was borrowed
    private LocalDate dueDate;          // When book should be returned
    private LocalDate returnDate;       // When book was actually returned (null if not returned)
    private boolean returned;           // Has book been returned?
    
    public Loan(Long bookId, Long memberId) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusWeeks(2); // 2-week loan period
        this.returned = false;
    }
}