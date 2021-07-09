package com.example.demo.controller;

import ch.qos.logback.core.CoreConstants;
import com.example.demo.dto.MemberDto;
import com.example.demo.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@AllArgsConstructor
public class MemberController {

    private MemberService memberService;

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

}
