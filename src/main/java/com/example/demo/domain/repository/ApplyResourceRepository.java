package com.example.demo.domain.repository;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyResource;
import com.example.demo.domain.entity.NoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface ApplyResourceRepository<T extends ApplyResource> extends JpaRepository<T, Long> {

    Page<T> findAllByMemberDao(MemberDao memberDao, Pageable pageable);

    @Query(value = "select a from ApplyResource a join fetch a.memberDao",
    countQuery = "select count(a) from ApplyResource a")
    Page<T> findAllByMemberDaoFetchJoin(MemberDao memberDao, Pageable pageable);

    @Query(value = "select * from apply_resource m where m.notice_id = ?1 and m.member_id=?2", nativeQuery = true)
    Optional<T> findByNoticeWithMember(NoticeEntity noticeEntity, MemberDao memberDao);

    Page<T> findAllByNoticeEntity(NoticeEntity noticeEntity, Pageable pageable);

    List<T> findAllByNoticeEntity(NoticeEntity noticeEntity);

    // 데이터 타입 찾기 (첨부파일 or 폼 형식)
    @Query(value = "select n.dtype from apply_resource n where n.id = ?1", nativeQuery = true)
    Object findDtypeById(Long applyId);

    //id로 찾기
    Optional<T> findById(Long id);

    @Transactional
    @Modifying
    @Query(value = "delete from ApplyResource a where a.id in :ids")
    void deleteAllByIdInQuery(@Param("ids") List<Long> ids);
}
