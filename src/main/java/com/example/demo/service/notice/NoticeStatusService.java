package com.example.demo.service.notice;

import com.example.demo.controller.notice.NoticeType;
import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.*;
import com.example.demo.domain.repository.ApplyFileRepository;
import com.example.demo.domain.repository.MemberApplyRepository;
import com.example.demo.domain.repository.NoticeRepository;
import com.example.demo.dto.FileNoticeDto;
import com.example.demo.dto.FormNoticeDto;
import com.example.demo.dto.NoticeInfoDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class NoticeStatusService {


  private final NoticeRepository noticeRepository;
  private final ApplyFileRepository applyFileRepository;
  private final MemberApplyRepository memberApplyRepository;

  public NoticeEntity findById(Long noticeId) {
    Optional<NoticeEntity> findNotice = noticeRepository.findById(noticeId);
    if(findNotice.isEmpty()) {
      throw new IllegalStateException("존재하지 않는 공지사항입니다.");
    }
    return findNotice.get();
  }

  public List<NoticeEntity> findByMember(MemberDao currentUser){

    return noticeRepository.findByMemberDao(currentUser);
  }

  public Page<NoticeInfoDto> findAllByPagination(int pageNum) {
    Pageable pageable = PageRequest.of(pageNum, 10, Sort.by("uploadDay").descending());
    Page<NoticeEntity> noticeEntityPages = noticeRepository.findAll(pageable);
    List<NoticeInfoDto> collect = noticeEntityPages.stream().map(nep -> new NoticeInfoDto(nep)).collect((toList()));
    return new PageImpl<>(collect, pageable, noticeEntityPages.getTotalElements());
  }

  public Page<NoticeInfoDto> findByTitle(int pageNum, String searchWord) {
    Pageable pageable = PageRequest.of(pageNum, 10, Sort.by("uploadDay").descending());
    Page<NoticeEntity> noticeEntityPages = noticeRepository.findByNameContaining(searchWord, pageable);
    List<NoticeInfoDto> noticeInfoDtoList = noticeEntityPages.stream().map(nep -> new NoticeInfoDto(nep)).collect((toList()));
    return new PageImpl<>(noticeInfoDtoList, pageable, noticeEntityPages.getTotalElements());
  }

  public Page<NoticeInfoDto> findByAuthor(int pageNum, List<MemberDao> members) {
    if (members.size() == 1) {
      Pageable pageable = PageRequest.of(pageNum, 10, Sort.by("uploadDay").descending());
      Page<NoticeEntity> noticeEntityPages = noticeRepository.findByMemberDao(members.get(0), pageable);
      List<NoticeInfoDto> noticeInfoDtoList = noticeEntityPages.stream().map(nep -> new NoticeInfoDto(nep)).collect((toList()));
      return new PageImpl<>(noticeInfoDtoList, pageable, noticeEntityPages.getTotalElements());
    } else {
      List<NoticeInfoDto> noticeInfoDtoList = new ArrayList<>();
      Pageable page = PageRequest.of(pageNum, 10, Sort.by("uploadDay").descending());
      int totalElements = 0;
      for (MemberDao member : members) {
        Page<NoticeEntity> nep = noticeRepository.findByMemberDao(member, page);
        totalElements += nep.getTotalElements();
        List<NoticeInfoDto> collect = nep.stream().map(n -> new NoticeInfoDto(n)).collect((toList()));
        noticeInfoDtoList.addAll(collect);
      }
      return new PageImpl<>(noticeInfoDtoList, page, totalElements);
    }

  }

  public Page<NoticeEntity> findAllByMemberDao(MemberDao currentUser, Pageable page){

    return noticeRepository.findAllByMemberDao(currentUser,page);
  }

  public Object findDtypeById(Long noticeId){

    return noticeRepository.findDtypeById(noticeId);
  }

  public NoticeType<? extends NoticeInfoDto> getFileNotice(Long noticeNum) {
    FileNotice fileNotice = (FileNotice) noticeRepository.findById(noticeNum).get();
    FileNoticeDto fileNoticeDto = new FileNoticeDto(fileNotice, Boolean.FALSE); // File 타입이므로 False를 같이 넘김
    NoticeType<FileNoticeDto> noticeType = new NoticeType<>();
    noticeType.setNotice(fileNoticeDto);
    return noticeType;
  }

  public NoticeType<? extends NoticeInfoDto> getFormNotice(Long noticeNum) {
    FormNotice formNotice = (FormNotice) noticeRepository.findById(noticeNum).get();
    FormNoticeDto formNoticeDto = new FormNoticeDto(formNotice, Boolean.TRUE); // Form 타입이므로 True를 같이 넘김
    NoticeType<FormNoticeDto> noticeType = new NoticeType<>();
    noticeType.setNotice(formNoticeDto);
    return noticeType;
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
