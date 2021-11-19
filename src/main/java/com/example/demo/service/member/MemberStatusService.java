package com.example.demo.service.member;

import com.example.demo.config.security.JwtAuthenticationFilter;
import com.example.demo.config.security.JwtTokenProvider;
import com.example.demo.dao.MemberDao;
import com.example.demo.domain.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberStatusService {

  private final MemberRepository memberRepository;

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  private final JwtTokenProvider jwtTokenProvider;

  @Secured({"ROLE_USER","ROLE_ADMIN"})
  public Optional<MemberDao> GetCurrentUserInfo(HttpServletRequest request){
    String token = jwtAuthenticationFilter.getJwtFromRequest(request);
    Long currentUserId = jwtTokenProvider.getUserIdFromJWT(token);
    return memberRepository.findById(currentUserId);
  }



}
