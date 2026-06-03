package com.daniel.library_management.repository;

import com.daniel.library_management.model.Member;
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
 * Repository class for Member database operations using JDBC.
 * 
 * <p>Handles all database interactions for Member entities,
 * including registration, updates, and search operations.</p>
 * 
 * @author Daniel
 * @version 1.0.0
 */
@Repository
public class MemberRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * RowMapper for converting database rows to Member objects.
     */
    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Member.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .membershipId(rs.getString("membership_id"))
                .phoneNumber(rs.getString("phone_number"))
                .joinDate(rs.getDate("join_date").toLocalDate())
                .active(rs.getBoolean("active"))
                .build();
        }
    }
    
    /**
     * Saves a new member to the database.
     * 
     * @param member The member to save
     * @return The saved member
     */
    @Transactional
    public Member save(Member member) {
        String sql = """
            INSERT INTO members (id, name, email, membership_id, phone_number, join_date, active)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        
        jdbcTemplate.update(sql,
            member.getId(),
            member.getName(),
            member.getEmail(),
            member.getMembershipId(),
            member.getPhoneNumber(),
            member.getJoinDate(),
            member.isActive()
        );
        
        return member;
    }
    
    /**
     * Finds a member by their UUID.
     * 
     * @param id The member's UUID
     * @return Optional containing the member if found
     */
    public Optional<Member> findById(String id) {
        String sql = "SELECT * FROM members WHERE id = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, new MemberRowMapper(), id);
            return Optional.ofNullable(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Finds a member by their email address.
     * 
     * @param email The email to search for
     * @return Optional containing the member if found
     */
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM members WHERE email = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, new MemberRowMapper(), email);
            return Optional.ofNullable(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Finds a member by their membership ID.
     * 
     * @param membershipId The membership ID to search for
     * @return Optional containing the member if found
     */
    public Optional<Member> findByMembershipId(String membershipId) {
        String sql = "SELECT * FROM members WHERE membership_id = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, new MemberRowMapper(), membershipId);
            return Optional.ofNullable(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Retrieves all members from the database.
     * 
     * @return List of all members
     */
    public List<Member> findAll() {
        String sql = "SELECT * FROM members ORDER BY name";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }
    
    /**
     * Finds all active members.
     * 
     * @return List of active members
     */
    public List<Member> findActiveMembers() {
        String sql = "SELECT * FROM members WHERE active = true ORDER BY name";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }
    
    /**
     * Updates an existing member.
     * 
     * @param member The member with updated information
     * @return The updated member
     */
    @Transactional
    public Member update(Member member) {
        String sql = """
            UPDATE members 
            SET name = ?, email = ?, phone_number = ?, active = ?
            WHERE id = ?
            """;
        
        jdbcTemplate.update(sql,
            member.getName(),
            member.getEmail(),
            member.getPhoneNumber(),
            member.isActive(),
            member.getId()
        );
        
        return member;
    }
    
    /**
     * Deactivates a member (soft delete).
     * 
     * @param id The member's UUID
     * @return true if deactivated successfully
     */
    @Transactional
    public boolean deactivate(String id) {
        String sql = "UPDATE members SET active = false WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }
    
    /**
     * Gets the total count of members.
     * 
     * @return Total member count
     */
    public long count() {
        String sql = "SELECT COUNT(*) FROM members";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
    
    /**
     * Bulk inserts multiple members using batch update.
     * 
     * @param members List of members to insert
     * @return Number of members inserted
     */
    @Transactional
    public int batchSave(List<Member> members) {
        String sql = """
            INSERT INTO members (id, name, email, membership_id, phone_number, join_date, active)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        
        List<Object[]> batchArgs = members.stream()
            .map(member -> new Object[]{
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getMembershipId(),
                member.getPhoneNumber(),
                member.getJoinDate(),
                member.isActive()
            })
            .toList();
        
        int[] results = jdbcTemplate.batchUpdate(sql, batchArgs);
        return results.length;
    }
}