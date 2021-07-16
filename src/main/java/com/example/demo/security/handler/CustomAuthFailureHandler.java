package com.example.demo.security.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = null;

        if(exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "id, password not equal";
        }

        else if(exception instanceof DisabledException) {
            errorMessage = "id is has been deactivated";
        }

        else if(exception instanceof CredentialsExpiredException) {
            errorMessage = "Your password has expired.";
        }

        else {
            errorMessage = "Login failed for an unknown reason.";
        }

        System.out.println("login errorMessage : " + errorMessage);
        request.setAttribute("errorMessage", errorMessage);
    }

}
