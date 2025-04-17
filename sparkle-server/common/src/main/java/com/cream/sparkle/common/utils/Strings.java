package com.cream.sparkle.common.utils;

import com.cream.sparkle.common.error.RunErr;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Slf4j
public class Strings {

    /**
     * 简单加密函数;Base64编码保证输出均为可打印ASCII字符，避免字符编码问题，比如异或后可能导致的字符信息丢失
     */
    public static String jiaMi(@NonNull String plainText, @NonNull String key) {
        byte[] bytes = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(xorProcess(bytes, keyBytes));
    }

    /**
     * 简单解密函数;Base64编码保证输出均为可打印ASCII字符，避免字符编码问题，比如异或后可能导致的字符信息丢失
     */
    public static String jieMi(@NonNull String cipherText, @NonNull String key) {
        byte[] encrypted = Base64.getDecoder().decode(cipherText);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return new String(xorProcess(encrypted, keyBytes), StandardCharsets.UTF_8);
    }

    private static byte[] xorProcess(byte[] inputBytes, byte[] keyBytes) {
        byte[] result = new byte[inputBytes.length];
        for (int i = 0; i < inputBytes.length; i++) {
            result[i] = (byte) (inputBytes[i] ^ keyBytes[i % keyBytes.length]);
        }
        return result;
    }

    public static String md5(@NonNull String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RunErr("md5加密失败", e);
        }
    }
}
