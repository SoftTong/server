package com.example.demo.domain.repository;

import com.example.demo.domain.entity.NoticeLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface NoticeLikeRepository extends JpaRepository<NoticeLike,Long> {

  @Query("select l from NoticeLike l where l.userId = :userId and l.noticeId = :noticeId")
  Optional<NoticeLike> findByLike(@Param("userId") Long userId, @Param("noticeId") Long noticeId);

  @Modifying
  @Transactional
  @Query("delete from NoticeLike l where l.userId = :userId and l.noticeId = :noticeId")
  void deleteByLike(@Param("userId") Long userId, @Param("noticeId") Long noticeId);

  List<NoticeLike> findAllByUserId(Long userId);
}
