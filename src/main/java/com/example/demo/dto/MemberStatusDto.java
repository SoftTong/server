package com.example.demo.dto;

import com.example.demo.dao.MemberDao;
import com.example.demo.dao.RoleDao;
import com.example.demo.domain.entity.NoticeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class MemberStatusDto {

    private String userId;
    private String email;
    private String phoneNumber;
    private String name;
    private String department;
    private Set<RoleDao> roles;

    public MemberStatusDto() {
    }

    public MemberStatusDto(MemberDao memberDao) {
        this.userId = memberDao.getUserId();
        this.email = memberDao.getEmail();
        this.phoneNumber = memberDao.getPhoneNumber();
        this.name = memberDao.getName();
        this.department = memberDao.getDepartment();
        this.roles = memberDao.getRoles();
    }
}
