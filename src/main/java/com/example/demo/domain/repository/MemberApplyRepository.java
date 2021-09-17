package com.example.demo.domain.repository;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.MemberApply;
import com.example.demo.domain.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberApplyRepository extends JpaRepository<MemberApply, Long> {
        //사용자 ID로 찾기
        List<MemberApply> findAllByMemberId(Long member_id);
        Page<MemberApply> findAllByMemberId(Long member_id, Pageable pageable);
        Optional<MemberApply> findByApplyId(Long applyId);

        @Query(value = "select * from member_apply m where m.notice_id = ?1 and m.member_id=?2", nativeQuery = true)
        Optional<MemberApply> findByNoticeWithMember(Long noticeId,  Long memberId);
}