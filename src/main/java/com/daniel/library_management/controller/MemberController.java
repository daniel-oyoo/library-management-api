package com.daniel.library_management.controller;

import com.daniel.library_management.model.Member;
import com.daniel.library_management.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping({"/api/v1/members", "/api/members"})
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
    public ResponseEntity<Member> getMember(@PathVariable String id) {

        Member member = memberService.getMemberById(id);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
       
        return ResponseEntity.ok(member);
    }
    
    // POST /members
    @PostMapping
    public ResponseEntity<Member> registerMember(@Valid @RequestBody Member member) {
        Member newMember = memberService.registerMember(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMember);
    }
    
    // PUT /members/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable String id, @Valid @RequestBody Member memberDetails) {
        
        Member updatedMember = memberService.updateMember(id, memberDetails);
        if (updatedMember == null) {
            return ResponseEntity.notFound().build();
        }
     
        return ResponseEntity.ok(updatedMember);
    }
    
    // DELETE /members/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateMember(@PathVariable String id) {
        boolean deactivated = memberService.deactivateMember(id);
        if (!deactivated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
    
}