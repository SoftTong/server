package com.example.demo.domain.entity;

import com.example.demo.dao.MemberDao;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class ApplyResource {

    @Id @GeneratedValue
    private Long id;

    @CreationTimestamp
    private Timestamp uploadDay;

    @ManyToOne(fetch = FetchType.LAZY)
//    @BatchSize(size = 10)
    @JoinColumn(name = "member_id")
    private MemberDao memberDao;

    @ManyToOne(fetch = FetchType.EAGER)
    @BatchSize(size = 10)
    @JoinColumn(name = "notice_id")
    private NoticeEntity noticeEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusName status;

}
