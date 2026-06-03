package com.daniel.library_management.repository;

import com.daniel.library_management.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for Loan database operations using JDBC.
 * 
 * <p>Manages all loan-related database operations including
 * borrowing, returning, and tracking overdue books.</p>
 * 
 * @author Daniel
 * @version 1.0.0
 */
@Repository
public class LoanRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * RowMapper for converting database rows to Loan objects.
     */
    private static class LoanRowMapper implements RowMapper<Loan> {
        @Override
        public Loan mapRow(ResultSet rs, int rowNum) throws SQLException {
            Loan loan = Loan.builder()
                .id(rs.getString("id"))
                .bookId(rs.getString("book_id"))
                .memberId(rs.getString("member_id"))
                .borrowDate(rs.getDate("borrow_date").toLocalDate())
                .dueDate(rs.getDate("due_date").toLocalDate())
                .returned(rs.getBoolean("returned"))
                .fineAmount(rs.getDouble("fine_amount"))
                .build();
            
            // Handle nullable return date
            java.sql.Date returnDate = rs.getDate("return_date");
            if (returnDate != null) {
                loan.setReturnDate(returnDate.toLocalDate());
            }
            
            return loan;
        }
    }
    
    /**
     * Saves a new loan to the database.
     * 
     * @param loan The loan to save
     * @return The saved loan
     */
    @Transactional
    public Loan save(Loan loan) {
        String sql = """
            INSERT INTO loans (id, book_id, member_id, borrow_date, due_date, returned, fine_amount)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        
        jdbcTemplate.update(sql,
            loan.getId(),
            loan.getBookId(),
            loan.getMemberId(),
            loan.getBorrowDate(),
            loan.getDueDate(),
            loan.isReturned(),
            loan.getFineAmount()
        );
        
        return loan;
    }
    
    /**
     * Finds a loan by its ID.
     * 
     * @param id The loan's UUID
     * @return Optional containing the loan if found
     */
    public Optional<Loan> findById(String id) {
        String sql = "SELECT * FROM loans WHERE id = ?";
        try {
            Loan loan = jdbcTemplate.queryForObject(sql, new LoanRowMapper(), id);
            return Optional.ofNullable(loan);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Finds all active (not returned) loans.
     * 
     * @return List of active loans
     */
    public List<Loan> findActiveLoans() {
        String sql = "SELECT * FROM loans WHERE returned = false ORDER BY due_date";
        return jdbcTemplate.query(sql, new LoanRowMapper());
    }
    
    /**
     * Finds all loans for a specific member.
     * 
     * @param memberId The member's UUID
     * @return List of loans for the member
     */
    public List<Loan> findByMemberId(String memberId) {
        String sql = "SELECT * FROM loans WHERE member_id = ? ORDER BY borrow_date DESC";
        return jdbcTemplate.query(sql, new LoanRowMapper(), memberId);
    }
    
    /**
     * Finds all active loans for a specific member.
     * 
     * @param memberId The member's UUID
     * @return List of active loans for the member
     */
    public List<Loan> findActiveLoansByMemberId(String memberId) {
        String sql = "SELECT * FROM loans WHERE member_id = ? AND returned = false";
        return jdbcTemplate.query(sql, new LoanRowMapper(), memberId);
    }
    
    /**
     * Finds all loans for a specific book.
     * 
     * @param bookId The book's UUID
     * @return List of loans for the book
     */
    public List<Loan> findByBookId(String bookId) {
        String sql = "SELECT * FROM loans WHERE book_id = ? ORDER BY borrow_date DESC";
        return jdbcTemplate.query(sql, new LoanRowMapper(), bookId);
    }
    
    /**
     * Finds all overdue loans.
     * 
     * @return List of overdue loans
     */
    public List<Loan> findOverdueLoans() {
        String sql = "SELECT * FROM loans WHERE returned = false AND due_date < CURRENT_DATE";
        return jdbcTemplate.query(sql, new LoanRowMapper());
    }
    
    /**
     * Updates a loan (typically for returning books).
     * 
     * @param loan The loan with updated information     * @return The updated loan
     */
    @Transactional
    public Loan update(Loan loan) {
        String sql = """
            UPDATE loans 
            SET return_date = ?, returned = ?, fine_amount = ?
            WHERE id = ?
            """;
        
        jdbcTemplate.update(sql,
            loan.getReturnDate(),
            loan.isReturned(),
            loan.getFineAmount(),
            loan.getId()
        );
        
        return loan;
    }
    
    /**
     * Returns a book by updating the loan record.
     * 
     * @param loanId The loan ID
     * @param returnDate The return date
     * @param fineAmount The calculated fine
     * @return true if updated successfully
     */
    @Transactional
    public boolean returnBook(String loanId, LocalDate returnDate, double fineAmount) {
        String sql = """
            UPDATE loans 
            SET return_date = ?, returned = true, fine_amount = ?
            WHERE id = ? AND returned = false
            """;
        
        int rowsAffected = jdbcTemplate.update(sql, returnDate, fineAmount, loanId);
        return rowsAffected > 0;
    }
    
    /**
     * Gets the count of active loans for a member.
     * 
     * @param memberId The member's UUID
     * @return Number of active loans
     */
    public int getActiveLoanCountForMember(String memberId) {
        String sql = "SELECT COUNT(*) FROM loans WHERE member_id = ? AND returned = false";
        return jdbcTemplate.queryForObject(sql, Integer.class, memberId);
    }
    
    /**
     * Checks if a book is currently borrowed.
     * 
     * @param bookId The book's UUID
     * @return true if the book is currently borrowed
     */
    public boolean isBookBorrowed(String bookId) {
        String sql = "SELECT COUNT(*) FROM loans WHERE book_id = ? AND returned = false";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, bookId);
        return count > 0;
    }
}