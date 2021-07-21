package com.example.demo.domain.repository;

import com.example.demo.domain.entity.MemberEntity;
import com.example.demo.domain.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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
    List<NoticeEntity> findByMemberEntity(MemberEntity memberEntity);
}
