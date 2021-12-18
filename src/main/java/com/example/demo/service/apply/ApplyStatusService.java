package com.example.demo.service.apply;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyResource;
import com.example.demo.domain.entity.MemberApply;
import com.example.demo.domain.repository.ApplyResourceRepository;
import com.example.demo.dto.ApplyDto;
import com.example.demo.service.member.MemberStatusService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ApplyStatusService {

    ApplyResourceRepository applyResourceRepository;
    MemberStatusService memberStatusService;

    public Page<ApplyResource> findApply(Authentication request, @PathVariable int pageNum) {

        Pageable page = PageRequest.of(pageNum, 10, Sort.by("id").descending());
        log.info("log1");
        log.info("request = {}",request.getPrincipal());
        
        MemberDao currentMember = memberStatusService.findMember(request).get();
        log.info("currentMember = {}",currentMember);
        Page<ApplyResource> applyResourcePage = applyResourceRepository.findAllByMemberDao(currentMember, page);
        log.info("log3");
        //List<ApplyDto> ApplyDtoList = memberApplyPages.stream().map(p-> new ApplyDto(p, memberRepository, noticeRepository, applyFileRepository)).collect((toList()));

        return applyResourcePage;
        //return new PageImpl<>(ApplyDtoList, page, memberApplyPages.getTotalElements());
    }


}
