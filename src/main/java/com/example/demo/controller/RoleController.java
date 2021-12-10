package com.example.demo.controller;

import com.example.demo.service.member.MemberModifyService;
import com.example.demo.service.role.RoleStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/role")
@RestController
public class RoleController {

    private final RoleStatusService roleStatusService;

    @PostMapping("/add/{memberId}")
    public ApiResult<?> roleAdd(@PathVariable Long memberId, @RequestParam String role) {
        return ApiResult.OK(roleStatusService.addRole(memberId, role));
    }

    @PostMapping("/remove/{memberId}")
    public ApiResult<?> roleRemove(@PathVariable Long memberId, @RequestParam String role) {
        return ApiResult.OK(roleStatusService.removeRole(memberId, role));
    }


}
