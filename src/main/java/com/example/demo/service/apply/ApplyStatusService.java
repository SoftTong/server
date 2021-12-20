package com.example.demo.service.apply;

import com.example.demo.controller.ApiResult;
import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.*;
import com.example.demo.domain.repository.ApplyResourceRepository;
import com.example.demo.domain.repository.NoticeRepository;
import com.example.demo.dto.ApplyFileDto;
import com.example.demo.dto.ApplyFormDto;
import com.example.demo.dto.ApplyListDto;
import com.example.demo.service.member.MemberStatusService;
import com.example.demo.service.notice.NoticeStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
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
    private final NoticeRepository noticeRepository;
    private final NoticeStatusService noticeStatusService;

    public Page<ApplyListDto> findApply(HttpServletRequest request, @PathVariable int pageNum) {

        Pageable page = PageRequest.of(pageNum, 10, Sort.by("id").descending());
        MemberDao currentMember = memberStatusService.findMember(request).get();
        Page<ApplyResource> applyResourcePage = applyResourceRepository.findAllByMemberDao(currentMember, page);
        List<ApplyListDto> collect = applyResourcePage.stream().map(applyResource ->
                new ApplyListDto(applyResource, noticeRepository.findDtypeById(applyResource.getNoticeEntity().getId()))).collect(toList());

        return new PageImpl<>(collect, page, applyResourcePage.getTotalElements());
    }


    public Page<?> findApplyByNoticeId(Long noticeId, int pageNum) {
        Pageable page = PageRequest.of(pageNum, 10, Sort.by("id").descending());

        NoticeEntity noticeEntity = noticeRepository.findById(noticeId).get();
        String dtype = (String) noticeStatusService.findDtypeById(noticeId);

        log.debug("dtype = {}", dtype);
        if (dtype.equals("file")) { // FileNotice 타입일 때
            Page<ApplyFileNoticeEntity> applyResourcePage = applyResourceRepository.findAllByNoticeEntity(noticeEntity, page);
            List<ApplyFileDto> collect = applyResourcePage.stream().map(applyResource -> new ApplyFileDto(applyResource)).collect(toList());
            return new PageImpl<>(collect, page, applyResourcePage.getTotalElements());
        }
        // FormNotice 타입일 때
        Page<ApplyFormNotice> applyResourcePage = applyResourceRepository.findAllByNoticeEntity(noticeEntity, page);
        List<ApplyFormDto> collect = applyResourcePage.stream().map(applyResource -> new ApplyFormDto(applyResource)).collect(toList());

        return new PageImpl<>(collect, page, applyResourcePage.getTotalElements());
    }
}
