package com.example.jpapractice.dto;

import org.junit.jupiter.api.Test;

class NameTest {

    @Test
    void test() {
        Name name = Name.STAR;
        System.out.println("************************");
        System.out.println(name.ordinal());
        System.out.println("************************");
    }

    @Test
    void test2() {
        for(Name name : Name.values()){
            System.out.println(name);
        }
    }

}