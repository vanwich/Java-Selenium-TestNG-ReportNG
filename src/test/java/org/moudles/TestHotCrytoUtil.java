package org.moudles;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class TestHotCrytoUtil {
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CTR/NoPadding";
    private static final String SHA_256 = "SHA-256";
    public static final String HOT_AUTH_TOKEN = "HOT-AUTH-TOKEN";
    public static final String KEY = "v0yp7MClE8Nd81Uhu6V@MOp4_HcH1npJK0JVuZIoEBG_hr>/mnlsX_<Z>Aqw1z:u";
    public static final String CONTENT = "%s|" + (System.currentTimeMillis() + 5 * 60 * 1000) / 1000;

    //    public static void main(String[] args) {
    //        System.out.println("Token is: " + decrypt("2-I/T015ZRhHIgyS1xqDozXrgBAzFrqC1Sm8QMjEk=", KEY));
    //    }

    public static void main(String[] args) {
        System.out.println("Token is: " + encrypt(String.format(CONTENT, "qa"), KEY));
    }

    public static synchronized String encrypt(String target) {
        return encrypt(String.format(CONTENT, target), KEY);
    }

    public static synchronized String encrypt(String content, String key) {
        if (content != null && !"".equals(content.trim()) && key != null && !"".equals(key.trim())) {
            try {
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
                String effectiveDateStr = dateformat.format(Long.parseLong(content.split("\\|")[1]) * 1000 - 5*60*1000);
                String expireDateStr = dateformat.format(Long.parseLong(content.split("\\|")[1]) * 1000);

                System.out.println("Effective Date format: " + effectiveDateStr);
                System.out.println("Expire Date format: " + expireDateStr);
                System.out.println("Content is: " + content);
                MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);
                messageDigest.update(key.getBytes(StandardCharsets.UTF_8));
                int maxAllowedKeyLength = Cipher.getMaxAllowedKeyLength(KEY_ALGORITHM) / 8;
                byte[] raw = slice(messageDigest.digest(), 0, maxAllowedKeyLength);
                SecretKeySpec secretKeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
                Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
                cipher.init(1, secretKeySpec);
                byte[] encryptedValue = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
                byte[] iv = cipher.getIV();
                return iv != null && iv.length != 0 ? "2-" + Base64.encodeBase64String(byteMerger(iv, encryptedValue)) : "1-" + Base64.encodeBase64String(encryptedValue);
            } catch (Exception var9) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String decrypt(String content, String key) {
        if (content != null && !"".equals(content.trim()) && key != null && !"".equals(key.trim())) {
            String separator = "-";
            int sepIndex = content.indexOf(separator);
            if (sepIndex < 0) {
                return decryptAESVersion0(content, key);
            } else {
                String version = content.substring(0, sepIndex);
                String data = content.substring(sepIndex + 1, content.length());
                if ("1".equals(version)) {
                    return decryptAESVersion1(data, key);
                } else {
                    return "2".equals(version) ? decryptAESVersion2(data, key) : "Unknown crypto version!";
                }
            }
        } else {
            return "";
        }
    }

    private static byte[] slice(byte[] src, int from, int until) {
        int lo = Math.max(from, 0);
        int hi = Math.min(Math.max(until, 0), src.length);
        int elems = Math.max(hi - lo, 0);
        byte[] b = new byte[elems];
        int i = lo;

        for (int j = 0; i < hi; ++j) {
            b[j] = src[i];
            ++i;
        }

        return b;
    }

    private static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    private static String decryptAESVersion0(String value, String privateKey) {
        try {
            byte[] raw = privateKey.substring(0, 16).getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(2, skeySpec);
            return new String(cipher.doFinal(Hex.decodeHex(value.toCharArray())));
        } catch (Exception var5) {
            return "";
        }
    }

    private static String decryptAESVersion1(String value, String privateKey) {
        try {
            byte[] data = Base64.decodeBase64(value);
            MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);
            messageDigest.update(privateKey.getBytes(StandardCharsets.UTF_8));
            int maxAllowedKeyLength = Cipher.getMaxAllowedKeyLength(KEY_ALGORITHM) / 8;
            byte[] raw = slice(messageDigest.digest(), 0, maxAllowedKeyLength);
            SecretKeySpec secretKeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(2, secretKeySpec);
            return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
        } catch (Exception var8) {
            return "";
        }
    }

    private static String decryptAESVersion2(String value, String privateKey) {
        try {
            byte[] data = Base64.decodeBase64(value);
            MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);
            messageDigest.update(privateKey.getBytes(StandardCharsets.UTF_8));
            int maxAllowedKeyLength = Cipher.getMaxAllowedKeyLength(KEY_ALGORITHM) / 8;
            byte[] raw = slice(messageDigest.digest(), 0, maxAllowedKeyLength);
            SecretKeySpec secretKeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            int blockSize = cipher.getBlockSize();
            byte[] iv = slice(data, 0, blockSize);
            byte[] payload = slice(data, blockSize, data.length);
            cipher.init(2, secretKeySpec, new IvParameterSpec(iv));
            return new String(cipher.doFinal(payload), StandardCharsets.UTF_8);
        } catch (Exception var11) {
            return "";
        }
    }
}
