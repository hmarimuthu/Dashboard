package com.etaap.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Base64;

public class EncryptionDecryptionAES {
	
	public static final String AES_ALGORITHM = "AES";
	public static final String AES_SYMMETRIC_KEY_FILE_PATH = "E:\\EtouchProjects\\ETAAPFramework\\eTAAP-Dashboard\\WebContent\\WEB-INF\\AESSymmetricSecretKey";
	
	public static SecretKey secretKey;
	
	
	public String encrypt(String valueToEnc) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	    Key key = getSecretKey();
	    Cipher c = Cipher.getInstance(AES_ALGORITHM);
	    c.init(Cipher.ENCRYPT_MODE, key);
	    byte[] encValue = c.doFinal(valueToEnc.getBytes());
	//    String encryptedValue = new BASE64Encoder().encode(encValue);
	    String encryptedValue = Base64.encodeBase64String(encValue);
	    return encryptedValue;
	}
	
	public String decrypt(String encryptedValue) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	    Key key = getSecretKey();
	    Cipher c = Cipher.getInstance(AES_ALGORITHM);
	    c.init(Cipher.DECRYPT_MODE, key);
	//    byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
	    byte[] decordedValue = Base64.decodeBase64(encryptedValue);
	    byte[] decValue = c.doFinal(decordedValue);
	    String decryptedValue = new String(decValue);
	    return decryptedValue;
	}
	
	public static void initialiseSecretKey() throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		SecretKey key = null;
		
//		File file = new File(AES_SYMMETRIC_KEY_FILE_PATH);
		
		ClassLoader classLoader = EncryptionDecryptionAES.class.getClassLoader();
		File file = new File(classLoader.getResource("/com/etaap/security/AESSymmetricSecretKey").getFile());		
		
		FileInputStream inputStream = new FileInputStream(file);
		ObjectInputStream oin = new ObjectInputStream(inputStream);
		try {
			key = (SecretKey) oin.readObject();
			secretKey = key;
		} finally {
		  oin.close();
		}
	}	
	
	public static void main(String args[]) throws Exception {
		EncryptionDecryptionAES encryptionDecryptionAES = new EncryptionDecryptionAES();
		String encrypted = encryptionDecryptionAES.encrypt("admin");
		System.out.println("Encrypted '"+encrypted+"'");
		String decrypted = encryptionDecryptionAES.decrypt(encrypted);
		System.out.println("Decrypted '"+decrypted+"'");
		
	}
	
	public static SecretKey getSecretKey() {
		return secretKey;
	}
	
	
}