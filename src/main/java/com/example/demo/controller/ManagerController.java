package com.example.demo.controller;

import com.example.demo.service.apply.ApplyFileResgisterService;
import com.example.demo.service.apply.ApplyFileStatusService;
import com.example.demo.service.member.MemberStatusService;
import com.example.demo.service.notice.NoticeRegisterService;
import com.example.demo.service.notice.NoticeStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
@Secured({"ROLE_USER","ROLE_ADMIN"})
@ResponseBody
@RequestMapping("/manage")
public class ManagerController {


    private final MemberStatusService memberStatusService;

    private final NoticeStatusService noticeStatusService;

    private final NoticeRegisterService noticeRegisterService;

    private final ApplyFileStatusService applyFileStatusService;

    private final ApplyFileResgisterService applyFileResgisterService;



}
