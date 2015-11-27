package com.onewifi.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;

public class GenerateHash {
 
	 public static void main(String[] args) throws Exception {

		GenerateHash client = new GenerateHash();
		System.out.println("Password Hash:"+client.generateHash(args[0]));		
	 }
	 
	 public static String generateHash(String password) throws Exception {
		 
		String hashStr = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.reset();			
			StringBuffer psd = new StringBuffer();
			psd.append("bcc_onewifi");
			psd.append(password);
			md.update((psd.toString()).getBytes("UTF-8")); // Change this to "UTF-16" if needed
			byte[] digest = md.digest();
			hashStr = new String(Base64.encodeBase64(digest));
		} catch (Exception e) {
		  e.printStackTrace();
		  throw e;
		}
		return hashStr; 
	 
	 }

}
