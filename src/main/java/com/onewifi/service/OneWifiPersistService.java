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
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

public class OneWifiPersistService {
		
	private final DevicemasterHome deviceMasterHome = DevicemasterHome.getInstance();
	private final UserroleHome userroleHome = UserroleHome.getInstance();
	private final PricingHome pricingHome = PricingHome.getInstance();
	private final CustomermasterHome customermasterHome = CustomermasterHome.getInstance();
	private final CustomerdeviceHome customerdeviceHome = CustomerdeviceHome.getInstance();
	private final CustomertopupHome customertopupHome = CustomertopupHome.getInstance();
	private final RetailermasterHome retailermasterHome = RetailermasterHome.getInstance();
	private final PartnermasterHome partnermasterHome = PartnermasterHome.getInstance();
	private final StatuscodemasterHome statuscodemasterHome = StatuscodemasterHome.getInstance();
	private final GlocalmeClient client = new GlocalmeClient();
	
	static {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	}

	public String saveCustomer(String loginId, Customermaster customermaster) throws Exception {
		String result = "Customer details cannot be saved";
		try {
			OneWifiUtilityService oneWifiUtilityService = new OneWifiUtilityService();
			Statuscodemaster statuscode = new Statuscodemaster();
			statuscode.setStatusCode("S12");
			customermaster.setCreationDate(new java.util.Date());
			customermaster.setSecretQuestion1("What is my first school name");
			customermaster.setSecretQuestionAnswer1("testing");
			customermaster.setSecretQuestion2("What is my best friend name");
			customermaster.setSecretQuestionAnswer2("testing");
			customermaster.setStatuscodemaster(statuscode);
			customermaster.setCustomerId("C1"+customermaster.getLoginId());
			customermaster.setPassword(" ");
			
			Boolean isCustomerExists = oneWifiUtilityService.validateCustomer(customermaster.getLoginId());
			System.out.println("Customer Exists:"+isCustomerExists);
			if(isCustomerExists!=null && !isCustomerExists.booleanValue()) {
				Boolean isIMEIValidForRetailer = oneWifiUtilityService.validateIMEIForRetailer(loginId, customermaster.getImei());
				System.out.println("isIMEIValidForRetailer="+isIMEIValidForRetailer);
				if(isIMEIValidForRetailer!=null && isIMEIValidForRetailer.booleanValue()) {
					Devicemaster resultDevicemaster = oneWifiUtilityService.getDeviceForIMEI(customermaster.getImei());
					if(resultDevicemaster!=null && resultDevicemaster.getStatuscodemaster().getStatusCode().equals("S2")) {						
						customermasterHome.persist(customermaster);
					} else {
						result = "IMEI is used by another user";
						return result;
					}
				} else {
					result = "IMEI not valid for this Retailer";
					return result;
				}
			} else {
				result = "Customer LoginId already exists";
				return result;
			}

			Userrole userrole = new Userrole();
			userrole.setLoginId(customermaster.getLoginId());
			userrole.setRoleName("User");
			userroleHome.persist(userrole);		
			
			CustomerdeviceId customerdeviceId = null;
			Customerdevice customerdevice = null;
			
			Devicemaster devicemaster = new Devicemaster();
			devicemaster.setImei(customermaster.getImei());
			List<Devicemaster> listDeviceMaster = deviceMasterHome.findByExample(devicemaster);
			Statuscodemaster statuscode1 = new Statuscodemaster();
			statuscode1.setStatusCode("S4");
			for(Devicemaster devicemaster1 : listDeviceMaster) {
				if(devicemaster1!=null) {
					devicemaster1.setStatuscodemaster(statuscode1);
					deviceMasterHome.merge(devicemaster1);
					
					customerdeviceId = new CustomerdeviceId();
					customerdevice = new Customerdevice();
					customerdeviceId.setCustomerId(customermaster.getCustomerId());
					customerdeviceId.setDeviceId(devicemaster1.getDeviceId());
					customerdevice.setId(customerdeviceId);
					customerdevice.setStatuscodemaster(statuscode1);
					customerdeviceHome.persist(customerdevice);
				}
			}			
			result = "Customer saved successfully";
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	public String updateCustomer(Customermaster customermaster) throws Exception {
		String result = "Customer details cannot be modified";
		try {
			customermasterHome.merge(customermaster);
			result = "SUCCESS";
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}	
	
	public String updateRetailer(Retailermaster retailermaster) throws Exception {
		String result = "Retailer details cannot be modified";
		try {
			retailermasterHome.merge(retailermaster);
			result = "SUCCESS";
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}	
	
	public String updatePartner(Partnermaster partnermaster) throws Exception {
		String result = "Partner details cannot be modified";
		try {
			partnermasterHome.merge(partnermaster);
			result = "SUCCESS";
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}	
	
	public String changeDeviceStatus(String loginId, String IMEI, String newStatus) throws Exception {
		String result = "Device Status cannot be modified";
		try {
			
			Statuscodemaster statuscodemaster = new Statuscodemaster();
			statuscodemaster.setStatusDetail(newStatus);			
			List<Statuscodemaster> listStatuscodemaster = statuscodemasterHome.findByExample(statuscodemaster);		
			statuscodemaster = null;
			for(Statuscodemaster statuscodemaster1 : listStatuscodemaster) {
				if(statuscodemaster1!=null) {
					statuscodemaster = statuscodemaster1;				
				}
				break;
			}
			
			Customermaster customermaster = new Customermaster();
			customermaster.setLoginId(loginId);
			List<Customermaster> listCustomermaster = customermasterHome.findByExample(customermaster);
			for(Customermaster customermaster1 : listCustomermaster) {
				if(customermaster1!=null) {
					Devicemaster devicemaster = new Devicemaster();
					devicemaster.setImei(IMEI);
					List<Devicemaster> listDeviceMaster = deviceMasterHome.findByExample(devicemaster);
					for(Devicemaster devicemaster1 : listDeviceMaster) {
						if(devicemaster1!=null && devicemaster1.getImei().equals(IMEI)) {
							CustomerdeviceId customerdeviceId = new CustomerdeviceId();
							customerdeviceId.setCustomerId(customermaster1.getCustomerId());
							customerdeviceId.setDeviceId(devicemaster1.getDeviceId());
							Customerdevice customerdevice = customerdeviceHome.findById(customerdeviceId);
							if(customerdevice!=null && customerdevice.getId()!=null) {
								devicemaster1.setStatuscodemaster(statuscodemaster);	
								deviceMasterHome.merge(devicemaster1);
								
								customerdevice.setStatuscodemaster(statuscodemaster);	
								customerdeviceHome.merge(customerdevice);									
								break;
							}
							break;
						}
					}
				}
			}
			result = "Device Status updated successfully";
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}		
	
	public String updateCustomerDevice(Customerdevice customerdevice) throws Exception {
		String result = "Customer Device cannot be modified";
		try {			
			customerdeviceHome.merge(customerdevice);	
			result = "Customer Device updated successfully";
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}	
	
	public String changeDeviceBoundStatus(String IMEI, String newStatus) throws Exception {
		String result = "Device Bound Status cannot be modified";
		try {
			
			Devicemaster devicemaster = new Devicemaster();
			devicemaster.setImei(IMEI);
			List<Devicemaster> listDeviceMaster = deviceMasterHome.findByExample(devicemaster);
			for(Devicemaster devicemaster1 : listDeviceMaster) {
				if(devicemaster1!=null && devicemaster1.getImei().equals(IMEI)) {
					devicemaster1.setDeviceBoundStatus(newStatus);
					deviceMasterHome.merge(devicemaster1);
					break;
				}				
			}
			result = "Device Bound Status updated successfully";
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}	
	
	public String saveCustomerTopup(Customertopup customertopup) throws Exception {
		String result = "Topup ";
		try {
			
			Calendar cal=Calendar.getInstance(TimeZone.getDefault());	
			List<Pricing> listPricing = pricingHome.findLatestPricingDetails();
			for(Pricing pricing: listPricing) {
				if(pricing!=null) {
					customertopup.setPriceListDate(pricing.getId().getValidDate());
				}
				break;
			}			
			Customermaster customermaster = new Customermaster();
			customermaster.setLoginId(customertopup.getLoginId());
			List<Customermaster> listCustomermaster = customermasterHome.findByExample(customermaster);
			for(Customermaster customermaster1 : listCustomermaster) {
				if(customermaster1!=null) {
					Set<Customerdevice> listCustomerDevices = customermaster1.getCustomerdevices();
					for(Customerdevice customerdevice : listCustomerDevices) {
						if(customerdevice!=null && customerdevice.getStatuscodemaster().getStatusCode().equals("S4")) {
							CustomertopupId customertopupId = new CustomertopupId();
							customertopupId.setCustomerId(customerdevice.getId().getCustomerId());
							customertopupId.setDeviceId(customerdevice.getId().getDeviceId());
							customertopupId.setTopupDate(cal.getTime());
							String selectedPackage = customertopup.getSelectedPackage();
							if(selectedPackage!=null && selectedPackage.lastIndexOf(":")>0) {
								customertopup.setTopupAmount(Double.parseDouble(selectedPackage.substring(selectedPackage.lastIndexOf(":")+1)));
								System.out.println("customertopup.getTopupAmount(=="+customertopup.getTopupAmount());
							}
							customertopup.setId(customertopupId);
							customertopup.setCurrency("USD");
							customertopup.setExhaustedStatus("unFilled");					
							
							Devicemaster devicemaster = deviceMasterHome.findById(customerdevice.getId().getDeviceId());
							JSONObject retObj = client.invokeUserBinding(devicemaster.getPartnerCode(), devicemaster.getUserCode(), devicemaster.getImei(), devicemaster.getPassword(), "0");
							System.out.println(retObj);
							if(retObj!=null && retObj.getString("resultCode").equals("0000")) {									
								customertopupHome.persist(customertopup);							
									
								Statuscodemaster statuscode = new Statuscodemaster();
								statuscode.setStatusCode("S5");
								devicemaster.setStatuscodemaster(statuscode);
								devicemaster.setDeviceBoundStatus("BIND");									
								deviceMasterHome.merge(devicemaster);													
								result = result + "for "+ customertopup.getCurrency() + " " + customertopup.getTopupAmount() + " has been completed";
							}							
							break;
						}
					}
				}
				break;
			}		
		} catch(Exception e) {
			result = result + " was not successful";
			e.printStackTrace();
			throw e;
		}
		return result;
	}	
	
	public Boolean updateCustomerTopup(Customertopup customertopup) throws Exception {
		Boolean result = new Boolean(false);
		try {
			Customermaster customermaster = new Customermaster();
			customermaster.setLoginId(customertopup.getLoginId());
			List<Customermaster> listCustomermaster = customermasterHome.findByExample(customermaster);
			for(Customermaster customermaster1 : listCustomermaster) {
				if(customermaster1!=null) {
					Set<Customertopup> listCustomertopup = customermaster1.getCustomertopups();
					for(Customertopup customertopup1 : listCustomertopup) {
						if(customertopup!=null && customertopup.getId()!=null) {
							if(customertopup.getId().getCustomerId().equals(customertopup1.getId().getCustomerId()) && customertopup.getId().getDeviceId().equals(customertopup1.getId().getDeviceId()) && customertopup.getId().getTopupDate().equals(customertopup1.getId().getTopupDate())) {
								customertopupHome.merge(customertopup);
								result = new Boolean(true);
								break;					
							}
						}
					}
				}
				break;
			}		
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
}