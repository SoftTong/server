package com.example.demo.domain.repository;

import com.example.demo.dao.MemberDao;
//import com.example.demo.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberDao, Long> {
    Optional<MemberDao> findByEmail(String email);
    Optional<MemberDao> findByNameOrEmail(String name, String email);
    List<MemberDao> findAllByIdIn(List<Long> Ids);
    Optional<MemberDao> findByName(String name);
    Optional<MemberDao> findByUserId(String userId);

    Boolean existsByUserId(String userId);
    Boolean existsByEmail(String email);
}

