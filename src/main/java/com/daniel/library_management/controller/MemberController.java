package com.daniel.library_management.controller;

import com.daniel.library_management.model.Member;
import com.daniel.library_management.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    
    @Autowired
    private MemberService memberService;
    
    // GET /members - View all members
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }
    
    // GET /members/{id} - View single member
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        Member member = new Member();
        /* 
        //Member member = memberService.getMemberById(id);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        */
        return ResponseEntity.ok(member);
    }
    
    // POST /members - Register new member (your /members/register)
    @PostMapping
    public ResponseEntity<Member> registerMember(@RequestBody Member member) {
        Member newMember = memberService.registerMember(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMember);
    }
    
    // PUT /members/{id} - Update member (your /members/edit/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member memberDetails) {
        Member updatedMember = new Member();
        /* 
        //Member updatedMember = memberService.updateMember(id, memberDetails);
        if (updatedMember == null) {
            return ResponseEntity.notFound().build();
        }
        */
        return ResponseEntity.ok(updatedMember);
    }
    
    // DELETE /members/{id} - Deactivate member (your /members/delete/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateMember(@PathVariable Long id) {
        boolean deactivated=true;
        //boolean deactivated = memberService.deactivateMember(id);
        if (!deactivated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
    
    // POST /members/login - Simple login simulation
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email) {
        // Simple authentication (in real app, use proper authentication)
        return ResponseEntity.ok("Login successful for: " + email);
    }
}