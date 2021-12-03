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
import com.example.demo.service.notice.NoticeStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplyFileStatusService {

  private final ApplyFileRepository applyFileRepository;
  private final MemberApplyRepository memberApplyRepository;
  private final MemberRepository memberRepository;
  private final NoticeRepository noticeRepository;
  private final MemberStatusService memberStatusService;
  private final NoticeStatusService noticeStatusService;

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
  public FileApplyDto findApplyFileByApplyId(HttpServletRequest request, @PathVariable Long applyId) {

    Optional<ApplyFileNoticeEntity> applyFileNoticeEntity = applyFileRepository.findById(applyId);
    return new FileApplyDto(applyFileNoticeEntity.get());
  }

  //관리자가 작성한 게시물의 지원한 지원서 정보들 반환
  public PageImpl<Object> findApplyFileByNoticeId(@PathVariable Long noticeId, @PathVariable int pageNum) {

    Pageable page = PageRequest.of(pageNum, 10, Sort.by("member_id").ascending());
    String dtype = (String) noticeStatusService.findDtypeById(noticeId);

    if (dtype.equals("file")) {
      Page<ApplyFileNoticeEntity> applyPages = findMemberById(noticeId,page);
      List<FileApplyDto> fileApplyDtoList = applyPages.stream().map(a-> new FileApplyDto(a) ).collect((toList()));
      return new PageImpl(fileApplyDtoList, page, applyPages.getTotalElements());
    } else if (dtype.equals("form")) {
      throw new IllegalStateException("폼 형식은 아직 구현되지 않았습니다.");
    } else {
      throw new IllegalStateException("올바르지 않은 공지사항입니다.");
    }

  }

  //지원한 파일 다운로드
  public ResponseEntity<InputStreamResource> downloadApplyFile(String fileName) throws IOException {

    String path = "src/main/resources/menufiles/"+fileName;
    File file = new File(path);
    HttpHeaders header = new HttpHeaders();

    header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
    header.add("Cache-Control", "no-cache, no-store, must-revalidate");
    header.add("Pragma", "no-cache");
    header.add("Expires", "0");

    InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

    return ResponseEntity.ok() .headers(header) .contentLength(file.length()) .contentType(MediaType.parseMediaType("application/octet-stream")) .body(resource);

  }

}
