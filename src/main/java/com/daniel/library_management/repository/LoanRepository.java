package com.daniel.library_management.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daniel.library_management.model.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan,String>{


    @Query(value="SELECT * FROM loans WHERE dueDate > \'2026-08-27\'",nativeQuery=true)
    List<Loan> findOverdueLoans();

    @Query(value="SELECT * FROM loans WHERE returnDate = NULL AND memberId=memberId ",nativeQuery=true)
    List<Loan> findActiveLoansByMemberId(@Param("memberId")String memberId);

     @Query(value="SELECT COUNT(*) FROM loans WHERE returnDate = NULL AND memberId=memberId ",nativeQuery=true)
    int getActiveLoanCountForMember(@Param("memberId")String memberId);

    @Query(value="UPDATE loans SET return_Date=returnDate fineAmount=fine_Amount WHERE loanId=loan_Id",nativeQuery=true)
    void returnBook(@Param("loanId")String loanId,@Param("returnDate") LocalDate returnDate, @Param("fineAmount")double fineAmount);}