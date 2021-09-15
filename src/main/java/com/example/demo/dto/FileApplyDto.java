package com.example.demo.dto;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyFileNoticeEntity;
import com.example.demo.domain.entity.StatusName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.lang.reflect.Member;
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


    public FileApplyDto(ApplyFileNoticeEntity apply){
        this.noticeTitle = apply.getNoticeEntity().getName();
        this.noticeUrl = apply.getNoticeEntity().getSwurl();
        this.fileName=apply.getFileName();
        this.filePath=apply.getFilePath();
        this.status = apply.getStatus();
        this.memberId = apply.getMemberDao().getId();
        this.memberName =apply.getMemberDao().getName();
        this.userId = apply.getMemberDao().getUserId();
        this.uploadDay = apply.getUploadDay();
    }
}
