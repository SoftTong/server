package com.example.demo.service.apply;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.MemberApply;
import com.example.demo.domain.repository.ApplyFileRepository;
import com.example.demo.domain.repository.MemberApplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ApplyFileDeleteService {

  private final MemberApplyRepository memberApplyRepository;
  private final ApplyFileRepository applyFileRepository;

  public boolean deleteApplyFiles(MemberDao member, Long applyId){
//  memberApplyRepository.findAllByMemberId(member.getId());
    Optional<MemberApply> apply = memberApplyRepository.findByApplyId(applyId);

    if (apply.isPresent()) {
      if (member.getId().equals(apply.get().getMemberId())) {
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
