package com.example.demo.domain.entity;

import com.example.demo.dao.MemberDao;
import com.example.demo.dto.FormAnswerDto;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class FormAnswer {

    @Id @GeneratedValue
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_question_id")
    private FormQuestion formQuestion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberDao memberDao;

    protected FormAnswer() {

    }

    public FormAnswer(FormQuestion formQuestion, MemberDao user, FormAnswerDto formAnswerDto) {
        this.formQuestion = formQuestion;
        this.memberDao = user;
        this.content = formAnswerDto.getContent();
    }
}
