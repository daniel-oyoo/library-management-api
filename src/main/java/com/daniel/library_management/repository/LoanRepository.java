package com.daniel.library_management.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daniel.library_management.model.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan,String>{


    @Query("select l from Loan l where l.returned = false and l.dueDate < CURRENT_DATE")
    List<Loan> findOverdueLoans();

    @Query("select l from Loan l where l.returned = false and l.memberId = :memberId")
    List<Loan> findActiveLoansByMemberId(@Param("memberId")String memberId);

    @Query("select count(l) from Loan l where l.returned = false and l.memberId = :memberId")
    int getActiveLoanCountForMember(@Param("memberId")String memberId);

    @Modifying
    @Query("update Loan l set l.returnDate = :returnDate, l.fineAmount = :fineAmount, l.returned = true where l.id = :loanId")
    void returnBook(@Param("loanId")String loanId,@Param("returnDate") LocalDate returnDate, @Param("fineAmount")double fineAmount);}