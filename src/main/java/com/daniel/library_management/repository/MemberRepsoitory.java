package com.daniel.library_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.daniel.library_management.model.Member;

public interface MemberRepsoitory extends JpaRepository<Member,String>{

    Optional<Member> findByEmail(String email);

    @Modifying
    @Query("update Member m set m.active = false where m.id = :id")
    int deactivate(@Param("id")String id);

    @Query("select m from Member m where m.active = true")
    List<Member> findActiveMembers();}