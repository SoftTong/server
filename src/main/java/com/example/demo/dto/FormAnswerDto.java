package com.example.demo.dto;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.FormQuestion;
import lombok.Getter;

import javax.persistence.*;

@Getter
public class FormAnswerDto {

    private String content;

}
