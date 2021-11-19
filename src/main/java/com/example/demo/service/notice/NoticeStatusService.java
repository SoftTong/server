package com.example.demo.service.notice;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.*;
import com.example.demo.domain.repository.ApplyFileRepository;
import com.example.demo.domain.repository.MemberApplyRepository;
import com.example.demo.domain.repository.NoticeRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class NoticeStatusService {


  private final NoticeRepository noticeRepository;
  private final ApplyFileRepository applyFileRepository;
  private final MemberApplyRepository memberApplyRepository;


  public List<NoticeEntity> findByMember(MemberDao currentUser){

    return noticeRepository.findByMemberDao(currentUser);
  }

  public Page<NoticeEntity> findAllByMemberDao(MemberDao currentUser, Pageable page){

    return noticeRepository.findAllByMemberDao(currentUser,page);
  }

  public Object findDtypeById(Long noticeId){

    return noticeRepository.findDtypeById(noticeId);
  }

  public FileNotice getFileNotice(Long noticeNum) {
    //HibernateProxy hibernateProxy = (HibernateProxy) noticeRepository.getById(noticeNum);
    //LazyInitializer initializer = hibernateProxy.getHibernateLazyInitializer();
    //FileNotice fileNotice = (FileNotice) initializer.getImplementation();
    FileNotice fileNotice = (FileNotice) noticeRepository.findById(noticeNum).get();
    return fileNotice;
  }

  public FormNotice getFormNotice(Long noticeNum) {
    //HibernateProxy hibernateProxy = (HibernateProxy) noticeRepository.getById(noticeNum);
    //LazyInitializer initializer = hibernateProxy.getHibernateLazyInitializer();
    //FormNotice formNotice = (FormNotice) initializer.getImplementation();
    FormNotice formNotice = (FormNotice) noticeRepository.findById(noticeNum).get();
    return formNotice;
  }

  public ApplyFileNoticeEntity makeApplyFileNotice(String filePath, MemberDao memberDao, NoticeEntity noticeEntity, String fileName){
    ApplyFileNoticeEntity applyFileNotice = new ApplyFileNoticeEntity(filePath, memberDao, noticeEntity, fileName);
    applyFileNotice.setStatus(StatusName.wait); //처음은 대기 상태로 저장
    ApplyFileNoticeEntity savedApplyFileNotice = applyFileRepository.save(applyFileNotice);

    MemberApply memberApply = new MemberApply(memberDao.getId(), noticeEntity.getId(), savedApplyFileNotice.getId(),"file");
    memberApplyRepository.save(memberApply);
    return applyFileNotice;
  }




}
