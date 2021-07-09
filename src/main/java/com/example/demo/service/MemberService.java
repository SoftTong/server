package com.example.demo.service;

import com.example.demo.domain.Role;
import com.example.demo.domain.entity.MemberEntity;
import com.example.demo.domain.repository.MemberRepository;
import com.example.demo.dto.MemberDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private MemberRepository memberRepository;

    //회원가입 처리
    @Transactional
    public Long joinUser(MemberDto memberDto) {

        //비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

        return memberRepository.save(MemberEntity.builder()
                .email(memberDto.getEmail())
                .status(memberDto.getStatus())
                .password(memberDto.getPassword())
                .name(memberDto.getName())
                .phone_number(memberDto.getPhone_number())
                .department(memberDto.getDepartment())
                .user_id(memberDto.getUser_id())
                .build()).getId();
    }

    //상세 정보 조회
    //사용자의 계정정보와 권한을 갖는 UserDetails 인터페이스 반환
    @Override
    public MemberEntity loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException((email)));
    }
}
