package com.example.demo.dto;


import com.example.demo.domain.entity.FileNotice;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;


@Getter @Setter
public class FileNoticeDto extends NoticeInfoDto{
    // 첨부파일 형식 공지사항 Dto
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
    private Boolean isForm;

    public FileNoticeDto() {

    }

    @Builder
    public FileNoticeDto(FileNotice noticeEntity, Boolean isForm) {
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
        this.isForm = isForm;
    }
}
