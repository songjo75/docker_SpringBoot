package com.mbc.day05.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 비밀번호 암호화
    public String encodePw(String rawPw) {
        // 암호화된 비번 반환
        return passwordEncoder.encode(rawPw);
    }

    // 비밀번호 검증
    public boolean matches(String rawPw, String encodedPw) {
        // 평문과 암호화된 비밀번호가 일치하면 true, 아니면 false를 리턴
        return passwordEncoder.matches(rawPw, encodedPw);
    }
}
