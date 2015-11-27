package com.onewifi.service;

import java.io.IOException;
import java.math.BigInteger;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import net.sf.json.JSONObject;
import org.apache.http.Header;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.UnsupportedEncodingException;

public class GlocalmeClient {
 
	 public static void main(String[] args) throws Exception {

		GlocalmeClient client = new GlocalmeClient();
		//client.invokeUserBinding();
		client.invokeQuerySubUserListInfo();
		client.invokeQuerySubUserInfo();
		//client.invokeQueryRealTimePositionInfo();
		//client.invokeQueryHisPositionListInfo();
		client.invokeQueryUserPackageListInfo();
		//client.invokeQueryUserBillingListInfo();
	 }
	 
	 /*private void invokeUserBinding() throws Exception {
	
		  String jsonStr = "{\"streamNo\":\"TESTT20150921174025000001\",\"timeStamp\":\"20150921174110\",\"partnerCode\":\"onewifi@bcc.com.sg\",\"userCode\":\"onewifi06@bcc.com.sg\",\"imei\":\"356166060827382\",\"password\":\"d307a600\",\"operateType\":\"0\"}";
	*/  
	public JSONObject invokeUserBinding(String partnerCode, String userCode, String imei, String password, String operatorType) throws Exception {
	
		  String jsonStr = "{\"streamNo\":\"TESTT20150921174025000001\",\"timeStamp\":\"20150921174110\",\"partnerCode\":\""+partnerCode+"\",\"userCode\":\""+userCode+"\",\"imei\":\""+imei+"\",\"password\":\""+password+"\",\"operateType\":\""+operatorType+"\"}";
		  		  
		  JSONObject retObj = getResponse(jsonStr, "http://59.152.232.156:8080/api/UserBinding");
		  return retObj;
	 }
	 
	 private void invokeQuerySubUserListInfo() throws Exception {
		 
		String jsonStr = "{\"streamNo\":\"TESTT20150921174025000001\",\"timeStamp\":\"20150921174110\",\"partnerCode\":\"onewifi@bcc.com.sg\",\"currentPage\":\"1\",\"perPageCount\":\"5\"}";
		  
		JSONObject retObj = getResponse(jsonStr, "http://59.152.232.156:8080/api/QuerySubUserListInfo");
		System.out.println("\n\nHTTP POST Response:"+retObj.toString());
		 
	 }

	 private void invokeQuerySubUserInfo() throws Exception {
		 
		String jsonStr = "{\"streamNo\":\"TESTT20150921174025000001\",\"timeStamp\":\"20150921174110\",\"partnerCode\":\"onewifi@bcc.com.sg\",\"userCode\":\"onewifi06@bcc.com.sg\",\"imei\":\"356166060827382\",\"queryType\":\"1\"}";
		  
		JSONObject retObj = getResponse(jsonStr, "http://59.152.232.156:8080/api/QuerySubUserInfo");
		System.out.println("\n\nHTTP POST Response:"+retObj.toString());
		 
	 }

	 public JSONObject invokeQueryRealTimePositionInfo(String partnerCode, String userCode, String IMEI, String queryType) throws Exception {
		 
		String jsonStr = "{\"streamNo\":\"TESTT20150921174025000001\",\"timeStamp\":\"20150921174110\",\"partnerCode\":\""+partnerCode+"\",\"userCode\":\""+userCode+"\",\"imei\":\""+IMEI+"\",\"queryType\":\""+queryType+"\"}";
		  
		JSONObject retObj = getResponse(jsonStr, "http://59.152.232.156:8080/api/QueryRealTimePositionInfo");
		return retObj;
		 
	 }

	 public JSONObject invokeQueryHisPositionListInfo(String partnerCode, String userCode, String IMEI, String bgTime, String endTime, int currentPage, int perPageCount, int queryType) throws Exception {
		 
		String jsonStr = "{\"streamNo\":\"TESTT20150921174025000001\",\"timeStamp\":\"20150921174110\",\"partnerCode\":\""+partnerCode+"\",\"userCode\":\""+userCode+"\",\"imei\":\""+IMEI+"\",\"queryType\":\""+queryType+"\",\"bgTime\":\""+bgTime+"\",\"endTime\":\""+endTime+"\",\"currentPage\":\""+currentPage+"\",\"perPageCount\":\""+perPageCount+"\"}";
		  
		JSONObject retObj = getResponse(jsonStr, "http://59.152.232.156:8080/api/QueryHisPositionListInfo");
		return retObj;
		 
	 }

	 private void invokeQueryUserPackageListInfo() throws Exception {
		 
		String jsonStr = "{\"streamNo\":\"TESTT20150921174025000001\",\"timeStamp\":\"20150921174110\",\"partnerCode\":\"onewifi@bcc.com.sg\",\"userCode\":\"onewifi06@bcc.com.sg\",\"bgTime\":\"20150921174110\",\"endTime\":\"20150921174110\",\"currentPage\":\"1\",\"perPageCount\":\"5\"}";
		  
		JSONObject retObj = getResponse(jsonStr, "http://59.152.232.156:8080/api/QueryUserPackageListInfo");
		System.out.println("\n\nHTTP POST Response:"+retObj.toString());
		 
	 }

	public JSONObject invokeQueryUserBillingListInfo(String partnerCode, String userCode, String bgTime, String endTime, int currentPage, int perPageCount) throws Exception {
		 
		//String jsonStr = "{\"streamNo\":\"TESTT20150921174025000001\",\"timeStamp\":\"20150921174110\",\"partnerCode\":\"onewifi@bcc.com.sg\",\"userCode\":\"onewifi06@bcc.com.sg\",\"bgTime\":\"20150921174110\",\"endTime\":\"20150921174110\",\"currentPage\":\"1\",\"perPageCount\":\"5\"}";
		  
		String jsonStr = "{\"streamNo\":\"TESTT20150921174025000001\",\"timeStamp\":\"20150921174110\",\"partnerCode\":\""+partnerCode+"\",\"userCode\":\""+userCode+"\",\"bgTime\":\""+bgTime+"\",\"endTime\":\""+endTime+"\",\"currentPage\":\""+currentPage+"\",\"perPageCount\":\""+perPageCount+"\"}";
		  
		JSONObject retObj = getResponse(jsonStr, "http://59.152.232.156:8080/api/QueryUserBillingListInfo");
		return retObj;
	 }

	 private JSONObject getResponse(String jsonStr, String URL) throws Exception {
	
		JSONObject retObj = null;
		try {
			HttpClient client = HttpClientBuilder.create().build(); //new DefaultHttpClient();
			System.out.println("\n\nHTTP URL:"+URL);
			HttpPost post = new HttpPost(URL);

			StringEntity input = new StringEntity(jsonStr, "UTF-8");  
			input.setContentEncoding("UTF-8");
			input.setContentType("application/json");   
			post.setEntity(input);  
			  
			String factCode = "zIQbAaeg0v25GqDjKTQgsS6bz";		   
			  
			post.setHeader("keyCode", generateHash(jsonStr, factCode)); 
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");  
			  
			String reqSrc = EntityUtils.toString(post.getEntity());  
			JSONObject reqObj = new JSONObject().fromObject(reqSrc);
			System.out.println("\n\nHTTP POST Request:"+reqObj.toString());
				  
			HttpResponse response = client.execute(post);
			  
			HttpEntity entity = response.getEntity();
			String retSrc = EntityUtils.toString(response.getEntity());   
			retObj = new JSONObject().fromObject(retSrc);		
		} catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return retObj;
	 }
	 
	 private String generateHash(String jsonStr, String factCode) throws Exception {
		 
		String hashStr = null;
		try {
	      StringBuffer str = new StringBuffer();
		  str.append(jsonStr);
		  str.append(factCode);
		  hashStr = DigestUtils.md5Hex(str.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
		  e.printStackTrace();
		  throw e;
		}
		return hashStr; 
	 
	 }

}
