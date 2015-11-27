package com.onewifi.util;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.multipart.MultipartFile;

public class OneWifiUtil {
	
	public static Date getDateFromStr(String dateStr, String dateFormat) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		if (dateStr != null) {
			try {
				sdf.setLenient(false);
				java.util.Date dateObj = sdf.parse(dateStr);
				return dateObj;
			}
			catch(Exception e) {
				throw e;
			}
		}
		return null;
	}

	public static File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException 
	{
	        File convFile = new File( multipart.getOriginalFilename());
	        multipart.transferTo(convFile);
	        return convFile;
	}
	
	public static Date getEndOfDay(Date date) {
	    return DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DATE), -1);
	}

	public static Date getStartOfDay(Date date) {
	    return DateUtils.truncate(date, Calendar.DATE);
	}
	
	public static Date getWeekStartDate() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
		c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
	
		Date weekStart = c.getTime();
		return weekStart;
	}
	
	public static Date getWeekEndDate() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
		c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
	
		c.add(Calendar.DAY_OF_MONTH, 6); 
		Date weekEnd = c.getTime();
		return weekEnd;
	}	
	
	public static Date getLastDateOfMonth() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Calendar calendar = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH,
		calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}
	
	public static Date getFirstDateofMonth() {
	    Calendar c = Calendar.getInstance();   // this takes current date
	    c.set(Calendar.DAY_OF_MONTH, 1);
	    return c.getTime();
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
