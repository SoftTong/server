package com.example.demo.config.security;

import com.example.demo.dao.MemberDao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;

//UserDetails에 해당하는 UserPrincipal
//이 인스턴스는 UserDetailService를 통해 반환된다.
//Spring Security는 UserPrincipal 객체에 저장된 정보를 이용해 인증과 권한 부여를 수행한다
@Getter
@Setter
public class UserPrincipal implements UserDetails {

    private Long id;

    private String userId;

    @JsonIgnore
    private String email;

    private String phoneNumber;

    @JsonIgnore
    private String password;

    private String name;

    private String department;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal (Long id, String userId, String email, String phoneNumber, String password, String name,  String department, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.name = name;
        this.department = department;
        this.authorities = authorities;
    }

    public static UserPrincipal create(MemberDao memberDao) {
        //steam api사용
        //steam의 map을 이용해 데이터 변환(중간연산)
        //Collectors.toList()를 이용해 stream에서 작업한 결과를 List로 받는다
        List<GrantedAuthority> authorities = memberDao.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new UserPrincipal(
                memberDao.getId(),
                memberDao.getUserId(),
                memberDao.getEmail(),
                memberDao.getPhoneNumber(),
                memberDao.getPassword(),
                memberDao.getName(),
                memberDao.getDepartment(),
                authorities
        );
    }

    @Override
    public String getUsername() {
        return userId;
    }
    /*public String getUsername() {
        return name;
    }*/

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
