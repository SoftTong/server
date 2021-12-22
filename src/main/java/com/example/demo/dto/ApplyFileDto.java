package com.example.demo.dto;

import com.example.demo.domain.entity.ApplyFile;
import com.example.demo.domain.entity.StatusName;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ApplyFileDto {

    private String noticeTitle;
    private String noticeUrl;
    private String fileName;
    private String filePath;
    private StatusName status;
    private String memberName;
    private String userId;
    private Timestamp uploadDay;
    private Long noticeId;
    private Long applyId;

    public ApplyFileDto(ApplyFile applyResource) {
        this.noticeTitle = applyResource.getNoticeEntity().getName();
        this.noticeUrl = applyResource.getNoticeEntity().getSwurl();
        this.fileName = applyResource.getFileName();
        this.filePath = applyResource.getFilePath();
        this.status = applyResource.getStatus();
        this.memberName = applyResource.getMemberDao().getName();
        this.userId = applyResource.getMemberDao().getUserId();
        this.uploadDay = applyResource.getUploadDay();
        this.noticeId = applyResource.getNoticeEntity().getId();
        this.applyId = applyResource.getId();
    }

}
