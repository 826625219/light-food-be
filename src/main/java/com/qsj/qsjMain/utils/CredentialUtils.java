package com.qsj.qsjMain.utils;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class CredentialUtils {

    private static final String AES_KEY = "qsj_private_f9HRN($&@)32D0#$2912";

    public static String encrypt(String content) {
        try {
            SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            return Base64Utils.encodeToString(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String content) {
        try {
            SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(Base64Utils.decodeFromString(content));
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64Utils.encodeToString(salt);
    }

    /**
     * 检查是否来自管理员的请求
     * @return
     */
    public static boolean isAdminRequest(Long userId) {
        return userId == 0L || userId == 1L;
    }
}
