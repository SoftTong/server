package com.example.demo.service.apply;

import com.example.demo.domain.entity.ApplyFileNoticeEntity;
import com.example.demo.domain.repository.ApplyFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplyFileStatusService {

  private final ApplyFileRepository applyFileRepository;

  public Page<ApplyFileNoticeEntity> findMemberById(Long noticeId, Pageable page){

    return applyFileRepository.findMemberById(noticeId, page);
  }

}
