package com.core.util.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created with IntelliJ IDEA.
 * User: hongxingshi
 * Date: 14-9-24
 * Time: 上午9:39
 * To change this template use File | Settings | File Templates.
 */
public final class DesUtils {
    private final static String IV = "01234567";
    private final static String ENCODING = "utf-8";
    public static final String KEY = "56212235226886758978225025933009171679";

    private DesUtils(){}

    public static String encryptBase64(String plainText) throws EncryptException {
        try {
            DESedeKeySpec spec = new DESedeKeySpec(KEY.getBytes("utf-8"));

            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            Key deskey= keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(IV.getBytes("utf-8"));
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            byte[] encryptData = cipher.doFinal(plainText.getBytes(ENCODING));
            return Base64.encodeBase64String(encryptData);
        } catch (InvalidKeyException e) {
            throw new EncryptException("InvalidKeyException" + e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            throw new EncryptException("UnsupportedEncodingException" + e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptException("NoSuchPaddingException" + e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException("NoSuchAlgorithmException" + e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            throw new EncryptException("IllegalBlockSizeException" + e.getMessage(), e);
        } catch (BadPaddingException e) {
            throw new EncryptException("BadPaddingException" + e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new EncryptException("InvalidAlgorithmParameterException" + e.getMessage(), e);
        } catch (InvalidKeySpecException e) {
            throw new EncryptException("InvalidKeySpecException" + e.getMessage(), e);
        }
    }

    public static String decryptBase64(String encryptText) throws EncryptException {
        try {
            DESedeKeySpec spec = new DESedeKeySpec(KEY.getBytes("utf-8"));

            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            Key deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(IV.getBytes("utf-8"));
            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
            byte[] decryptData = cipher.doFinal(Base64.decodeBase64(encryptText));
            return new String(decryptData, ENCODING);
        } catch (InvalidKeyException e) {
            throw new EncryptException("InvalidKeyException" + e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            throw new EncryptException("UnsupportedEncodingException" + e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptException("NoSuchPaddingException" + e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException("NoSuchAlgorithmException" + e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            throw new EncryptException("IllegalBlockSizeException" + e.getMessage(), e);
        } catch (BadPaddingException e) {
            throw new EncryptException("BadPaddingException" + e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new EncryptException("InvalidAlgorithmParameterException" + e.getMessage(), e);
        } catch (InvalidKeySpecException e) {
            throw new EncryptException("InvalidKeySpecException" + e.getMessage(), e);
        }
    }
}
