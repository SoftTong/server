package com.example.demo.domain.repository;

import com.example.demo.domain.entity.FormNotice;
import com.example.demo.domain.entity.FormQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormQuestionRepository extends JpaRepository<FormQuestion, Long> {
    //id로 찾기
    Optional<FormQuestion> findById(Long id);

    //noticeId로 찾기
    Optional<FormQuestion> findByFormNotice(FormNotice formNotice);
}
