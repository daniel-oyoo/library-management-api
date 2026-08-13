package com.daniel.library_management.repository.impl.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daniel.library_management.model.Member;

@Repository
public interface MRepository  extends JpaRepository<Member,String>{

    /*int batchSave(List<Member> members);*/

    /*List<Member> findActiveMembers();

    boolean deactivate(String id);

    Member update(Member existingMember);
*/
    Optional<Member> findByEmail(String email);
    
}
