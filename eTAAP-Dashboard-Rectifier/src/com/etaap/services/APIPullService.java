package com.etaap.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.etaap.security.EncryptionDecryptionAES;

public class APIPullService {
	
	private static final Logger logger = Logger.getLogger(APIPullService.class);
	
	public JSONObject readJsonFromUrl(String url, String user, String pass) {
		logger.info("Inside JiraDataPullAPI :: readJsonFromUrl()");

		InputStream is = null;
		JSONObject json = null;
	    try {
	    	URLConnection urlConnection = setUsernamePassword(new URL(url), user, pass);
            is = urlConnection.getInputStream();
	    	BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	    	String jsonText = readAll(rd);
//	    	System.out.println("******"+jsonText);
	    	json = new JSONObject(jsonText);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	logger.error("ERROR :: readJsonFromUrl() :IOException: " + e.getMessage());
	    } catch (JSONException e) {
	    	e.printStackTrace();
	    	logger.error("ERROR :: readJsonFromUrl() :JSONException: " + e.getMessage());
	    } finally {
	    	try {
	    		if (is != null) {
	    			is.close();
	    		}
	    	} catch (Exception e) {
	    		logger.error("ERROR :: readJsonFromUrl() :: " + e.getMessage());
			}
	    }

	    return json;
	}

	private URLConnection setUsernamePassword(URL url, String user, String password) throws IOException {
		try {
			if(password == null) {
				password = "";
			}
			password = password.trim();
			if(!password.equals("")) {
				EncryptionDecryptionAES encryptionDecryptionAES = new EncryptionDecryptionAES();
				password = encryptionDecryptionAES.decrypt(password);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			RuntimeException ex = new RuntimeException(e.getMessage());
			ex.setStackTrace(e.getStackTrace());
			throw ex;
		}
		
		URLConnection urlConnection = url.openConnection();
	    String authString = user + ":" + password;
	    
	    String authStringEnc = new String(org.springframework.security.crypto.codec.Base64.encode(authString.getBytes()));
	    urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);

	    return urlConnection;
	}

	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
	    while ((cp = rd.read()) != -1) {
	    	sb.append((char) cp);
	    }

	    return sb.toString();
	}

}
