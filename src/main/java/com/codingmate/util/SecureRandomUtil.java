package com.codingmate.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Base64;

@UtilityClass
public class SecureRandomUtil {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateToken(int length) {
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}