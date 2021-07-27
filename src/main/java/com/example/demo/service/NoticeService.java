package com.example.demo.service;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.NoticeEntity;
import com.example.demo.domain.repository.NoticeRepository;
import com.example.demo.dto.MemberDto;
import com.example.demo.dto.NoticeDto;

import javax.transaction.Transactional;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NoticeService {
    private NoticeRepository noticeRepository;


    @Transactional
    public NoticeDto newPostBoard(MemberDao user, NoticeDto noticeInfo){

        NoticeEntity temp = noticeRepository.save(NoticeEntity.builder()
                .name(noticeInfo.getName())
                .content(noticeInfo.getContent())
                .tag1(noticeInfo.getTag1())
                .tag2(noticeInfo.getTag2())
                .tag3(noticeInfo.getTag3())
                .startDay(noticeInfo.getStartDay())
                .destDay(noticeInfo.getDestDay())
                .memberDao(user)
                .build());

        return noticeInfo;
    }


}
