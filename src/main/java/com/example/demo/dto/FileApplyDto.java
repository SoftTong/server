package com.example.demo.dto;

import com.example.demo.domain.entity.ApplyFile;
import com.example.demo.domain.entity.StatusName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class FileApplyDto {
    private String noticeTitle;
    private String noticeUrl;
    private String fileName;
    private String filePath;
    private StatusName status;
    private Long memberId;
    private String memberName;
    private String userId;
    private Timestamp uploadDay;
    private String dtype;
    private Long applyId;


    public FileApplyDto(ApplyFile apply){
        this.noticeTitle = apply.getNoticeEntity().getName();
        this.noticeUrl = apply.getNoticeEntity().getSwurl();
        this.fileName=apply.getFileName();
        this.filePath=apply.getFilePath();
        this.status = apply.getStatus();
        this.memberId = apply.getMemberDao().getId();
        this.memberName =apply.getMemberDao().getName();
        this.userId = apply.getMemberDao().getUserId();
        this.uploadDay = apply.getUploadDay();
        this.dtype = "file";
        this.applyId = apply.getId();
    }
}
