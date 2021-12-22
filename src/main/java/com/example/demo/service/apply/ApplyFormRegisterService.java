package com.example.demo.service.apply;

import com.example.demo.controller.ApiResult;
import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.*;
import com.example.demo.domain.repository.ApplyResourceRepository;
import com.example.demo.domain.repository.FormAnswerRepository;
import com.example.demo.domain.repository.FormQuestionRepository;
import com.example.demo.dto.FormAnswerDto;
import com.example.demo.service.member.MemberStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplyFormRegisterService {

    private final FormQuestionRepository formQuestionRepository;
    private final FormAnswerRepository formAnswerRepository;
    private final MemberStatusService memberStatusService;
    private final ApplyResourceRepository applyResourceRepository;

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    public ApiResult<?> addApplyForm(HttpServletRequest request, FormAnswerDto formAnswerDto, Long formQuestionId) {

        FormQuestion formQuestion = formQuestionRepository.findById(formQuestionId).get();
        MemberDao currentMember = memberStatusService.findMember(request).get();

        applyResourceRepository.save(new ApplyForm(formAnswerDto, currentMember, formQuestion.getFormNotice()));
        formAnswerRepository.save(new FormAnswer(formQuestion,currentMember,formAnswerDto));

        return ApiResult.OK(formAnswerDto);
    }
}
