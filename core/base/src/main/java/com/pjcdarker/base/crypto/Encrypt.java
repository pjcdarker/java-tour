package com.pjcdarker.base.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Objects;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * @author pjcdarker
 */
public class Encrypt {

    private static final String transform = "AES/CBC/PKCS5Padding";
    private static final String KEYSTORE_FILE = "keystore.ks";


    public static void keyStore(String pwd) throws KeyStoreException {
        // PKCS12
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

        char[] keyStorePwd = pwd.toCharArray();
        try (InputStream keyStoreData = Files.newInputStream(Paths.get(KEYSTORE_FILE))) {
            keyStore.load(keyStoreData, keyStorePwd);

            // don't want to load any data into the KeyStore
            // keyStore.load(null, keyStorePwd);

            // getEntry
            KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(keyStorePwd);
            KeyStore.Entry keyEntry = keyStore.getEntry("keyAlias", entryPassword);

            // privateKeyEntry
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry("keyAlias",
                entryPassword);

        } catch (CertificateException | NoSuchAlgorithmException | IOException | UnrecoverableEntryException e) {
            e.printStackTrace();
        }
    }

    public static void encryptString(String original) throws NoSuchPaddingException, NoSuchAlgorithmException,
        InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Objects.requireNonNull(original, "crypto text require not null");
        //  which encryption algorithm to use
        Cipher cipher = Cipher.getInstance(transform);

        // byte[] keyBytes = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        // String algorithm = "RawBytes";
        // SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);

        SecretKey key = genSecretKey("AES", 256);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] plainText = original.getBytes("UTF-8");
        cipher.doFinal(plainText);
    }

    /**
     * Mac
     *
     * @param original
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     */
    public static String macString(String original) throws NoSuchAlgorithmException,
        InvalidKeyException, UnsupportedEncodingException {
        Objects.requireNonNull(original, "crypto text require not null");
        //  which encryption algorithm to use
        Mac mac = Mac.getInstance(transform);

        // byte[] keyBytes = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        // String algorithm = "RawBytes";
        // SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);

        SecretKey key = genSecretKey("HmacSHA256", 256);
        mac.init(key);

        byte[] plainText = original.getBytes("UTF-8");
        byte[] result = mac.doFinal(plainText);
        return new String(result);
    }

    /**
     * @param original
     * @param algorithm AES, SHA-256
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String digestString(String original, String algorithm)
        throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(original.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();
        byte[] bytes = digest.digest();
        for (byte aByte : bytes) {
            sb.append(String.format("%02x", aByte & 0xff));
        }
        return sb.toString();
    }

    /**
     * @param original
     * @param algorithm SHA256WithDSA
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String signatureString(KeyPair keyPair, String original, String algorithm) throws NoSuchAlgorithmException,
        InvalidKeyException, UnsupportedEncodingException, SignatureException {

        Signature signature = Signature.getInstance(algorithm);

        // KeyPair keyPair = genKeyPair("DSA");
        SecureRandom secureRandom = new SecureRandom();
        signature.initSign(keyPair.getPrivate(), secureRandom);

        byte[] data = original.getBytes("UTF-8");
        signature.update(data);

        byte[] digitalSignature = signature.sign();
        return new String(digitalSignature);
    }


    public static boolean signatureVerify(KeyPair keyPair, String original, String digitalSignature, String algorithm)
        throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {

        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(keyPair.getPublic());

        signature.update(original.getBytes("UTF-8"));

        boolean result = signature.verify(digitalSignature.getBytes("UTF-8"));
        System.out.println("verified = " + result);
        return result;
    }


    /**
     * @param algorithm
     * @param keyBitSize
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static SecretKey genSecretKey(String algorithm, int keyBitSize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        SecureRandom secureRandom = new SecureRandom();
        keyGenerator.init(keyBitSize, secureRandom);
        return keyGenerator.generateKey();
    }

    /**
     * @param algorithm
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static KeyPair genKeyPair(String algorithm) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        return keyPairGenerator.generateKeyPair();
    }
}
