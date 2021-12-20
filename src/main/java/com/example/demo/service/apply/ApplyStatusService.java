package com.example.demo.service.apply;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyResource;
import com.example.demo.domain.repository.ApplyResourceRepository;
import com.example.demo.dto.ApplyListDto;
import com.example.demo.service.member.MemberStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ApplyStatusService {

    private final ApplyResourceRepository applyResourceRepository;
    private final MemberStatusService memberStatusService;

    public Page<ApplyListDto> findApply(HttpServletRequest request, @PathVariable int pageNum) {

        Pageable page = PageRequest.of(pageNum, 10, Sort.by("id").descending());
        MemberDao currentMember = memberStatusService.findMember(request).get();
        Page<ApplyResource> applyResourcePage = applyResourceRepository.findAllByMemberDao(currentMember, page);
        List<ApplyListDto> collect = applyResourcePage.stream().map(applyResource -> new ApplyListDto(applyResource)).collect(toList());

        return new PageImpl<>(collect, page, applyResourcePage.getTotalElements());
    }


}
