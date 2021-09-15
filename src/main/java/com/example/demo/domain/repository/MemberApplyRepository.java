package com.example.demo.domain.repository;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.MemberApply;
import com.example.demo.domain.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberApplyRepository extends JpaRepository<MemberApply, Long> {
        //사용자 ID로 찾기
        List<MemberApply> findAllByMemberId(Long member_id);
        Page<MemberApply> findAllByMemberId(Long member_id, Pageable pageable);
}