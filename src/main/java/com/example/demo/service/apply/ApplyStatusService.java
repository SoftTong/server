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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
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
        Page<ApplyResource> applyResourcePage = applyResourceRepository.findAllByMemberDaoFetchJoin(currentMember, page);
        List<Long> ids = applyResourcePage.stream().map(applyResource -> applyResource.getId()).collect(Collectors.toUnmodifiableList());
        List<Object> dtypes = noticeRepository.findDtypeInIds(ids);
        int idx = 0;
        List<ApplyListDto> collect = new ArrayList<>();
        for(ApplyResource a : applyResourcePage) {
            collect.add(new ApplyListDto(a, dtypes.get(idx++)));
        }

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

        FormQuestion formQuestion = formQuestionRepository.findByFormNotice((FormNotice) noticeEntity).get();
        List<ApplyFormDto> collect = applyResourcePage.stream().map(applyResource -> new ApplyFormDto(applyResource,formQuestion.getDescription())).collect(toList());

        return new PageImpl<>(collect, page, applyResourcePage.getTotalElements());
    }

    @Transactional
    public ApiResult<?> modifyApplyStatus(ApplyStatusDto applyStatusDto, Long applyId) {

        String dtype = (String) findDtypeById(applyId);

        if (dtype.equals("file")) { // File 형식 제출일때
            ApplyFile applyFile = (ApplyFile)applyResourceRepository.findById(applyId).get();

            if(applyStatusDto.getStatus().equals("wait")){
                applyFile.setStatus(StatusName.wait);
                applyResourceRepository.save(applyFile);
                return ApiResult.OK("wait");
            }

            else if(applyStatusDto.getStatus().equals("confirm")){
                applyFile.setStatus(StatusName.confirm);
                applyResourceRepository.save(applyFile);
                return ApiResult.OK("confirm");
            }

            else if(applyStatusDto.getStatus().equals("reject")){
                applyFile.setStatus(StatusName.reject);
                applyResourceRepository.save(applyFile);
                return ApiResult.OK("reject");
            }

            return ApiResult.ERROR(new IllegalStateException("잘못된 리퀘스트 입니다"), HttpStatus.BAD_REQUEST);
        }
        // Form 형식 제출일때
        ApplyForm applyForm = (ApplyForm)applyResourceRepository.findById(applyId).get();

        if(applyStatusDto.getStatus().equals("wait")){
            applyForm.setStatus(StatusName.wait);
            applyResourceRepository.save(applyForm);
            return ApiResult.OK("wait");
        }

        else if(applyStatusDto.getStatus().equals("confirm")){
            applyForm.setStatus(StatusName.confirm);
            applyResourceRepository.save(applyForm);
            return ApiResult.OK("confirm");
        }

        else if(applyStatusDto.getStatus().equals("reject")){
            applyForm.setStatus(StatusName.reject);
            applyResourceRepository.save(applyForm);
            return ApiResult.OK("reject");
        }

        return ApiResult.ERROR(new IllegalStateException("잘못된 리퀘스트 입니다"), HttpStatus.BAD_REQUEST);
    }
}
