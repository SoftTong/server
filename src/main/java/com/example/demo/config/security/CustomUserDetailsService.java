package com.example.demo.config.security;

import com.example.demo.dao.MemberDao;
import com.example.demo.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

//해당 사용자의 데이터를 로드한다
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    MemberRepository memberRepository;

    //Spring Security에 의해 사용되며
    //findByUserId는 학번을 이용해 로그인할 수 있도록 한다
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        //Let people login with either username or email
        MemberDao memberDao = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with user id : " + userId)
                );
        return UserPrincipal.create(memberDao);
    }

    //JWT인증 필터에 사용되는 메소드
    @Transactional
    public UserDetails loadUserById(Long id) {
        MemberDao memberDao = memberRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );
        return UserPrincipal.create(memberDao);
    }
}
