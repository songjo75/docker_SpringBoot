package com.mbc.day05.service;

import com.mbc.day05.domain.User;
import com.mbc.day05.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    public User getUserById(Long userId) {
        return userMapper.getUserById(userId);
    }

    public void saveUser(User user) {
        if (user.getUserId() == null) {
            // 비밀번호 암호화
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userMapper.insertUser(user);
        } else {
            userMapper.updateUser(user);
        }
    }

    public void deleteUser(Long userId) {
        userMapper.deleteUser(userId);
    }

    // 전화번호 중복체크
    public boolean existsByPhone(String phone) {
        return userMapper.existsByPhone(phone);
    }

    // 이메일 중복체크
    public boolean existsByEmail(String email) {
        return userMapper.existsByEmail(email);
    }

    // 로그인 인증 처리
    public boolean authenticate(User user) {
        User findUser = userMapper.findByEmail(user.getEmail());
        // True && True 이면 True 리턴
//        return findUser != null && findUser.getPassword().equals(user.getPassword());
        return findUser != null && passwordEncoder.matches(user.getPassword(), findUser.getPassword());
    }

    // --------------- 아이디 / 비번 찾기 ----------
    public String getEmailByPhone(String phone) {
        return userMapper.selectEmailByPhone(phone);
    }

    public boolean resetPassword(String username, String email) {
        User user = userMapper.findByUsernameAndEmail(username, email);
        if (user != null) { // 이름과 이메일에 해당하는 회원이 있는 경우
            // 1. 임시비밀번호 생성
            // 2. 임시비밀번호 암호화
            // 3. 생성된 임시비밀번호로 해당 회원의 비밀번호를 수정
            // 4. 해당 사용자의 이메일로 임시비밀번호 전송
            String tempPassword = UUID.randomUUID().toString().substring(0, 8);
            userMapper.updatePassword(user.getUserId(), passwordEncoder.encode(tempPassword));
            // soutv : 바로 위의 변수를 출력하는 코드 생성
            System.out.println("tempPassword = " + tempPassword);

            try {
                // MimeMessage 객체를 생성, SimpleMailMessage
                MimeMessage message = mailSender.createMimeMessage();
                // MimeMessageHelper는 MimeMessage를 쉽게 설정할 수 있도록 도와 주는 객체
                MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8"); // false : 일반텍스로 전송, true: Html형식으로 전송

                helper.setTo(email);
                helper.setSubject("[Order Management] 임시 비밀번호 안내");
                helper.setText("<p>안녕하세요, <strong>" + username + "</strong>님.</p>" +
                        "<p>임시 비밀번호는 <strong>" + tempPassword + "</strong> 입니다.</p>", true); // true는 HTML로 전송한다는 의미

                mailSender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false; // 회원이 없으면 false
    }

    // 이메일로 회원정보 가져오기
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    // 비밀번호 변경 처리
    public boolean changePassword(String email, String currentPassword, String newPassword) {
        User user = userMapper.findByEmail(email);
        // 이메일에 해당하는 사용자가 없거나, 사용자가 있는데 비밀번호가 일치하지 않는 경우 false 리턴
        if (user == null || !passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }
        userMapper.updatePassword(user.getUserId(), passwordEncoder.encode(newPassword));
        return true;
    }

    // 회원 수정시 비밀번호 확인
    public boolean checkPassword(String email, String rawPassword) {
        User user = userMapper.findByEmail(email);
        return user != null && passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // 나의 정보 수정
    public boolean updateUserProfile(User user) {
        // updatedRows는 0 또는 1 값을 갖는다.
        // 해당 email의 회원 1명이거나 없을 수 있다.
        int updatedRows = userMapper.updateUserProfile(user);
        return updatedRows > 0; // true or false
    }

    // 회원 탈퇴
    public boolean deleteUserAccount(String email, String password) {
        User user = userMapper.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            userMapper.deleteUser(user.getUserId());
            return true;
        }
        return false;
    }
}

