package com.example.demo.controller;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyFileNoticeEntity;
import com.example.demo.domain.entity.NoticeEntity;

import com.example.demo.dto.FileApplyDto;
import com.example.demo.dto.NoticeInfoDto;
import com.example.demo.service.apply.ApplyFileResgisterService;
import com.example.demo.service.apply.ApplyFileStatusService;
import com.example.demo.service.member.MemberRegisterService;
import com.example.demo.service.member.MemberStatusService;
import com.example.demo.service.notice.NoticeRegisterService;
import com.example.demo.service.notice.NoticeStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
@RequiredArgsConstructor
@Secured({"ROLE_USER","ROLE_ADMIN"})
@ResponseBody
@RequestMapping("/manage")
public class ManagerController {


    private final MemberStatusService memberStatusService;

    private final MemberRegisterService memberRegisterService;

    private final NoticeStatusService noticeStatusService;

    private final NoticeRegisterService noticeRegisterService;

    private final ApplyFileStatusService applyFileStatusService;

    private final ApplyFileResgisterService applyFileResgisterService;

    @GetMapping()//현재 사용자 정보 받아오기
    public List<NoticeEntity> getManager(HttpServletRequest request){

        log.info("GetManager");
        MemberDao currentUser = memberStatusService.GetCurrentUserInfo(request).get();

        List<NoticeEntity> noticeEntitys = noticeStatusService.findByMember(currentUser);

        return noticeEntitys;
    }


    @GetMapping("/{pageNum}")
    public Page<NoticeInfoDto> getManagerPage(HttpServletRequest req, @PathVariable int pageNum) {

        Pageable page = PageRequest.of(pageNum, 10, Sort.by("uploadDay").descending());
        MemberDao currentUser = memberStatusService.GetCurrentUserInfo(req).get();

        Page<NoticeEntity> noticeEntityPages = noticeStatusService.findAllByMemberDao(currentUser,page);

        List<NoticeInfoDto> noticeInfoDtoList = noticeEntityPages.stream().map(nep -> new NoticeInfoDto(nep)).collect((toList()));

        return new PageImpl<>(noticeInfoDtoList, page, noticeEntityPages.getTotalElements());
    }


    @GetMapping("/{noticeId}/{pageNum}")
    public PageImpl<Object> getNotice(@PathVariable Long noticeId, @PathVariable int pageNum) {
        Pageable page = PageRequest.of(pageNum, 10, Sort.by("member_id").ascending());

        String dtype = (String) noticeStatusService.findDtypeById(noticeId);

        if (dtype.equals("file")) {
            Page<ApplyFileNoticeEntity> applyPages = applyFileStatusService.findMemberById(noticeId,page);
            List<FileApplyDto> fileApplyDtoList = applyPages.stream().map(a-> new FileApplyDto(a) ).collect((toList()));
            return new PageImpl(fileApplyDtoList, page, applyPages.getTotalElements());
        } else if (dtype.equals("form")) {
            throw new IllegalStateException("폼 형식은 아직 구현되지 않았습니다.");
        } else {
            throw new IllegalStateException("올바르지 않은 공지사항입니다.");
        }

    }



}
