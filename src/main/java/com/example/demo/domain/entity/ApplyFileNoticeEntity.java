package com.example.demo.domain.entity;

import com.example.demo.dao.MemberDao;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "apply_file")
public class ApplyFileNoticeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String filePath;

    //private Long memberId;

    //private Long fileNoticeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @BatchSize(size = 10)
    @JoinColumn(name = "member_id")
    private MemberDao memberDao;

    @ManyToOne(fetch = FetchType.EAGER)
    @BatchSize(size = 10)
    @JoinColumn(name = "notice_id")
    private NoticeEntity noticeEntity;

    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private StatusName status;

    protected ApplyFileNoticeEntity(){

    }

    public ApplyFileNoticeEntity(String filePath, MemberDao memberDao, NoticeEntity noticeEntity, String fileName) {
        this.filePath = filePath;
        this.memberDao = memberDao;
        this.noticeEntity = noticeEntity;
        this.fileName = fileName;
    }
}
