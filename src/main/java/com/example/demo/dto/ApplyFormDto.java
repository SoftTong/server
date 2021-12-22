package com.example.demo.dto;

import com.example.demo.domain.entity.ApplyForm;
import com.example.demo.domain.entity.StatusName;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ApplyFormDto {

    private String noticeTitle;
    private String noticeUrl;
    private String content;
    private StatusName status;
    private String memberName;
    private String userId;
    private Timestamp uploadDay;
    private Long noticeId;
    private Long applyId;
    private Boolean isForm;

    public ApplyFormDto(ApplyForm applyResource) {
        this.noticeTitle = applyResource.getNoticeEntity().getName();
        this.noticeUrl = applyResource.getNoticeEntity().getSwurl();
        this.content = applyResource.getContent();
        this.status = applyResource.getStatus();
        this.memberName = applyResource.getMemberDao().getName();
        this.userId = applyResource.getMemberDao().getUserId();
        this.uploadDay = applyResource.getUploadDay();
        this.noticeId = applyResource.getNoticeEntity().getId();
        this.applyId = applyResource.getId();
        this.isForm = true;
    }

}
