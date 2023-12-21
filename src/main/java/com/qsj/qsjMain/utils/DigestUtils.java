package com.qsj.qsjMain.utils;

import java.security.MessageDigest;

public class DigestUtils {
    public static String sha1(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuilder hexStr = new StringBuilder();
            String shaHex;
            for (byte b : digest) {
                shaHex = Integer.toHexString(b & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }

            return hexStr.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuilder hexStr = new StringBuilder();
            String shaHex;
            for (byte b : digest) {
                shaHex = Integer.toHexString(b & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }

            return hexStr.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
