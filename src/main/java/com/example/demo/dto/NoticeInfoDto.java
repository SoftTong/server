package com.example.demo.dto;

import com.example.demo.domain.entity.FileNotice;
import com.example.demo.domain.entity.FormNotice;
import com.example.demo.domain.entity.NoticeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
public class NoticeInfoDto {
    private Long id;
    private String name;
    private String swurl;
    private String tag1;
    private String tag2;
    private String tag3;
    private Timestamp uploadDay;
    private Date startDay;
    private Date destDay;
    private int viewCount;
    private String authorName;
    private Long likeCount;
    private String dtype;

    public NoticeInfoDto() {

    }


    public NoticeInfoDto(NoticeEntity noticeEntity) {
        this.id = noticeEntity.getId();
        this.name = noticeEntity.getName();
        this.swurl = noticeEntity.getSwurl();
        this.tag1 = noticeEntity.getTag1();
        this.tag2 = noticeEntity.getTag2();
        this.tag3 = noticeEntity.getTag3();
        this.uploadDay = noticeEntity.getUploadDay();
        this.startDay = noticeEntity.getStartDay();
        this.destDay = noticeEntity.getDestDay();
        this.viewCount = noticeEntity.getViewCount();
        this.authorName = noticeEntity.getMemberDao().getName();
        this.likeCount = noticeEntity.getLikeCount();
    }

    public NoticeInfoDto(NoticeEntity noticeEntity, String dtype) {
        this.id = noticeEntity.getId();
        this.name = noticeEntity.getName();
        this.swurl = noticeEntity.getSwurl();
        this.tag1 = noticeEntity.getTag1();
        this.tag2 = noticeEntity.getTag2();
        this.tag3 = noticeEntity.getTag3();
        this.uploadDay = noticeEntity.getUploadDay();
        this.startDay = noticeEntity.getStartDay();
        this.destDay = noticeEntity.getDestDay();
        this.viewCount = noticeEntity.getViewCount();
        this.authorName = noticeEntity.getMemberDao().getName();
        this.likeCount = noticeEntity.getLikeCount();
        this.dtype = dtype;
    }
}
