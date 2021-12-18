package com.example.demo.domain.repository;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ApplyResourceRepository extends JpaRepository<ApplyResource, Long> {

    Page<ApplyResource> findAllByMemberDao(MemberDao memberDao, Pageable pageable);

}
