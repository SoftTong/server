package com.example.demo.controller;

import java.security.Principal;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import ch.qos.logback.core.CoreConstants;
import com.example.demo.config.security.JwtAuthenticationFilter;
import com.example.demo.config.security.JwtTokenProvider;
import com.example.demo.dao.MemberDao;
import com.example.demo.domain.repository.MemberRepository;
import com.example.demo.dto.MemberDto;
import com.example.demo.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberService memberService;
    @Autowired
    private final MemberRepository memberRepository;

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



}
