package com.example.demo.service;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.FileNotice;
import com.example.demo.domain.entity.FormNotice;
import com.example.demo.domain.repository.NoticeRepository;
import com.example.demo.dto.FormNoticeDto;
import com.example.demo.dto.FileNoticeDto;

import javax.transaction.Transactional;

import lombok.AllArgsConstructor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
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

    // Proxy 객체에서 실제 구현체 값을 가져오는 메서드 -> find방법으로 수정함
    public FileNotice getFileNotice(Long noticeNum) {
        //HibernateProxy hibernateProxy = (HibernateProxy) noticeRepository.getById(noticeNum);
        //LazyInitializer initializer = hibernateProxy.getHibernateLazyInitializer();
        //FileNotice fileNotice = (FileNotice) initializer.getImplementation();
        FileNotice fileNotice = (FileNotice) noticeRepository.findById(noticeNum).get();
        return fileNotice;
    }

    // Proxy 객체에서 실제 구현체 값을 가져오는 메서드 -> find방법으로 수정함
    public FormNotice getFormNotice(Long noticeNum) {
        //HibernateProxy hibernateProxy = (HibernateProxy) noticeRepository.getById(noticeNum);
        //LazyInitializer initializer = hibernateProxy.getHibernateLazyInitializer();
        //FormNotice formNotice = (FormNotice) initializer.getImplementation();
        FormNotice formNotice = (FormNotice) noticeRepository.findById(noticeNum).get();
        return formNotice;
    }

}
