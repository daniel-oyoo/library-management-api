package com.daniel.library_management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private Long id;                    // Unique identifier for member
    private String name;                // Member's full name
    private String email;               // Member's email address
    private String membershipId;        // Library membership ID
    private String phoneNumber;         // Contact phone number
    private LocalDate joinDate;         // When member joined
    private List<Long> borrowedBooks;   // IDs of currently borrowed books
    private boolean active;             // Is membership active?
    
    public Member(String name, String email) {
        this.name = name;
        this.email = email;
        this.borrowedBooks = new ArrayList<>();
        this.active = true;
        this.joinDate = LocalDate.now();
    }
}