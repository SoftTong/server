package com.example.demo.dto;

import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
public class MemberDto {

    @Column(unique = true)
    private String userId;

    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private String department;
}
