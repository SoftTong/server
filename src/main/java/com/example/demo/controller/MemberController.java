package com.example.demo.controller;

import java.security.Principal;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import ch.qos.logback.core.CoreConstants;
import com.example.demo.domain.repository.MemberRepository;
import com.example.demo.dto.MemberDto;
import com.example.demo.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @ResponseBody
    @PatchMapping(value= "/user/{id}")// 회원정보 수정 Patch
    public MemberDto modifyUserInfo(@PathVariable(value="id") String id, @RequestBody MemberDto userInfo){

        memberService.PatchUser(id,userInfo);

        return userInfo;
    }



}
