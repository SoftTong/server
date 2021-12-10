package com.example.demo.domain.repository;

import com.example.demo.domain.entity.FormAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormAnswerRepository extends JpaRepository<FormAnswer, Long> {
    //id로 찾기
    Optional<FormAnswer> findById(Long id);
}