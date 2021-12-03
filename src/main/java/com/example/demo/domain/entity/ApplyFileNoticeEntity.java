package com.example.demo.domain.entity;

import com.example.demo.dao.MemberDao;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@DiscriminatorValue("file")
public class ApplyFileNoticeEntity extends ApplyResource{

    private String filePath;
    private String fileName;

    protected ApplyFileNoticeEntity(){

    }

    public static ApplyFileNoticeEntity createApplyFileNotice(String filePath, MemberDao memberDao, NoticeEntity noticeEntity, String fileName) {
        ApplyFileNoticeEntity afn = new ApplyFileNoticeEntity();
        afn.setFileName(fileName);
        afn.setFilePath(filePath);
        afn.setNoticeEntity(noticeEntity);
        afn.setMemberDao(memberDao);
        return afn;
    }

}
