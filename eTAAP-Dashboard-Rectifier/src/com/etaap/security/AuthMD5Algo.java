package com.etaap.security;

import java.security.MessageDigest;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.keygen.KeyGenerators;

public class AuthMD5Algo {
	
	private static final Logger logger = Logger.getLogger(AuthMD5Algo.class);
	
	public String authenticateUserPassword(String dbPassword,String loginPassword) {
		logger.info("Inside AuthMD5Algo :: authenticateUserPassword()");
		try {
			String salt = dbPassword.substring(0,16);
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(loginPassword.getBytes());

			byte byteData[] = md.digest();

			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			String generatedLoginPass = salt+sb.toString();
			if (generatedLoginPass.equals(dbPassword)) {
				return generatedLoginPass;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String generateMD5Password(String passwordClearTxt) throws Exception{
		 String salt = KeyGenerators.string().generateKey(); //"50fe0348b3d0a731"; - For Alok
			String password = "user";

			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());

			byte byteData[] = md.digest();

			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			/* System.out.println("Digest(in hex format):: " + sb.toString());
			 * convert the byte to hex format method 2
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				String hex = Integer.toHexString(0xff & byteData[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			System.out.println("Digest(in hex format):: " + hexString.toString()); 
			System.out.println("New Generated Password is "+salt+sb.toString());*/
			return salt+sb.toString();
	}
	
	public static void main(String[] args) throws Exception {
	   AuthMD5Algo.generateMD5Password("user");
	}
}
