package com.vezixtor.ontormi.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class CryptUtils {
    public static String toMd5(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(password.getBytes());
        byte[] digest = messageDigest.digest();
        StringBuilder stringBuffer = new StringBuilder();
        for (byte b : digest) {
            stringBuffer.append(String.format("%02x", b & 0xff));
        }
        return stringBuffer.toString();
    }
}
