package com.example.demo.dto;

import com.example.demo.domain.entity.ApplyResource;
import com.example.demo.domain.entity.StatusName;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ApplyListDto {

    private String noticeTitle;
    private Long noticeId;
    private StatusName status;
    private String userId;
    private Timestamp uploadDay;
    private Long applyId;

    public ApplyListDto(ApplyResource applyResource) {
        this.noticeTitle = applyResource.getNoticeEntity().getName();
        this.noticeId = applyResource.getNoticeEntity().getId();
        this.status = applyResource.getStatus();
        this.userId = applyResource.getMemberDao().getUserId();
        this.uploadDay = applyResource.getUploadDay();
        this.applyId = applyResource.getId();
    }
}
