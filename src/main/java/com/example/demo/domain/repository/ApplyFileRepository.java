package com.example.demo.domain.repository;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyFileNoticeEntity;
import com.example.demo.domain.entity.FileNotice;
import com.example.demo.domain.entity.NoticeEntity;
import com.example.demo.dto.FileNoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplyFileRepository extends JpaRepository<ApplyFileNoticeEntity, Long> {

    @Query(value = "select * from apply_file n where n.notice_id = ?1",
            countQuery = "SELECT COUNT(*) FROM apply_file n where n.notice_id=?1",
            nativeQuery = true)
    Page<ApplyFileNoticeEntity> findMemberById(Long noticeId,Pageable pageable);
}
