package com.example.demo.service.notice;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.FileNotice;
import com.example.demo.domain.entity.FormNotice;
import com.example.demo.domain.repository.NoticeRepository;
import com.example.demo.dto.FileNoticeDto;
import com.example.demo.dto.FormNoticeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NoticeRegisterService {

  private final NoticeRepository noticeRepository;

  public void makeFileNotice(MemberDao user, FileNoticeDto noticeInfo){
    noticeRepository.save(FileNotice.createFileNotice(noticeInfo, user));
  }

  public void makeFormNotice(MemberDao user, FormNoticeDto noticeInfo){
    noticeRepository.save(FormNotice.createFormNotice(noticeInfo, user));
  }



}
