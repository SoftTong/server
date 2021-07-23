package com.example.demo.config;

import com.example.demo.config.security.CustomUserDetailsService;
import com.example.demo.config.security.JwtAuthenticationEntryPoint;
import com.example.demo.config.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity //웹 보안
@EnableGlobalMethodSecurity( //어노페이션에 기반한 메소드 레벨 보안을 가능하도록 함
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomUserDetailsService customUserDetailsService; //userDetailService인터페이스를 구현하는것

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler; //인증이 이루어지지 않은 상태에서 보호 자원에 접근시도가 들어오면 401에러를 반환

    //모든 Request의 Authorization Header에서 JWT 인증 토큰을 읽는다
    //토큰의 유효성을 검사한다
    //해당 토큰을 가진 사용자 세부 사항을 로드한다
    //Spring Security의 SecurityContext에서 사용자 세부 사항을 설정하고 이를 사용해 권한 검사를 수행한다.
    //그리고 SecurityContext컨트롤러에 저장된 사용자 세부 정조에 접근하여 비즈니스 로직을 수행할 수 있다
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    //AuthenticationManagerBuilder는 AuthenticationManager 사용자를 인증하기 위해
    //주요 Spring Security 인터페이스의 인터페이스를 생성하는데 사용된다다
    //customUserDetailsService와 passwordEncoder를 제공하여 AuthenticationManager를 구축했다
    //AuthenticationManager기반으로 로그인 API에서 사용자 인증을 진행
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                    .and()
                .csrf()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers("/",
                            "/favicon.ico",
                            "/**/*.png",
                            "/**/*/.gif",
                            "/**/*/.svg",
                            "/**/*/.jpg",
                            "/**/*/.html",
                            "/**/*/.css",
                            "/**/*/.js")
                            .permitAll()
                    .antMatchers("/api/auth/**")
                        .permitAll()
                    .antMatchers("/api/user/checkUserIdAvailability", "/api/user/checkEmailAvailability")
                        .permitAll()
                    .antMatchers(HttpMethod.GET, "/api/users/**")
                        .permitAll()
                    .anyRequest()
                        .authenticated();

        // Add our Custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}