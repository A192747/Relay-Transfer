package com.example.hashgen.util;

import java.security.SecureRandom;
import java.util.Base64;

public class HashGenerator {
    public static String generate() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[6]; // Массив из 6 байт для получения строки длиной в 8 символов после кодирования в Base64
        String uniqueString;

        random.nextBytes(bytes);
        uniqueString = Base64.getEncoder().encodeToString(bytes);

        return uniqueString;
    }
}
