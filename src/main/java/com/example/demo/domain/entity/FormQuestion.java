package com.example.demo.domain.entity;

import com.example.demo.dao.MemberDao;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class FormQuestion {

    @Id @GeneratedValue
    private Long id;

    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private FormNotice formNotice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberDao memberDao;
}
