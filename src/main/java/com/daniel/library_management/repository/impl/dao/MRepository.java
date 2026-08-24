package com.daniel.library_management.repository.impl.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daniel.library_management.model.Member;

@Repository
public interface MRepository  extends JpaRepository<Member,String>{

    Member save(Member member);
    Optional<Member> findById();
    Optional<Member> findByEmail(String email);
    List<Member> findAll();
    List<Member> findActiveMembers();
    boolean deactivate(String id);
    
}
