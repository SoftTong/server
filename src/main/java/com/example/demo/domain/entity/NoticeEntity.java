package com.example.demo.domain.entity;

import com.example.demo.dao.MemberDao;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.message.TimestampMessage;
import org.hibernate.annotations.CreationTimestamp;
import org.w3c.dom.Text;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "file_board")
public class NoticeEntity {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberDao memberDao;

    protected NoticeEntity() {
    }

    @Builder
    public NoticeEntity(MemberDao memberDao,String name, String swurl, String tag1, String tag2, String tag3, Date startDay, Date destDay) {
      this.name=name;
      this.swurl=swurl;
      this.tag1=tag1;
      this.tag2=tag2;
      this.tag3=tag3;
      this.startDay=startDay;
      this.destDay=destDay;
      this.memberDao=memberDao;
    }

}
