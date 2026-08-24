package com.daniel.library_management.repository.impl.dao;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daniel.library_management.model.Loan;

@Repository
public interface LRepository extends JpaRepository<Loan,String>{

    Optional<Loan> findById(String id);

    List<Loan> findByMemeberId(String memberId);


    List<Loan> findByBookId(String bookId);

    Loan save(Loan loan);


    List<Loan> findAll();

    int getActiveLoanCountForMember(String memberId);

    List<Loan> findActiveLoans();

    List<Loan> findActiveLoansByMemberId(String memberId);

    Optional<Loan> findLoanByMemberId(String memberId);

    List<Loan> findOverdueLoans();
    
}
