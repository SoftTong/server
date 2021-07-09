package com.example.demo.dto;

import com.example.demo.domain.entity.MemberEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberDto {
    private String userId;
    private String password;
    private String name;
    private String email;
    private String phone_number;
    private String department;
    private String status;
}
