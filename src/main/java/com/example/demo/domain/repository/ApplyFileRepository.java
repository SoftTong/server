package com.example.demo.domain.repository;

import com.example.demo.domain.entity.ApplyFileNoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyFileRepository extends JpaRepository<ApplyFileNoticeEntity, Long> {
}
