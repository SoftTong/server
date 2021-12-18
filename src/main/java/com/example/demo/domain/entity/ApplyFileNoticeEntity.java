package com.example.demo.domain.entity;

import com.example.demo.dao.MemberDao;
import com.example.demo.dto.FormAnswerDto;
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

    public ApplyFileNoticeEntity (String filePath, String fileName, MemberDao memberDao, FileNotice fileNotice) {
        this.setMemberDao(memberDao);
        this.setNoticeEntity(fileNotice);
        this.setStatus(StatusName.wait);
        this.setFilePath(filePath);
        this.setFileName(fileName);
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
