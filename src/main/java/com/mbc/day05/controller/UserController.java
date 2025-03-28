package com.mbc.day05.controller;

import com.mbc.day05.domain.User;
import com.mbc.day05.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/form")
    public String showForm(@RequestParam(value = "id", required = false) Long userId, Model model) {
        if (userId == null) {
            model.addAttribute("user", new User());
        } else {
            model.addAttribute("user", userService.getUserById(userId));
        }
        return "user/user-form";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long userId) {
        userService.deleteUser(userId);
        return "redirect:/users";
    }

    // 전화번호 중복 체크
    @GetMapping("check-phone")
//    ResponseEntity<Boolean> : 단순한 성공/실패 응답 보낼때 사용
    public ResponseEntity<Boolean> checkPhone(@RequestParam("phone") String phone) {
        boolean exists = userService.existsByPhone(phone);
        return ResponseEntity.ok(exists);
    }

    // 이메일 중복 체크
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
    //------------------- 로그인 / 로그아웃 -------------------
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "user/login";
    }

    @PostMapping("/login")
    public String loginUser(User user, Model model, HttpSession session) {
        if(userService.authenticate(user)){ // 로그인 성공시
            session.setAttribute("loggedInUser", user.getEmail());
            return "redirect:/";
        }else{ // 로그인 실패
            model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "user/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // 아이디 / 비밀번호 찾기
    @GetMapping("/find-id")
    public String showFindForm() {
        return "user/find-idpw";
    }

    // 아이디(이메일) 찾기
    @PostMapping("/find-id")
    public String findIdByPhone(@RequestParam String phone, Model model) {
        String email = userService.getEmailByPhone(phone);
        model.addAttribute("foundEmail", email != null ? email : "전화번호에 해당하는 이메일이 없습니다!!");
        return "user/find-result";
    }

    // 비번 찾기
    @PostMapping("/find-password")
    public String findPassword(@RequestParam String username
            ,@RequestParam String email, Model model) {
        boolean result = userService.resetPassword(username, email);
        System.out.println("result = " + result);
        model.addAttribute("passwordReset", result ? "임시 비밀번호가 이메일로 발송되었습니다." : "사용자 정보가 일치하지 않습니다!!");
        return "user/find-result";
    }

    //------------------ 마이프로필 ------------------------
    // 프로필 페이지 이동
    @GetMapping("/profile")
    public String userProfile(Model model, HttpSession session) {
        String email = (String) session.getAttribute("loggedInUser");
        User user = userService.findByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("confirmed", false); // 기본값 추가
        return "user/my-profile-edit";
    }

    // 비밀번호 변경 페이지 이동
    @GetMapping("/my/change-password")
    public String changePasswordPage(Model model, HttpSession session) {
        String email = (String) session.getAttribute("loggedInUser");
        if (email == null) {
            return "redirect:/users/login";
        }
        User user = userService.findByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("selected", "password");
        return "user/my-profile-change-password";
    }

    // 비밀번호 변경 처리
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUser");
        if (email == null) {
            return "redirect:/users/login";
        }

        boolean result = userService.changePassword(email, currentPassword, newPassword);
        if (result) {
            model.addAttribute("success", "비밀번호가 성공적으로 변경되었습니다.");
        } else {
            model.addAttribute("error", "현재 비밀번호가 일치하지 않거나 오류가 발생했습니다.");
        }
        return "user/my-profile-change-password";
    }

    // 회원수정시 비밀번호 확인
    @PostMapping("/my/edit/confirm-password")
    public String confirmPassword(@RequestParam String password, HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUser");
        if (userService.checkPassword(email, password)) {
            model.addAttribute("confirmed", true);
            model.addAttribute("user", userService.findByEmail(email));
            return "user/my-profile-edit";
        } else {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("confirmed", false);
            return "user/my-profile-edit";
        }
    }

    // 나의 정보 수정
    @PostMapping("/my/edit/save")
    public String saveProfile(@ModelAttribute User user, HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUser");
        if (email == null) {
            return "redirect:/users/login";
        }

        user.setEmail(email);  // 세션 이메일을 기준으로 수정
        boolean result = userService.updateUserProfile(user);
        if (result) {
            model.addAttribute("success", "회원정보가 성공적으로 수정되었습니다.");
        } else {
            model.addAttribute("error", "회원정보 수정에 실패했습니다.");
        }
        model.addAttribute("confirmed", true);
        model.addAttribute("user", userService.findByEmail(email));
        return "user/my-profile-edit";
    }

    // 회원탈퇴 페이지 이동
    @GetMapping("/my/delete")
    public String deleteAccountPage() {
        return "user/my-profile-delete";
    }

    // 회원 탈퇴 처리
    @PostMapping("/delete-account")
    public String deleteAccount(@RequestParam String password, HttpSession session, RedirectAttributes rttr) {
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }
        if (userService.deleteUserAccount(loggedInUser, password)) {
            // 세션 초기화되면서 로그아웃 처리
            session.invalidate();
            rttr.addFlashAttribute("success", true);
            return "redirect:/users/my/delete"; // 탈퇴 페이지로 리다이렉트
        } else {
            rttr.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/users/my/delete";
        }
    }

}// end of class

