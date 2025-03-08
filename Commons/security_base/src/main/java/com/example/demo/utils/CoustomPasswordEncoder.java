package com.example.demo.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public final class CoustomPasswordEncoder implements PasswordEncoder {
    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
    public static String encrypt(String strSrc) {
        String encryptCode = encoder.encode(strSrc);
        return encryptCode;
    }
    @Override
    public String encode(CharSequence rawPassword) {
        return encrypt(rawPassword.toString());
    }
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {
        System.out.println(encrypt("123456789"));
    }
}

