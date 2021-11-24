package com.example.demo.controller;

import java.util.Optional;

import com.example.demo.dao.MemberDao;
import com.example.demo.dto.MemberDto;
import com.example.demo.service.MemberService;
import com.example.demo.service.member.MemberModifyService;
import com.example.demo.service.member.MemberStatusService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberModifyService memberModifyService;
    private final MemberStatusService memberStatusService;

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    @GetMapping(value= "/status")//현재 사용자 정보 받아오기
    public Optional<MemberDao> memberStatus(HttpServletRequest request){
        return memberStatusService.findMember(request);
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @ResponseBody
    @PatchMapping(value= "/info")// 회원정보 수정 Patch
    public MemberDto memberModify(@RequestBody MemberDto userInfo, HttpServletRequest request){

        String currentUserId = memberStatusService.findMember(request).get().getUserId();
        memberModifyService.modifyMember(currentUserId,userInfo);

        return userInfo;
    }

}
