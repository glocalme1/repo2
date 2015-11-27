package com.onewifi.util;

import org.apache.commons.codec.binary.Base64;

public class EncryptionUtil {

	public static String encryptNameAndLoginId(String fullName, String loginId) {
		String result = "";
		try {
			StringBuffer cnt = new StringBuffer();
			cnt.append(fullName);			
			cnt.append(",bcc_onewifi_encryption,");
			cnt.append(loginId);
			result = new String(Base64.encodeBase64(cnt.toString().getBytes()));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String decryptNameAndLoginId(String encryptedText) {
		String loginId = "";
		try {
			encryptedText = new String(Base64.decodeBase64(encryptedText.getBytes()));
			loginId = encryptedText.substring(encryptedText.lastIndexOf(",")+1);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return loginId;
	}		
	
}
