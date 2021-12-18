package com.example.demo.service.apply;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyResource;
import com.example.demo.domain.entity.MemberApply;
import com.example.demo.domain.repository.ApplyResourceRepository;
import com.example.demo.dto.ApplyDto;
import com.example.demo.service.member.MemberStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplyStatusService {

    ApplyResourceRepository applyResourceRepository;
    MemberStatusService memberStatusService;

    public Page<ApplyResource> findApply(HttpServletRequest request, @PathVariable int pageNum) {

        Pageable page = PageRequest.of(pageNum, 10, Sort.by("id").descending());
        MemberDao currentMember = memberStatusService.findMember(request).get();
        Page<ApplyResource> applyResourcePage = applyResourceRepository.findAllByMemberDao(currentMember, page);
        //List<ApplyDto> ApplyDtoList = memberApplyPages.stream().map(p-> new ApplyDto(p, memberRepository, noticeRepository, applyFileRepository)).collect((toList()));

        return applyResourcePage;
        //return new PageImpl<>(ApplyDtoList, page, memberApplyPages.getTotalElements());
    }


}
