package com.example.demo.domain.repository;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    // 공지사항 이름으로 찾기
    Optional<NoticeEntity> findByName(String name);
    // 마감기한으로 찾기
    List<NoticeEntity> findByDestDay(Date destDay);
    // 작성자 아디로 찾기
    List<NoticeEntity> findByMemberDao(MemberDao memberDao);
    //id로 찾기
    Optional<NoticeEntity> findById(Long id);

    // 데이터 타입 찾기 (첨부파일 or 폼 형식)
    @Query(value = "select n.dtype from notice n where n.id = ?1", nativeQuery = true)
    Object findDtypeById(Long noticeId);
}
