package com.etaap.security;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class BasicAuthentication extends Authentication {
	
	
	/**API used into this method is implemented into httpclient-4.3.1.jar, httpcore-4.3.jar downloaded from apache site. This code
	 * is used to issue authentication preemptively. 
	 * Note: This method does NOT work with https URL.
	 * @throws ClientProtocolException, IOException
	 * @throws RuntimeException In case any of httpUrlString, userName or password are empty or null.
	 * @return String Contains response after successful authentication.
	 */

	public String getTextResponseUsingPreemptiveAuthentication() throws ClientProtocolException, IOException {
		
		if((this.getHttpUrlString() == null) && (this.getHttpUrlString().trim().equals(""))) {
			throw new RuntimeException("HTTP url must not be empty while doing Basic Preemptive Authentication.");
		}
		if((this.getUserName() == null) && (this.getUserName().trim().equals(""))) {
			throw new RuntimeException("Username must not be empty while doing Basic Preemptive Authentication.");
		}
		if((this.getPassword() == null) && (this.getPassword().trim().equals(""))) {
			throw new RuntimeException("Password must not be empty while doing Basic Preemptive Authentication.");
		}
		
		URI uri = URI.create(this.getHttpUrlString());
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		
		credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()), new UsernamePasswordCredentials(this.getUserName(), this.getPassword()));
		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();	
		authCache.put(host, basicAuth);
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		HttpGet httpGet = new HttpGet(uri);
		// Add AuthCache to the execution context
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);

		HttpResponse response = httpClient.execute(host, httpGet, localContext);

		return EntityUtils.toString(response.getEntity());
	}
	
	public String getResponseForJiraUsingAuthetication(String url) throws ClientProtocolException, IOException{	
		
		System.out.println("JIRA URL: " +url);
		if((this.getHttpUrlString() == null) && (this.getHttpUrlString().trim().equals(""))) {
			throw new RuntimeException("HTTP url must not be empty while doing Basic Preemptive Authentication.");
		}
		if((this.getUserName() == null) && (this.getUserName().trim().equals(""))) {
			throw new RuntimeException("Username must not be empty while doing Basic Preemptive Authentication.");
		}
		if((this.getPassword() == null) && (this.getPassword().trim().equals(""))) {
			throw new RuntimeException("Password must not be empty while doing Basic Preemptive Authentication.");
		}	
		HttpGet request = new HttpGet(url);
		//String auth = "rpandey" + ":" + "eTouch123$$";
		String auth = this.getUserName() + ":"+this.getPassword();
		System.out.println(auth);
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
		String authHeader = "Basic " + new String(encodedAuth);
		request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		 
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(request);
		 
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("statusCode: "+statusCode);
		return EntityUtils.toString(response.getEntity());		
		     
		
	}



	
}
