package com.example.app_backend.helpers;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptUtil {

    private static final String ALGORITHM = "AES";
    private static final String DEFAULT_TOKEN = "token123";

    private static SecretKey generateSecretKey(String salt) {
        try {
            byte[] key = salt.getBytes("UTF-8");
            return new SecretKeySpec(key, 0, 16, ALGORITHM);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String data, String salt) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKey secretKey = generateSecretKey(salt);
            if (secretKey == null) {
                return DEFAULT_TOKEN;
            }

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes("UTF-8"));
            String encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);
            return encryptedString.replace("/", "");
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT_TOKEN;
        }
    }
}
