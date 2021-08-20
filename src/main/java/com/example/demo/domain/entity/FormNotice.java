package com.example.demo.domain.entity;

import com.example.demo.dao.MemberDao;
import com.example.demo.dto.FormNoticeDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter @Setter
@DiscriminatorValue("form")
public class FormNotice extends NoticeEntity{

    private String description;

    // 생성자 대신 FormNotice를 만들 때 사용하는 메서드

    /**
     * 생성자 대신 FormNotice를 만들 때 사용하는 메서드
     * @param noticeDto 폼 형식으로 들어온 Dto
     * @param user 공지사항 작성자
     * @return FormNotice
     */
    public static FormNotice createFormNotice(FormNoticeDto noticeDto, MemberDao user) {
        FormNotice formNotice = new FormNotice();
        formNotice.setDescription(noticeDto.getDescription());
        formNotice.setName(noticeDto.getName());
        formNotice.setSwurl(noticeDto.getSwurl());
        formNotice.setTag1(noticeDto.getTag1());
        formNotice.setTag2(noticeDto.getTag2());
        formNotice.setTag3(noticeDto.getTag3());
        formNotice.setUploadDay(noticeDto.getUploadDay());
        formNotice.setStartDay(noticeDto.getStartDay());
        formNotice.setDestDay(noticeDto.getDestDay());
        formNotice.setViewCount(noticeDto.getViewCount());
        formNotice.setMemberDao(user);
        return  formNotice;
    }
}
