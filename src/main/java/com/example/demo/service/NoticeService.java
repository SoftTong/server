package com.example.demo.service;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.FileNotice;
import com.example.demo.domain.entity.FormNotice;
import com.example.demo.domain.repository.NoticeRepository;
import com.example.demo.dto.FormNoticeDto;
import com.example.demo.dto.FileNoticeDto;

import javax.transaction.Transactional;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NoticeService {
    private NoticeRepository noticeRepository;

    // 첨부 파일 형식 공지사항 작성
    @Transactional
    public void makeFileNotice(MemberDao user, FileNoticeDto noticeInfo){
        noticeRepository.save(FileNotice.createFileNotice(noticeInfo, user));
    }

    // 폼 형식 공지사항 작성
    @Transactional
    public void makeFormNotice(MemberDao user, FormNoticeDto noticeInfo){
        noticeRepository.save(FormNotice.createFormNotice(noticeInfo, user));
    }

}
