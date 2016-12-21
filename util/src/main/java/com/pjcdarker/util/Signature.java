package com.pjcdarker.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author pjc
 * @Created 12/20/2016.
 */
public class Signature {

    public static String generateToken() {
        String value = String.valueOf(System.currentTimeMillis());
        value += UUID.randomUUID().toString() + new Random().nextInt(10000);
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] b = md.digest(value.getBytes());
            return Base64.getEncoder().encodeToString(b); // 生成token
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String generateSignature(String token, String timestamp, String nonce) {
        String[] signatureArr = {token, timestamp, nonce};
        Arrays.sort(signatureArr);
        String tmpStr = ArrayToString(signatureArr);
        tmpStr = sha1(tmpStr);
        return tmpStr;
    }

    private static String ArrayToString(String[] strArr) {
        return Arrays.asList(strArr).stream().collect(Collectors.joining());
    }

    private static String sha1(String source) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(source.getBytes());
            for (byte b : bytes) {
                int t = b & 0xff;
                if (t < 0x10) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(Long.toString(t, 16));
            }
        } catch (NoSuchAlgorithmException e) {
            //
        }
        return stringBuilder.toString().toLowerCase();
    }
}
