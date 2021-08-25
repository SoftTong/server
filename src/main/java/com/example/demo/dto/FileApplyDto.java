package com.example.demo.dto;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyFileNoticeEntity;
import com.example.demo.domain.entity.StatusName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Member;

@Getter
@Setter
@ToString
public class FileApplyDto {
    private String fileName;
    private String filePath;
    private StatusName status;
    private Long memberId;
    private String memberName;
    private String userId;


    public FileApplyDto(ApplyFileNoticeEntity apply){
        this.fileName=apply.getFileName();
        this.filePath=apply.getFilePath();
        this.status = apply.getStatus();
        this.memberId = apply.getMemberDao().getId();
        this.memberName =apply.getMemberDao().getName();
        this.userId = apply.getMemberDao().getUserId();
    }
}
