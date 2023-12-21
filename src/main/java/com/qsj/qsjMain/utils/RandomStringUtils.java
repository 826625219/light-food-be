package com.qsj.qsjMain.utils;

import java.util.Random;

public class RandomStringUtils {
    public static String randomAlphanumeric(int count) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString().toUpperCase();

    }
}
