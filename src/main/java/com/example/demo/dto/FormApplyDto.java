package com.example.demo.dto;

import com.example.demo.domain.entity.ApplyForm;
import com.example.demo.domain.entity.StatusName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class FormApplyDto {
    private String noticeTitle;
    private String noticeUrl;
    private StatusName status;
    private Long memberId;
    private String memberName;
    private String userId;
    private Timestamp uploadDay;
    private String dtype;
    private Long applyId;

    private String question;
    private String answer;

    public FormApplyDto(ApplyForm apply){
        this.noticeTitle = apply.getNoticeEntity().getName();
        this.noticeUrl = apply.getNoticeEntity().getSwurl();
        this.status = apply.getStatus();
        this.memberId = apply.getMemberDao().getId();
        this.memberName =apply.getMemberDao().getName();
        this.userId = apply.getMemberDao().getUserId();
        this.uploadDay = apply.getUploadDay();
        this.dtype = "form";
        this.applyId = apply.getId();
        this.answer = apply.getContent();
    }
}
