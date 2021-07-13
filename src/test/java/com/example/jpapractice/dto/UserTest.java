package com.example.jpapractice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void test(){
        User user = new User();
        user.setName("hong");
        user.setEmail("hong@naver.com");

        System.out.println(user);
    }

}