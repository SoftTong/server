package com.example.demo.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member")
public class MemberEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String userId;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String phone_number;

    @Column(length = 50, nullable = false)
    private String department;

    @Column(name = "status")
    private String status;

    @Builder
    public MemberEntity(String email, String password, String status, String name, String phone_number, String department, String userId) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.department = department;
        this.status = status;
    }

    // 사용자의 권한을 콜렉션 형태로 반환
    // 자료형은 GrantedAuthority를 구현해야함
    // ADMIN은 관리자의 권한(ADMIN)뿐만 아니라 일반 유저(USER)의 권한도 가지고 있기 때문에, ADMIN의 auth는 "ROLE_ADMIN,ROLE_USER"와 같은 형태로 전달이 될 것이고, 쉼표(,) 기준으로 잘라서 ROLE_ADMIN과 ROLE_USER를 roles에 추가해줍니다. 아까 루트 패스("/")에 권한을 USER에게만 주었지만, ADMIN은 두 개의 권한(USER, ADMIN)을 가지고 있기 때문에 접근이 가능합니다.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : status.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    // 사용자의 id를 반환 (unique한 값)
    @Override
    public String getUsername() {
        return email;
    }

    // 사용자의 password를 반환
    @Override
    public String getPassword() {
        return password;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true; // true -> 잠금되지 않았음
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return true; // true -> 사용 가능
    }
}
