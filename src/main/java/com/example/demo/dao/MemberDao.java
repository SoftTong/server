package com.example.demo.dao;

import com.example.demo.dao.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "member")
@Getter
@Setter
public class MemberDao extends DateAudit {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String userId;

    @Email
    @NotBlank
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(max = 50)
    private String phoneNumber;

    @NotBlank
    @Size(max = 100)
    private String password;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String department;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleDao> roles = new HashSet<>();

    public MemberDao() {

    }

    public MemberDao (String userId, String email, String phoneNumber, String password, String name,  String department) {
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.name = name;
        this.department = department;
    }

    static public class Builder {
        private Long id;
        private String userId;
        private String email;
        private String phoneNumber;
        private String password;
        private String name;
        private String department;
        private Set<RoleDao> roles;

        public Builder() {
        }

        public Builder(MemberDao member) {
            this.id = member.id;
            this.userId = member.userId;
            this.email = member.email;
            this.phoneNumber = member.phoneNumber;
            this.password = member.password;
            this.name = member.name;
            this.department = member.department;
            this.roles = member.roles;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder userid(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder roles(Set<RoleDao> roles) {
            this.roles = roles;
            return this;
        }

        public MemberDao build() { return new MemberDao(userId,email,phoneNumber,password,name,department);}

    }


}
