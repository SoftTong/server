package com.example.demo.service.member;

import com.example.demo.config.security.JwtAuthenticationFilter;
import com.example.demo.config.security.JwtTokenProvider;
import com.example.demo.dao.MemberDao;
import com.example.demo.domain.repository.MemberRepository;
import com.example.demo.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberModifyService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public boolean modifyMember(String id, MemberDto userInfo){

    final Optional<MemberDao> optMember = memberRepository.findByUserId(id);
    MemberDao modifyMember = optMember.get();

    if (userInfo.getPassword() != null){ // 비밀번호 수정.
      modifyMember.setPassword(passwordEncoder.encode(userInfo.getPassword()));
    }
    if (userInfo.getName() != null){ // 이름 수정.
      modifyMember.setName(userInfo.getName());
    }
    if (userInfo.getEmail() != null){ // 이메일 수정.
      modifyMember.setEmail(userInfo.getEmail());
    }
    if (userInfo.getPhoneNumber() != null) { // 핸드폰 번호 수정
      modifyMember.setPhoneNumber(userInfo.getPhoneNumber());
    }
    memberRepository.save(modifyMember); // 저장.

    return true;
  }

}
