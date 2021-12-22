package com.example.demo.domain.entity;

import com.example.demo.dao.MemberDao;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@DiscriminatorValue("file")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ApplyFile extends ApplyResource{

    private String filePath;
    private String fileName;

    protected ApplyFile(){

    }

    public ApplyFile(String filePath, String fileName, MemberDao memberDao, FileNotice fileNotice) {
        this.setMemberDao(memberDao);
        this.setNoticeEntity(fileNotice);
        this.setStatus(StatusName.wait);
        this.setFilePath(filePath);
        this.setFileName(fileName);
    }

    public static ApplyFile createApplyFileNotice(String filePath, MemberDao memberDao, NoticeEntity noticeEntity, String fileName) {
        ApplyFile afn = new ApplyFile();
        afn.setFileName(fileName);
        afn.setFilePath(filePath);
        afn.setNoticeEntity(noticeEntity);
        afn.setMemberDao(memberDao);
        return afn;
    }

}
