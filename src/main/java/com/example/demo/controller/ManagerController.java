package com.example.demo.controller;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyFileNoticeEntity;
import com.example.demo.domain.entity.FileNotice;
import com.example.demo.domain.entity.NoticeEntity;
import com.example.demo.domain.entity.StatusName;
import com.example.demo.domain.repository.ApplyFileRepository;
import com.example.demo.domain.repository.MemberRepository;
import com.example.demo.domain.repository.NoticeRepository;
import com.example.demo.dto.FileApplyDto;
import com.example.demo.dto.FileNoticeDto;
import com.example.demo.dto.NoticeInfoDto;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
@RequiredArgsConstructor
@Secured({ "ROLE_ADMIN" })
@ResponseBody
@RequestMapping("/manage")
public class ManagerController {

    @Autowired
    private final MemberService memberService;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private  final ApplyFileRepository applyFileRepository;
    @Autowired
    private final NoticeRepository noticeRepository;


    @GetMapping()//현재 사용자 정보 받아오기
    public List<NoticeEntity> getManager(HttpServletRequest request){

        log.info("GetManager");
        MemberDao currentUser = memberService.GetCurrentUserInfo(request).get();

        List<NoticeEntity> noticeEntitys = noticeRepository.findByMemberDao(currentUser);

        return noticeEntitys;
    }


    @GetMapping("/{pageNum}")
    public Page<NoticeInfoDto> getManagerPage(HttpServletRequest req, @PathVariable int pageNum) {
        Pageable page = PageRequest.of(pageNum, 5, Sort.by("uploadDay").descending());

        MemberDao currentUser = memberService.GetCurrentUserInfo(req).get();

        Page<NoticeEntity> noticeEntityPages = noticeRepository.findAllByMemberDao(currentUser,page);
        List<NoticeInfoDto> noticeInfoDtoList = noticeEntityPages.stream().map(nep -> new NoticeInfoDto(nep)).collect((toList()));

        return new PageImpl<>(noticeInfoDtoList, page, noticeEntityPages.getTotalElements());
    }


    @GetMapping("/{noticeId}/{pageNum}")
    public Page<FileApplyDto> getNotice(@PathVariable Long noticeId,@PathVariable int pageNum) {
        Pageable page = PageRequest.of(pageNum, 5, Sort.by("member_id").ascending());

        Page<ApplyFileNoticeEntity> applyPages = applyFileRepository.findMemberById(noticeId,page);
        List<FileApplyDto> fileApplyDtoList = applyPages.stream().map(a-> new FileApplyDto(a) ).collect((toList()));


        return new PageImpl<>(fileApplyDtoList, page, applyPages.getTotalElements());
    }



}
