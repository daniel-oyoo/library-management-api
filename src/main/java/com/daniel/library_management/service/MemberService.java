package com.daniel.library_management.service;

import com.daniel.library_management.model.Member;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MemberService {
    private Map<Long, Member> members = new HashMap<>();
    private Long nextId = 1L;
    
    // Register new member
    public Member registerMember(Member member) {
        member.setId(nextId++);
        member.setJoinDate(java.time.LocalDate.now());
        member.setBorrowedBooks(new ArrayList<>());
        member.setActive(true);
        members.put(member.getId(), member);
        return member;
    }
    
    // Get all members
    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }
    
    // Get member by ID
    public Member getMemberById(Long id) {
        return members.get(id);
    }
    
    // Update member
    public Member updateMember(Long id, Member memberDetails) {
        Member member = members.get(id);
        if (member != null) {
            member.setName(memberDetails.getName());
            member.setEmail(memberDetails.getEmail());
            member.setPhoneNumber(memberDetails.getPhoneNumber());
        }
        return member;
    }
    
    // Delete member (soft delete - mark as inactive)
    public boolean deactivateMember(Long id) {
        Member member = members.get(id);
        if (member != null) {
            member.setActive(false);
            return true;
        }
        return false;
    }
}