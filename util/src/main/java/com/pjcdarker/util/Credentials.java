package com.pjcdarker.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author pjcdarker
 * @created 12/19/2016.
 */
public class Credentials {

    public static String build(String original, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(original.getBytes());

        StringBuilder sb = new StringBuilder();
        byte[] bytes = digest.digest();
        for (byte aByte : bytes) {
            sb.append(String.format("%02x", aByte & 0xff));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String md5 = Credentials.build("123456", "MD5");
        System.out.println(md5);
    }
}
