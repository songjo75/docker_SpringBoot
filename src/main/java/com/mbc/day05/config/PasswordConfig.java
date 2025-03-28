package com.mbc.day05.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // 스프링 설정 클래스에 사용하는 어너테이션
public class PasswordConfig {

    @Bean // 스프링에서 Bean은 스프링 컨테이너가 관리하는 객체를 의미
    // PasswordEncoder == 인터페이스, 암호화, 평문과 암호화된 결과를 검증
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder 는 PasswordEncoder인터페이스를 구현한 구현체
        return new BCryptPasswordEncoder();
    }
}

