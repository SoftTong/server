package com.example.demo.service.apply;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyFileNoticeEntity;
import com.example.demo.domain.entity.MemberApply;
import com.example.demo.domain.repository.ApplyFileRepository;
import com.example.demo.domain.repository.MemberApplyRepository;
import com.example.demo.domain.repository.MemberRepository;
import com.example.demo.domain.repository.NoticeRepository;
import com.example.demo.dto.ApplyDto;
import com.example.demo.dto.FileApplyDto;
import com.example.demo.service.member.MemberStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplyFileStatusService {

  private final ApplyFileRepository applyFileRepository;
  private final MemberStatusService memberStatusService;
  private final MemberApplyRepository memberApplyRepository;
  private final MemberRepository memberRepository;
  private final NoticeRepository noticeRepository;

  public Page<ApplyFileNoticeEntity> findMemberById(Long noticeId, Pageable page){

    return applyFileRepository.findMemberById(noticeId, page);
  }

  public Page<ApplyDto> findApply(HttpServletRequest request, @PathVariable int pageNum) {

    Pageable page = PageRequest.of(pageNum, 10, Sort.by("id").descending());
    MemberDao currentMember = memberStatusService.findMember(request).get();
    Page<MemberApply> memberApplyPages = memberApplyRepository.findAllByMemberId(currentMember.getId(), page);
    List<ApplyDto> ApplyDtoList = memberApplyPages.stream().map(p-> new ApplyDto(p, memberRepository, noticeRepository, applyFileRepository)).collect((toList()));

    return new PageImpl<>(ApplyDtoList, page, memberApplyPages.getTotalElements());
  }

  //사용자가 지원한 지원 파일 정보
  public FileApplyDto findapplyFile(HttpServletRequest request, @PathVariable Long applyId) {

    Optional<ApplyFileNoticeEntity> applyFileNoticeEntity = applyFileRepository.findById(applyId);
    return new FileApplyDto(applyFileNoticeEntity.get());
  }

}
