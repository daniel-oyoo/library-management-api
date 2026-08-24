package com.daniel.library_management.service;

import com.daniel.library_management.exception.DuplicateResourceException;
import com.daniel.library_management.exception.ResourceNotFoundException;
import com.daniel.library_management.model.Member;
import com.daniel.library_management.repository.MemberRepository;
import com.daniel.library_management.repository.impl.dao.MRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

/**
 * Service class for Member business logic.
 * 
 * <p>Handles all member-related operations including registration,
 * updates, and membership management. All operations are transactional.</p>
 * 
 * @author Daniel
 * @version 1.0.0
 */
@Service
public class MemberService {
    
    @Autowired
    private MemberRepository memberRepository;
    
    /**
     * Registers a new member.
     * 
     * @param member The member to register
     * @return The registered member
     * @throws DuplicateResourceException if email already exists
     */
    @Transactional
    public Member registerMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        
        // Check for duplicate email
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Member with email " + member.getEmail() + " already exists");
        }
        
        // Generate membership ID
        String membershipId = generateMembershipId();
        member.setMembershipId(membershipId);
        
        // Set default values
        if (member.getId() == null) {
            member.setId(java.util.UUID.randomUUID().toString());
        }
        member.setJoinDate(LocalDate.now());
        member.setActive(true);
        
        return memberRepository.save(member);
    }
    
    /**
     * Generates a unique membership ID.
     * Format: LIB-YYYY-XXXXX
     * 
     * @return Generated membership ID
     */
    private String generateMembershipId() {
        String year = String.valueOf(Year.now().getValue());
        long count = memberRepository.count() + 1;
        String sequence = String.format("%05d", count);
        return "LIB-" + year + "-" + sequence;
    }
    
    /**
     * Retrieves all members.
     * 
     * @return List of all members
     */
    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
    
    /**
     * Retrieves a member by ID.
     * 
     * @param id The member ID (UUID)
     * @return The member
     * @throws ResourceNotFoundException if member not found
     */
    @Transactional(readOnly = true)
    public Member getMemberById(String id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
    }
    
    /**
     * Updates an existing member.
     * 
     * @param id The member ID
     * @param memberDetails The updated member details
     * @return The updated member
     */
    @Transactional
    public Member updateMember(String id, Member memberDetails) {
        Member existingMember = getMemberById(id);
        
        existingMember.setName(memberDetails.getName());
        existingMember.setEmail(memberDetails.getEmail());
        existingMember.setPhoneNumber(memberDetails.getPhoneNumber());
        
        return memberRepository.save(existingMember);
    }
    
    /**
     * Deactivates a member (soft delete).
     * 
     * @param id The member ID
     * @return true if deactivated
     */
    @Transactional
    public boolean deactivateMember(String id) {
        getMemberById(id); // Ensure exists
        return memberRepository.deactivate(id);
    }
    
    /**
     * Gets all active members.
     * 
     * @return List of active members
     */
    @Transactional(readOnly = true)
    public List<Member> getActiveMembers() {
       return memberRepository.findActiveMembers();
    }
    
    /**
     * Generates a large number of random members for testing.
     * 
     * @param count Number of members to generate
     * @return Number of members generated
     */
    @Transactional
    public int generateRandomMembers(int count) {
        List<Member> members = DataGenerator.generateMembers(count);
        //int generated = memberRepository.batchSave(members);
        for(Member member : members){
            memberRepository.save(member);
        }
        return (int)memberRepository.count();
    }
    
    /**
     * Gets total member count.
     * 
     * @return Total number of members
     */
    @Transactional(readOnly = true)
    public long getMemberCount() {
        return memberRepository.count();
    }
}