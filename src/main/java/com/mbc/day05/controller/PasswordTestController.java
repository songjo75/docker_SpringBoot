package com.mbc.day05.controller;

import com.mbc.day05.service.PasswordService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordTestController {
    @Autowired
    private PasswordService passwordService;

    @GetMapping("/test")
    public String test() {
        String rawPw = "1234";
        String rawPw2 = "1234";
        String encPw= passwordService.encodePw(rawPw);
        String encPw2= passwordService.encodePw(rawPw2);

        boolean isMatch = passwordService.matches(rawPw, encPw);
        boolean isMatch2 = passwordService.matches(rawPw2, encPw2);

        return "평문비밀번호 : " + rawPw + "<br/>"
                +"암호화된 비밀번호 : " + encPw+"<br/>"
                +"암호화된 비밀번호 : " + encPw2+"<br/>"
                +"비밀번호 일치 : " + isMatch+"<br/>"
                +"비밀번호 일치 : " + isMatch2;
    }
}
