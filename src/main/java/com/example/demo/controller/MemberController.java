package com.example.demo.controller;

import java.util.Map;
import java.util.HashMap;
import ch.qos.logback.core.CoreConstants;
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

    @PostMapping("/register/user")
    public String signUp(MemberDto memberDto) { // 회원 추가
        memberService.joinUser(memberDto);

        return "redirect:/login";
    }

    //Get으로 /logout오면 Spring Security logout 작동
    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
//
    @ResponseBody
    @PatchMapping(value= "/user/{id}")// 회원정보 수정 Patch
    public MemberDto modifyUserInfo(@PathVariable(value="id") String id, @RequestBody MemberDto userInfo){
        Map<String,Object> response = new HashMap<>();

        System.out.println(id);
        System.out.println(userInfo.getName());

        return userInfo;
    }



}

