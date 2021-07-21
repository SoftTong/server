package com.example.demo.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.w3c.dom.Text;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter @Setter
@Table(name = "file_board")
public class NoticeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String content;

    private String tag1;
    private String tag2;
    private String tag3;

    @CreationTimestamp
    private Timestamp uploadDay;
    private Date startDay;
    private Date destDay;

    private int viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private MemberEntity memberEntity;
}
