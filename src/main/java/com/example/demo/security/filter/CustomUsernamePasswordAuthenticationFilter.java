package com.example.demo.security.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

//Form 요청이면 기존 usernamePasswordAuthenticationFilter와 동일하게 동작하고,
//JSON 요청이면 request의 buffer를 읽어 요청을 파싱하여 Map에 데이터를 보관하고 사용자가 입력한 파라미터명과 동일한 키값으로 이를 가져와 기존과 동일하게
//UsernamePasswordAuthenticationToken을 만들어 Authenticationmanager가 authentication과정을 진행할 수 있도록 구현
//이후 UserDetailService에서 요청에 포함된 Username을 기준으로 DB에서 사용자를 찾고 authenticationManager에 등록된 PasswordEncoder로 패스워드를 비교하여 로그인 과정 진행
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private boolean postOnly = true;
    private HashMap<String, String> jsonRequest;

    @Override
    protected  String obtainPassword(HttpServletRequest request) {
        String passwordParameter = super.getPasswordParameter();
        if(request.getHeader("Content-Type").equals(ContentType.APPLICATION_JSON.getMimeType())) {
            return jsonRequest.get(passwordParameter);
        }
        return request.getParameter(passwordParameter);
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String usernameParameter = super.getUsernameParameter();
        if(request.getHeader("Content-Type").equals(ContentType.APPLICATION_JSON.getMimeType())) {
            return jsonRequest.get(usernameParameter);
        }
        return request.getParameter(usernameParameter);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if(postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        if(request.getHeader("Content-Type").equals(ContentType.APPLICATION_JSON.getMimeType())) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                this.jsonRequest = (HashMap<String, String>) mapper.readValue(request.getReader().lines().collect(Collectors.joining()), new TypeReference<Map<String, String>>() {

                });
            } catch (IOException e) {
                e.printStackTrace();
                throw new AuthenticationServiceException("Request Content-Type(application) Parsing Error");
            }
        }
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println("login id : " + username);

        if(username == null) {
            username = "";
        }

        if(password == null) {
            password = "";
        }

        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }
}
