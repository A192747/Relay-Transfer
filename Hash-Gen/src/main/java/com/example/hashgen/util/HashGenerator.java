package com.example.hashgen.util;

import java.security.SecureRandom;
import java.util.Base64;

public class HashGenerator {
    private HashGenerator() {
    }

    public static String generate() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[6];
        String uniqueString;

        random.nextBytes(bytes);


        uniqueString = Base64.getEncoder().encodeToString(bytes);
        return formString(uniqueString);
    }

    //remove '/' element
    private static String formString(String string) {
        char elem = '/';
        int index = string.indexOf(elem);
        if (index != -1) {
            return string.replace(elem, string.charAt(Math.abs(index - 1)));
        }
        return string;
    }
}
