package com.example.demo.domain.entity;

import com.example.demo.dao.MemberDao;
import com.example.demo.dto.FileNoticeDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter @Setter
@DiscriminatorValue("file")
public class FileNotice extends NoticeEntity{
    // 첨부 파일 형식 공지사항 엔티티

    /**
     * 생성자 대신 FileNotice 만들 때 사용하는 메서드
     * @param noticeDto 폼 형식으로 들어온 Dto
     * @param user 공지사항 작성자
     * @return FileNotice
     */
    public static FileNotice createFileNotice(FileNoticeDto noticeDto, MemberDao user) {
        FileNotice fileNotice = new FileNotice();
        fileNotice.setName(noticeDto.getName());
        fileNotice.setSwurl(noticeDto.getSwurl());
        fileNotice.setTag1(noticeDto.getTag1());
        fileNotice.setTag2(noticeDto.getTag2());
        fileNotice.setTag3(noticeDto.getTag3());
        fileNotice.setUploadDay(noticeDto.getUploadDay());
        fileNotice.setStartDay(noticeDto.getStartDay());
        fileNotice.setDestDay(noticeDto.getDestDay());
        fileNotice.setViewCount(noticeDto.getViewCount());
        fileNotice.setMemberDao(user);
        return fileNotice;
    }
}
