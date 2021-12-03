package com.example.demo.service.apply;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.MemberApply;
import com.example.demo.domain.repository.ApplyFileRepository;
import com.example.demo.domain.repository.MemberApplyRepository;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.member.MemberStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ApplyFileDeleteService {

  private final MemberApplyRepository memberApplyRepository;
  private final ApplyFileRepository applyFileRepository;
  private final MemberStatusService memberStatusService;

  //사용자가 지원한 지원 파일 삭제
  public Boolean removeApplyFile(HttpServletRequest request, @PathVariable Long applyId) {

    MemberDao currentMember = memberStatusService.findMember(request).get();

    Optional<MemberApply> apply = memberApplyRepository.findByApplyId(applyId);

    if (apply.isPresent()) {
      if (currentMember.getId().equals(apply.get().getMemberId())) {
        applyFileRepository.deleteById(applyId);
        memberApplyRepository.deleteById(apply.get().getId());
        return true;
      } else {
        log.info("wrong");
        return false;
      }
    }
    return false;
  }
}
