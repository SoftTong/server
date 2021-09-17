package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import ch.qos.logback.core.CoreConstants;
import com.example.demo.config.security.JwtAuthenticationFilter;
import com.example.demo.config.security.JwtTokenProvider;
import com.example.demo.dao.MemberDao;
import com.example.demo.domain.entity.ApplyFileNoticeEntity;
import com.example.demo.domain.entity.MemberApply;
import com.example.demo.domain.entity.NoticeEntity;
import com.example.demo.domain.repository.ApplyFileRepository;
import com.example.demo.domain.repository.MemberApplyRepository;
import com.example.demo.domain.repository.MemberRepository;
import com.example.demo.domain.repository.NoticeRepository;
import com.example.demo.dto.ApplyDto;
import com.example.demo.dto.FileApplyDto;
import com.example.demo.dto.MemberDto;
import com.example.demo.dto.NoticeInfoDto;
import com.example.demo.payload.ApiResponse;
import com.example.demo.service.MemberService;
import com.example.demo.service.NoticeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.stream.Collectors.toList;


@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberService memberService;
    @Autowired
    private final NoticeService noticeService;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final ApplyFileRepository applyFileRepository;
    @Autowired
    private final MemberApplyRepository memberApplyRepository;
    @Autowired
    private final NoticeRepository noticeRepository;

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    @PatchMapping(value= "/user")// 회원정보 수정 Patch
    public MemberDto modifyUserInfo(@RequestBody MemberDto userInfo, HttpServletRequest request){

        String currentUserId = memberService.GetCurrentUserInfo(request).get().getUserId();
        memberService.PatchUser(currentUserId,userInfo);

        return userInfo;
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    @GetMapping(value= "/user/current")//현재 사용자 정보 받아오기
    public Optional<MemberDao> currentUserInfo(HttpServletRequest request){

        return memberService.GetCurrentUserInfo(request);
    }

    @ResponseBody
    @GetMapping("/apply/{pageNum}")
    public Page<ApplyDto> getApply(HttpServletRequest request, @PathVariable int pageNum) {

        Pageable page = PageRequest.of(pageNum, 10, Sort.by("id").descending());
        MemberDao currentMember = memberService.GetCurrentUserInfo(request).get();
        Page<MemberApply> memberApplyPages = memberApplyRepository.findAllByMemberId(currentMember.getId(), page);
        List<ApplyDto> ApplyDtoList = memberApplyPages.stream().map(p-> new ApplyDto(p, memberRepository, noticeRepository, applyFileRepository)).collect((toList()));

        return new PageImpl<>(ApplyDtoList, page, memberApplyPages.getTotalElements());
    }

    @ResponseBody
    @GetMapping("/apply/file/detail/{applyId}")
    public FileApplyDto getApplyFile(HttpServletRequest request, @PathVariable Long applyId) {

        //MemberDao currentMember = memberService.GetCurrentUserInfo(request).get();

        Optional<ApplyFileNoticeEntity> applyFileNoticeEntity = applyFileRepository.findById(applyId);
        FileApplyDto fileApplyDto = new FileApplyDto(applyFileNoticeEntity.get());

        return fileApplyDto;
    }



    @ResponseBody
    @DeleteMapping("/apply/file/detail/{applyId}")
    public ApiResponse deleteApplyFile(HttpServletRequest request, @PathVariable Long applyId) {

        MemberDao currentMember = memberService.GetCurrentUserInfo(request).get();

        if(noticeService.deleteApplyFiles(currentMember,applyId)){
            return new ApiResponse(Boolean.TRUE,"정상적으로 삭제되었습니다.");
        }else{
            return new ApiResponse(Boolean.FALSE,"잘못된 접근입니다.");
        }



    }

}
