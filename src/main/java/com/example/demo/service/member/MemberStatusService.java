package com.example.demo.service.member;

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
public class MemberStatusService {

  private MemberRepository memberRepository;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtTokenProvider jwtTokenProvider;

  @Secured({"ROLE_USER","ROLE_ADMIN"})
  public Optional<MemberDao> findMember(HttpServletRequest request){
    String token = jwtAuthenticationFilter.getJwtFromRequest(request);
    Long currentUserId = jwtTokenProvider.getUserIdFromJWT(token);
    return memberRepository.findById(currentUserId);
  }

  public List<MemberDao> findBySearchWordContaining(String searchWord) {
    return memberRepository.findByNameContaining(searchWord);
  }



}
