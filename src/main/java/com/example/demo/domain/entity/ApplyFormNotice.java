package com.example.demo.domain.entity;

import com.example.demo.dao.MemberDao;
import com.example.demo.dto.FileNoticeDto;
import com.example.demo.dto.FormAnswerDto;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Setter
@Getter
@Entity
@DiscriminatorValue("form")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ApplyFormNotice extends ApplyResource {
    private String content;

    protected ApplyFormNotice() {

    }

    public ApplyFormNotice (FormAnswerDto formAnswerDto, MemberDao memberDao, FormNotice formNotice) {
        this.setMemberDao(memberDao);
        this.setNoticeEntity(formNotice);
        this.setStatus(StatusName.wait);
        this.setContent(formAnswerDto.getContent());
    }
}
