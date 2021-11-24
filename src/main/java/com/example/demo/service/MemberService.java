package com.example.demo.service;

import com.example.demo.config.security.JwtAuthenticationFilter;
import com.example.demo.config.security.JwtTokenProvider;
import com.example.demo.dao.MemberDao;
//import com.example.demo.domain.Role;
import com.example.demo.domain.repository.MemberRepository;
import com.example.demo.dto.MemberDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtTokenProvider jwtTokenProvider;
    PasswordEncoder passwordEncoder;

    // 회원정보 수정  Sangrok
    @Transactional
    public boolean PatchUser(String id, MemberDto userInfo){

        final Optional<MemberDao> optMember =memberRepository.findByUserId(id);
        MemberDao fetchedUser = optMember.get();

        if (userInfo.getPassword() != null){ // 비밀번호 수정.
            fetchedUser.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        }
        if (userInfo.getName() != null){ // 이름 수정.
            fetchedUser.setName(userInfo.getName());
        }
        if (userInfo.getEmail() != null){ // 이메일 수정.
            fetchedUser.setEmail(userInfo.getEmail());
           }
        if (userInfo.getPhoneNumber() != null){ // 핸드폰 번호 수정
            fetchedUser.setPhoneNumber(userInfo.getPhoneNumber());
        }

        memberRepository.save(fetchedUser); // 저장.

        return true;

    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    public Optional<MemberDao> GetCurrentUserInfo(HttpServletRequest request){
        String token = jwtAuthenticationFilter.getJwtFromRequest(request);
        Long currentUserId = jwtTokenProvider.getUserIdFromJWT(token);
        return memberRepository.findById(currentUserId);
    }

}


