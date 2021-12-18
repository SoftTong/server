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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

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

        applyResourceRepository.save(new ApplyFormNotice(formAnswerDto, currentMember, formQuestion.getFormNotice()));
        formAnswerRepository.save(new FormAnswer(formQuestion,currentMember,formAnswerDto));

        return ApiResult.OK(formAnswerDto);
    }
}
