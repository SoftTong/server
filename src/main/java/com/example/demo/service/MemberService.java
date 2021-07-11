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

import java.util.HashMap;
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
                .userId(memberDto.getUserId())
                .build()).getId();
    }

    //상세 정보 조회
    //사용자의 계정정보와 권한을 갖는 UserDetails 인터페이스 반환
    @Override
    public MemberEntity loadUserByUsername(String userId) throws UsernameNotFoundException {
        return memberRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException((userId)));
    }


    // 회원정보 수정  Sangrok
    @Transactional
    public boolean PatchUser(String id,MemberDto userInfo){
        final Optional<MemberEntity> optMember =memberRepository.findByUserId(id);
        MemberEntity fetchedUser = optMember.get();
    //  DB에서 가져온 유저 정보가 없거나 보낸 사람과 다르다면
        if (fetchedUser==null || fetchedUser.getUserId() != id){
            return false;
        }
        else{
            if (userInfo.getName() != null){ // null 이 아니면 수정.
                fetchedUser.setName(userInfo.getName());
            } // 수정 계속하고
        }

        memberRepository.save(fetchedUser); // 저장.

        return true;

    }

}
