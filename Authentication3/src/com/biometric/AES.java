package com.biometric;


import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Random;

public class AES {
	static byte[] skey = new byte[1000];
	public static MyResult AESencrypt(String inputMessage) {
		try {
			Random r = new Random();
			int num = r.nextInt(10000);
			String knum = String.valueOf(num);
			byte[] knumb = knum.getBytes();
            skey = getRawKey(knumb);
			byte[] ibyte = inputMessage.getBytes();
			byte[] ebyte = encrypt(skey, ibyte);
            String encryptedData = new String(ebyte);
            return new MyResult(skey, encryptedData);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;

    }
    public static String AESdecrypt(byte[] skey,String inputMessage) {
		try {
            byte[] ebyte = inputMessage.getBytes();
			byte[] dbyte = decrypt(skey, ebyte);
			String decryptedMessage = new String(dbyte);
			return decryptedMessage;
		} catch (Exception e) {
			System.out.println(e);
		}
		return inputMessage;
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
        byte[] raw;
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		kgen.init(128, sr); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		raw = skey.getEncoded();
		return raw;
	}

	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted)
			throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
    }
    
}