package com.example.demo.config;

import com.example.demo.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private MemberService memberService;

    @Bean
    public PasswordEncoder passwordEncoder() { //암호화 객체
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //항상 인증 통과
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                    //페이지 권한 설정
                    //.antMatchers("/admin/**").hasRole("ADMIN")
                    //.antMatchers("/**").permitAll()
                    .antMatchers("/login", "/register/user", "/signup", "/user").permitAll() // 누구나 접근 허용
                    .antMatchers("/").hasRole("USER") // USER, ADMIN만 접근 가능
                    .antMatchers("/admin").hasRole("ADMIN") // ADMIN만 접근 가능
                    //.anyRequest().authenticated() // 나머지 요청들은 권한의 종류에 상관 없이 권한이 있어야 접근 가능
                .and() //로그인 설정
                    .formLogin()
                    .loginPage("/login") // 로그인 페이지 링크
                    .defaultSuccessUrl("/")
                    //.usernameParameter("파라미터명") //로그인 form에서 아이디는 name=username인 input을 기본으로 인식하는데, usernameParameter() 메서드를 통해 파라미터명을 변경할 수 있습니다.
                    .permitAll()
                .and()
                    .logout()
                    .logoutSuccessUrl("/login")
                    .invalidateHttpSession(true) //세션 날리기
                .and()
                    //403에러
                    .exceptionHandling().accessDeniedPage("/denied"); //예외 핸들링
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception { //인증
        //UserDetailService를 통해서 필요한 정보를 가져오는데, memberService에서 처리한다
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
}
