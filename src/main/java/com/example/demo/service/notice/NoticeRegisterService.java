package com.example.demo.service.notice;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.FileNotice;
import com.example.demo.domain.entity.FormNotice;
import com.example.demo.domain.entity.FormQuestion;
import com.example.demo.domain.repository.FormQuestionRepository;
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
  private final FormQuestionRepository formQuestionRepository;

  public void makeFileNotice(MemberDao user, FileNoticeDto noticeInfo){
    noticeRepository.save(FileNotice.createFileNotice(noticeInfo, user));
  }

  public void makeFormNotice(MemberDao user, FormNoticeDto noticeInfo){

    FormNotice formNotice = FormNotice.createFormNotice(noticeInfo, user);

    //FormNotice 등록
    noticeRepository.save(formNotice);

    //FormQuestion 등록
    formQuestionRepository.save(new FormQuestion(formNotice, user));
  }



}
