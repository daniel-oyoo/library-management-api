package com.daniel.library_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daniel.library_management.model.Member;

public interface MemberRepsoitory extends JpaRepository<Member,String>{

    Optional<Member> findByEmail(String email);

    @Query(value ="UPDATE  members SET active = false WHERE id = id ",nativeQuery=true)
    int deactivate(@Param("id")String id);

    @Query(value ="SELECT * FROM members WHERE active = true ",nativeQuery=true)
    List<Member> findActiveMembers();}