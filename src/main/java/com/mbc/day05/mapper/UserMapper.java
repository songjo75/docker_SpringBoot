package com.mbc.day05.mapper;

import com.mbc.day05.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> getAllUsers();

    User getUserById(Long userId);

    void insertUser(User user);

    void updateUser(User user);

    void deleteUser(Long userId);

    // 전화번호 중복체크
    @Select("select count(*) > 0 from users where phone=#{phone}")
    boolean existsByPhone(String phone);

    // 이메일 중복체크
    @Select("select count(*) > 0 from users where email=#{email}")
    boolean existsByEmail(String email);

    // 이메일로 회원정보 찾기
    @Select("select * from users where email = #{email}")
    User findByEmail(String email);

    // ---------- 아이디 / 비번 찾기 ------------
    @Select("select email from users where phone = #{phone}")
    String selectEmailByPhone(String phone);

    // 이메일과 이름으로 회원찾기
    @Select("select * from users where username = #{username} and email= #{email}")
    User findByUsernameAndEmail(@Param("username") String username, @Param("email") String email);

    @Update("update users set password=#{password} where userId = #{userId}")
    void updatePassword(@Param("userId") Long userId, @Param("password") String password);

    // 나의 정보 수정
    @Update("update users set username=#{username}, phone=#{phone}, address=#{address} where email = #{email}")
    int updateUserProfile(User user);
}






