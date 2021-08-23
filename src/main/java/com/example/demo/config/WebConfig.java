package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//ip:port/menufiles/~로 접근했을때 바로 클래스 패스의 menufiles폴더로 접근하기 위해 설정
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/menufiles/**")
                .addResourceLocations("classpath:/menufiles/");
    }

}