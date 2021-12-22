package com.example.demo.service.apply;

import com.example.demo.controller.ApiResult;
import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.*;
import com.example.demo.domain.repository.ApplyResourceRepository;
import com.example.demo.domain.repository.FormQuestionRepository;
import com.example.demo.domain.repository.NoticeRepository;
import com.example.demo.dto.*;
import com.example.demo.service.member.MemberStatusService;
import com.example.demo.service.notice.NoticeStatusService;
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
    private final NoticeRepository noticeRepository;
    private final NoticeStatusService noticeStatusService;
    private final FormQuestionRepository formQuestionRepository;

    public Object findDtypeById(Long applyId){
        return applyResourceRepository.findDtypeById(applyId);
    }

    public Page<ApplyListDto> findApply(HttpServletRequest request, @PathVariable int pageNum) {

        Pageable page = PageRequest.of(pageNum, 10, Sort.by("id").descending());
        MemberDao currentMember = memberStatusService.findMember(request).get();
        Page<ApplyResource> applyResourcePage = applyResourceRepository.findAllByMemberDao(currentMember, page);
        List<ApplyListDto> collect = applyResourcePage.stream().map(applyResource ->
                new ApplyListDto(applyResource, noticeRepository.findDtypeById(applyResource.getNoticeEntity().getId()))).collect(toList());

        return new PageImpl<>(collect, page, applyResourcePage.getTotalElements());
    }

    public ApiResult<?> findApplyByApplyId(HttpServletRequest request, Long applyId) {

        String dtype = (String) findDtypeById(applyId);

        log.debug("dtype = {}", dtype);
        if (dtype.equals("file")) { // File 형식 제출일때
            ApplyFile applyFile = (ApplyFile)applyResourceRepository.findById(applyId).get();
            return ApiResult.OK(new FileApplyDto(applyFile));
        }
        // Form 형식 제출일때
        ApplyForm applyForm = (ApplyForm)applyResourceRepository.findById(applyId).get();
        FormApplyDto formApplyDto = new FormApplyDto(applyForm);

        FormNotice noticeEntity = (FormNotice) applyForm.getNoticeEntity();
        FormQuestion formQuestion = formQuestionRepository.findByFormNotice(noticeEntity).get();
        formApplyDto.setQuestion(formQuestion.getDescription());

        return ApiResult.OK(formApplyDto);
    }


    public Page<?> findApplyByNoticeId(Long noticeId, int pageNum) {
        Pageable page = PageRequest.of(pageNum, 10, Sort.by("id").descending());

        NoticeEntity noticeEntity = noticeRepository.findById(noticeId).get();
        String dtype = (String) noticeStatusService.findDtypeById(noticeId);

        log.debug("dtype = {}", dtype);
        if (dtype.equals("file")) { // FileNotice 타입일 때
            Page<ApplyFile> applyResourcePage = applyResourceRepository.findAllByNoticeEntity(noticeEntity, page);
            List<ApplyFileDto> collect = applyResourcePage.stream().map(applyResource -> new ApplyFileDto(applyResource)).collect(toList());
            return new PageImpl<>(collect, page, applyResourcePage.getTotalElements());
        }
        // FormNotice 타입일 때
        Page<ApplyForm> applyResourcePage = applyResourceRepository.findAllByNoticeEntity(noticeEntity, page);
        List<ApplyFormDto> collect = applyResourcePage.stream().map(applyResource -> new ApplyFormDto(applyResource)).collect(toList());

        return new PageImpl<>(collect, page, applyResourcePage.getTotalElements());
    }
}
