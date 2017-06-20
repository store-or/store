package com.core.util.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: hongxingshi
 * Date: 14-9-23
 * Time: 上午10:59
 * To change this template use File | Settings | File Templates.
 */
public final class AesUtils {
    private static final String AES = "AES";
    private static final String ENCODE = "UTF-8";

    private static final String IV = "12345678abcdefgh";
    private static final int KEY_SIZE = 16;
    public static final String KEY = "56212235226886758978225025933009171679";

    public static final String CIPHER_ALGORITHM="AES/CBC/PKCS5Padding";

    private AesUtils(){}

    public static String encryptBase64(String content, String password) throws EncryptException {
        byte keyPtr[] = new byte[KEY_SIZE];
        try {
            byte passPtr[] = password.getBytes(ENCODE);

            for (int i = 0; i < KEY_SIZE; i++) {
                if (i < passPtr.length){
                    keyPtr[i] = passPtr[i];
                }
                else {
                    keyPtr[i] = 0;
                }
            }
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyPtr, AES);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(IV.getBytes(ENCODE)));
            return Base64.encodeBase64String(cipher.doFinal(content.getBytes(ENCODE)));
        } catch (UnsupportedEncodingException e) {
            throw new EncryptException("UnsupportedEncodingException: " + e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptException("NoSuchPaddingException: " + e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new EncryptException("InvalidAlgorithmParameterException: " + e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException("NoSuchAlgorithmException: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            throw new EncryptException("IllegalBlockSizeException: " + e.getMessage(), e);
        } catch (BadPaddingException e) {
            throw new EncryptException("BadPaddingException: " + e.getMessage(), e);
        } catch (InvalidKeyException e) {
            throw new EncryptException("InvalidKeyException: " + e.getMessage(), e);
        }
    }

    public static String decryptBase64(String content, String password) throws EncryptException {
        byte keyPtr[] = new byte[KEY_SIZE];
        try {
            byte passPtr[] = password.getBytes(ENCODE);
            for (int i = 0; i < KEY_SIZE; i++) {
                if (i < passPtr.length){
                    keyPtr[i] = passPtr[i];
                }
                else {
                    keyPtr[i] = 0;
                }
            }
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyPtr, AES);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec,new IvParameterSpec(IV.getBytes(ENCODE)));
            byte[] base64 = Base64.decodeBase64(content);
            return new String(cipher.doFinal(base64), Charset.forName(ENCODE));
        } catch (UnsupportedEncodingException e) {
            throw new EncryptException("UnsupportedEncodingException: " + e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptException("NoSuchPaddingException: " + e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new EncryptException("InvalidAlgorithmParameterException: " + e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException("NoSuchAlgorithmException: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            throw new EncryptException("IllegalBlockSizeException: " + e.getMessage(), e);
        } catch (BadPaddingException e) {
            throw new EncryptException("BadPaddingException: " + e.getMessage(), e);
        } catch (InvalidKeyException e) {
            throw new EncryptException("InvalidKeyException: " + e.getMessage(), e);
        }
    }
}
