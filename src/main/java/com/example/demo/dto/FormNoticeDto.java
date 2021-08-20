package com.example.demo.dto;

import com.example.demo.domain.entity.FormNotice;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter @Setter
public class FormNoticeDto {
    // 폼 형식 공지사항 Dto
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
    private String description;
    private Boolean isForm;

    public FormNoticeDto() {

    }

    @Builder
    public FormNoticeDto(FormNotice noticeEntity, Boolean isForm) {
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
        this.description = noticeEntity.getDescription();
        this.isForm = isForm;
    }

}
