package com.onewifi.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.Date;

import com.onewifi.beans.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import org.apache.commons.codec.binary.Base64;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

public class OneWifiUtilityService {
		

	private final DevicemasterHome deviceMasterHome = DevicemasterHome.getInstance();
	private final RetailermasterHome retailermasterHome = RetailermasterHome.getInstance();
	private final RetailerdeviceHome retailerdeviceHome = RetailerdeviceHome.getInstance();
	private final PricingHome pricingHome = PricingHome.getInstance();
	private final CustomermasterHome customermasterHome = CustomermasterHome.getInstance();
	private final CustomerdeviceHome customerdeviceHome = CustomerdeviceHome.getInstance();
	private final StatuscodemasterHome statuscodemasterHome = StatuscodemasterHome.getInstance();
	
	static {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	}

	public String validateDevice(String loginId, String IMEI, String deviceSerialNo) throws Exception {
		String deviceStatus = null;
		try {	
			Customermaster customermaster = new Customermaster();
			customermaster.setLoginId(loginId);
			List<Customermaster> listCustomermaster = customermasterHome.findByExample(customermaster);
			for(Customermaster customermaster1 : listCustomermaster) {
				if(customermaster1!=null) {
					Devicemaster devicemaster = new Devicemaster();
					devicemaster.setImei(IMEI);
					devicemaster.setDeviceSerialNo(deviceSerialNo);
					List<Devicemaster> listDeviceMaster = deviceMasterHome.findByExample(devicemaster);
					for(Devicemaster devicemaster1 : listDeviceMaster) {
						if(devicemaster1!=null && devicemaster1.getImei().equals(IMEI) && devicemaster1.getDeviceSerialNo().equals(deviceSerialNo)) {
							CustomerdeviceId customerdeviceId = new CustomerdeviceId();
							customerdeviceId.setCustomerId(customermaster1.getCustomerId());
							customerdeviceId.setDeviceId(devicemaster1.getDeviceId());
							Customerdevice customerdevice = customerdeviceHome.findById(customerdeviceId);
							if(customerdevice!=null && customerdevice.getId()!=null && customerdevice.getStatuscodemaster()!=null) {									
								List<Statuscodemaster> listStatuscodemaster = statuscodemasterHome.findByExample(customerdevice.getStatuscodemaster());		
								for(Statuscodemaster statuscodemaster : listStatuscodemaster) {
									if(statuscodemaster!=null) {
										deviceStatus = statuscodemaster.getStatusDetail();				
									}
									break;
								}								
								break;
							}
							break;
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return deviceStatus;
	}	

	public Boolean validateIMEI(String IMEI) throws Exception {
		Boolean deviceValid = new Boolean(false);
		try {	
			Devicemaster devicemaster = new Devicemaster();
			devicemaster.setImei(IMEI);
			List<Devicemaster> listDeviceMaster = deviceMasterHome.findByExample(devicemaster);
			for(Devicemaster devicemaster1 : listDeviceMaster) {
				if(devicemaster1!=null && devicemaster1.getImei().equals(IMEI)) {
					deviceValid = new Boolean(true);
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return deviceValid;
	}
	
	public Boolean validateIMEIForRetailer(String loginId, String IMEI) throws Exception {
		Boolean deviceValidForRetailer = new Boolean(false);
		try {	
			Retailermaster retailermaster = new Retailermaster();
			retailermaster.setLoginId(loginId);
			List<Retailermaster> listRetailermaster = retailermasterHome.findByExample(retailermaster);
			for(Retailermaster retailermaster1: listRetailermaster) {
				System.out.println(IMEI);
				Devicemaster devicemaster = new Devicemaster();
				devicemaster.setImei(IMEI);
				List<Devicemaster> listDeviceMaster = deviceMasterHome.findByExample(devicemaster);
				for(Devicemaster devicemaster1 : listDeviceMaster) {
					if((devicemaster1!=null && devicemaster1.getImei().equals(IMEI)) && (devicemaster1.getStatuscodemaster()!=null && devicemaster1.getStatuscodemaster().getStatusCode().equalsIgnoreCase("S2"))) {
						System.out.println(retailermaster1.getRetailerId()+"=="+devicemaster1.getDeviceSerialNo());
						RetailerdeviceId retailerdeviceId = new RetailerdeviceId();
						retailerdeviceId.setAssignerId(retailermaster1.getRetailerId());							
						retailerdeviceId.setDeviceSerialNo(devicemaster1.getDeviceSerialNo());						
						Retailerdevice retailerdevice =  retailerdeviceHome.findById(retailerdeviceId);					
						if(retailerdevice!=null && retailerdevice.getPurpose().equalsIgnoreCase("Ownership")) {		
							System.out.println(retailermaster1.getRetailerId()+"=="+devicemaster1.getDeviceSerialNo());
							deviceValidForRetailer = new Boolean(true);
							break;
						}
					}
				}
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return deviceValidForRetailer;
	}		
	
	public Devicemaster getDeviceForIMEI(String IMEI) throws Exception {
		Devicemaster resultDevicemaster = null;
		try {	
			Devicemaster devicemaster = new Devicemaster();
			devicemaster.setImei(IMEI);
			List<Devicemaster> listDeviceMaster = deviceMasterHome.findByExample(devicemaster);
			for(Devicemaster devicemaster1 : listDeviceMaster) {
				if(devicemaster1!=null && devicemaster1.getImei().equals(IMEI)) {
					resultDevicemaster = devicemaster1;
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return resultDevicemaster;
	}	
	
	public Boolean validateCustomer(String loginId) throws Exception {
		Boolean customerExists = new Boolean(false);
		try {				
			if(customermasterHome.findById("C1"+loginId)!=null) {
				customerExists= new Boolean(true);
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return customerExists;
	}	
	
	public Boolean validateRetailer(String loginId) throws Exception {
		Boolean retailerValid = new Boolean(false);
		try {	
			Retailermaster retailermaster = new Retailermaster();
			retailermaster.setLoginId(loginId);
			List<Retailermaster> listRetailermaster = retailermasterHome.findByExample(retailermaster);
			for(Retailermaster retailermaster1 : listRetailermaster) {
				if(retailermaster1!=null && retailermaster1.getLoginId().equals(loginId)) {
					retailerValid = new Boolean(true);
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return retailerValid;
	}	
	
	public Boolean validateDeviceSerialNo(String deviceSerialNo) throws Exception {
		Boolean deviceValid = new Boolean(false);
		try {	
			Devicemaster devicemaster = new Devicemaster();
			devicemaster.setDeviceSerialNo(deviceSerialNo);
			List<Devicemaster> listDeviceMaster = deviceMasterHome.findByExample(devicemaster);
			for(Devicemaster devicemaster1 : listDeviceMaster) {
				if(devicemaster1!=null && devicemaster1.getDeviceSerialNo().equals(deviceSerialNo)) {
					deviceValid = new Boolean(true);
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return deviceValid;
	}	
	
	public Boolean validateRetailerId(String retailerId) throws Exception {
		Boolean retailerValid = new Boolean(false);
		try {	
			Retailermaster retailermaster = new Retailermaster();
			retailermaster.setRetailerId(retailerId);
			List<Retailermaster> listRetailermaster = retailermasterHome.findByExample(retailermaster);
			for(Retailermaster retailermaster1 : listRetailermaster) {
				if(retailermaster1!=null && retailermaster1.getRetailerId().equals(retailerId)) {
					retailerValid = new Boolean(true);
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return retailerValid;
	}		
	
	public Boolean validateRetailerDevice(RetailerdeviceId retailerdeviceId) throws Exception {
		Boolean retailerValid = new Boolean(false);
		try {	
			Retailerdevice retailerdevice = retailerdeviceHome.findById(retailerdeviceId);
			if(retailerdevice!=null) {
				retailerValid = new Boolean(true);
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return retailerValid;
	}	
	
	public Boolean validatePricing(PricingId pricingId) throws Exception {
		Boolean pricingValid = new Boolean(false);
		try {	
			Pricing pricing = pricingHome.findById(pricingId);
			if(pricing!=null) {
				pricingValid = new Boolean(true);
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		System.out.println(pricingValid);
		return pricingValid;
	}
	
	public String encryptNameAndLoginId(String fullName, String loginId, String IMEI) throws Exception {
		String result = "";
		try {
			StringBuffer cnt = new StringBuffer();
			
			String deviceSerialNo = null;
			try {	
				Devicemaster devicemaster = new Devicemaster();
				devicemaster.setImei(IMEI);
				List<Devicemaster> listDeviceMaster = deviceMasterHome.findByExample(devicemaster);
				for(Devicemaster devicemaster1 : listDeviceMaster) {
					if((devicemaster1!=null && devicemaster1.getImei().equals(IMEI)) && (devicemaster1.getStatuscodemaster()!=null && devicemaster1.getStatuscodemaster().getStatusCode().equalsIgnoreCase("S4"))) {
						deviceSerialNo = devicemaster1.getDeviceSerialNo();
						break;
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
				throw e;
			}
			System.out.println(deviceSerialNo);
			cnt.append(fullName);			
			cnt.append(",bcc_onewifi_encryption,");
			cnt.append(loginId);
			cnt.append(";");
			cnt.append(IMEI);
			cnt.append("|");
			cnt.append(deviceSerialNo);
			cnt.append("|");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			cnt.append(sdf.format(cal.getTime()));
			
			result = new String(Base64.encodeBase64(cnt.toString().getBytes()));
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	public String[] decryptNameAndLoginId(String encryptedText) throws Exception {
		String[] results = new String[3];
		try {
			encryptedText = new String(Base64.decodeBase64(encryptedText.getBytes()));
			
			results[0] = encryptedText.substring(encryptedText.lastIndexOf(",")+1, encryptedText.lastIndexOf(";"));
			results[1] = encryptedText.substring(encryptedText.lastIndexOf(";")+1, encryptedText.indexOf("|"));
			results[2] = encryptedText.substring(encryptedText.indexOf("|")+1, encryptedText.lastIndexOf("|"));
			System.out.println(encryptedText.indexOf("|")+1+"=="+results[0]+"=="+results[1]+"=="+results[2]);
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return results;
	}		
}