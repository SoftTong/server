package com.example.demo.domain.entity;

import com.example.demo.dao.MemberDao;
import com.example.demo.dto.FormNoticeDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class FormQuestion {

    @Id @GeneratedValue
    private Long id;

    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private FormNotice formNotice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberDao memberDao;

    protected FormQuestion() {

    }

    public FormQuestion(FormNotice formInfo, MemberDao user) {

        this.setDescription(formInfo.getDescription());
        this.setFormNotice(formInfo);
        this.setMemberDao(user);
    }
}
