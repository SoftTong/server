package com.example.demo.domain.entity;

import com.example.demo.dao.MemberDao;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "notice")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class NoticeEntity {
    // 추상 클래스이기 때문에 빌더 x -> 폼 형식이면 FormNotice, 첨부 파일 형식이면 FileNotice 사용

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String swurl;

    private String tag1;
    private String tag2;
    private String tag3;

    @CreationTimestamp
    private Timestamp uploadDay;
    private Date startDay;
    private Date destDay;

    private int viewCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @BatchSize(size = 10)
    @JoinColumn(name = "member_id")
    private MemberDao memberDao;

    protected NoticeEntity() {

    }

}
