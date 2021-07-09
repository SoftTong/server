package com.example.demo.domain.repository;

import com.example.demo.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmail(String email);
    //Optional<MemberEntity> findByUserId(String user_id);
    //Optional<MemberEntity> findByUserId(String user_id);
}
