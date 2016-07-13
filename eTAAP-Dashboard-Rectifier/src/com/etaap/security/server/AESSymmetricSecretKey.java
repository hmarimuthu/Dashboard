package com.etaap.security.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.etaap.security.EncryptionDecryptionAES;

public class AESSymmetricSecretKey {

	
	private static final byte[] keyMaterial = new byte[] { 'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		// TODO Auto-generated method stub
		SecretKey secretKey = null;
		AESSymmetricSecretKey keyFile = new AESSymmetricSecretKey();
		secretKey = (SecretKey)keyFile.generateKey();
		keyFile.saveSecretKey(secretKey);
	}
	
	
	private Key generateKey() throws NoSuchAlgorithmException {
	    Key key = new SecretKeySpec(keyMaterial, EncryptionDecryptionAES.AES_ALGORITHM);
//	    SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();		
//	    return secretKey;
		return key;	    
	}

	public void saveSecretKey(SecretKey secretKey) throws IOException {
		// TODO Auto-generated method stub
		File file = new File(EncryptionDecryptionAES.AES_SYMMETRIC_KEY_FILE_PATH);
		FileOutputStream outputStream = new FileOutputStream(file);
		ObjectOutputStream oout = new ObjectOutputStream(outputStream);
		try {
		  oout.writeObject(secretKey);
		} finally {
		  oout.close();
		}		
	}

}
