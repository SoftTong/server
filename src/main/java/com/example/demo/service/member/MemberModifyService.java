package com.example.demo.service.member;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.repository.MemberRepository;
import com.example.demo.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberModifyService {

  private final MemberRepository memberRepository;

  private final PasswordEncoder passwordEncoder;

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

}
