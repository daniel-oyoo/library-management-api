package com.daniel.library_management.repository.impl.dao;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daniel.library_management.model.Loan;

@Repository
public interface LRepository extends JpaRepository<Loan,String>{

    /* 
    int getActiveLoanCountForMember(String memberId);

    void returnBook(String loanId, LocalDate returnDate, double fineAmount);
    

    List<Loan> findById(String memberId);

    List<Loan> findActiveLoans();

    List<Loan> findOverdueLoans();

    List<Loan> findActiveLoansByMemberId(String memberId);
    */
   //Loan findById(String id);
    
}
