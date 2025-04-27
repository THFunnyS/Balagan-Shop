package com.balagan.balaganShop;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "234";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}

