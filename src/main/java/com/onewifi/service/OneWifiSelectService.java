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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.HashMap;

public class OneWifiSelectService {
		
	private final DevicemasterHome deviceMasterHome = DevicemasterHome.getInstance();
	private final RetailermasterHome retailermasterHome = RetailermasterHome.getInstance();
	private final PricingHome pricingHome = PricingHome.getInstance();
	private final CustomermasterHome customermasterHome = CustomermasterHome.getInstance();
	private final PartnermasterHome partnermasterHome = PartnermasterHome.getInstance();
	private final StatuscodemasterHome statuscodemasterHome = StatuscodemasterHome.getInstance();
	private final GlocalmeClient client = new GlocalmeClient();
	
	
	static {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	}

	public Set<Customertopup> fetchCustomerTopupHistory(String loginId, Date fromDate, Date toDate) throws Exception {		
		Set<Customertopup> resultListCustomertopup = new HashSet<Customertopup>(0);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//fromDate = sdf.parse(sdf.format(fromDate));
			//toDate = sdf.parse(sdf.format(toDate));
			Date convertedTopupDate = null;
			Customermaster customermaster = new Customermaster();
			customermaster.setLoginId(loginId);
			List<Customermaster> listCustomermaster = customermasterHome.findByExample(customermaster);
			for(Customermaster customermaster1 : listCustomermaster) {
				if(customermaster1!=null) {
					Set<Customertopup> listCustomertopup = customermaster1.getCustomertopups();
					for(Customertopup customertopup : listCustomertopup) {					
						if(customertopup!=null && customertopup.getId()!=null) {
							convertedTopupDate = sdf.parse(sdf.format(customertopup.getId().getTopupDate()));
							System.out.println(convertedTopupDate+"=="+fromDate+"=="+toDate);
							if((fromDate!=null && toDate!=null && convertedTopupDate.after(fromDate) && convertedTopupDate.before(toDate)) || convertedTopupDate.equals(fromDate) || convertedTopupDate.equals(toDate) || (fromDate==null && toDate==null)) {
								customertopup.setLoginId(loginId);
								resultListCustomertopup.add(customertopup);
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
		return resultListCustomertopup;
	}

	public List<Pricing> fetchPricing() throws Exception {		
		List<Pricing> listPricing = null;
		try {
			listPricing = pricingHome.findLatestPricingDetails();
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return listPricing;
	}
	
	public List<Pricing> fetchPricing(Date validDate) throws Exception {		
		List<Pricing> listPricing = null;
		try {
			listPricing = pricingHome.findByDate(validDate);
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return listPricing;
	}
		
	
	public HashMap<String, List<String>> fetchPricingDetailsForTopup() throws Exception {		
		HashMap<String, List<String>> mapPricing = new HashMap<String, List<String>>();		
		try {
			List<Pricing> listPricing = fetchPricing();
			for(Pricing pricing: listPricing) {
				if(pricing!=null) {
					List<String> listPackages = new ArrayList<String>();
					listPackages.add(pricing.getDaily150mb());
					listPackages.add(pricing.getDays7450mb());
					listPackages.add(pricing.getDays301gb());
					listPackages.add(pricing.getDays902gb());
					listPackages.add(pricing.getDays1803gb());
					listPackages.add(pricing.getDay1Pass());
					listPackages.add(pricing.getDays5Pass());
					listPackages.add(pricing.getDays10Pass());
					listPackages.add(pricing.getDays20Pass());
					mapPricing.put(pricing.getId().getProductCode()+":"+pricing.getCountry(), listPackages);				
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return mapPricing;
	}

	public JSONObject fetchTrafficHistory(String IMEI, Date fromDate, Date toDate, int currentPage, int perPageCount) throws Exception {		
		JSONObject resultJSONObject = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String convertedFromDate = sdf.format(fromDate);
			String convertedToDate = sdf.format(toDate);
			Devicemaster devicemaster = new Devicemaster();
			devicemaster.setImei(IMEI);
			List<Devicemaster> listDeviceMaster = deviceMasterHome.findByExample(devicemaster);
			String userCode = null;
			String partnerCode = null;
			for(Devicemaster devicemaster1 : listDeviceMaster) {
				if(devicemaster1!=null && devicemaster1.getImei().equals(IMEI)) {
					partnerCode = devicemaster1.getPartnerCode();
					userCode = devicemaster1.getUserCode();
					break;
				}
			}
			resultJSONObject = client.invokeQueryUserBillingListInfo(partnerCode, userCode, convertedFromDate, convertedToDate, currentPage, perPageCount);	
			if(resultJSONObject!=null && !(resultJSONObject.getString("resultCode").equals("0000"))) {
				resultJSONObject = null;				
			}			
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return resultJSONObject;
	}

	public AccountSummaryBean fetchAccountSummary(String IMEI) throws Exception {		
		AccountSummaryBean resultAccountSummaryBean = new AccountSummaryBean();
		try {
			
			Calendar cal = Calendar.getInstance();
			Date toDate = cal.getTime();
			
			cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -12);
			Date fromDate = cal.getTime();
			
			int currentPage=1;
			int perPageCount=20;
			
			Date loginDate = null;
			Date latestDate = null;
			
			Devicemaster devicemaster = new Devicemaster();
			devicemaster.setImei(IMEI);
			List<Devicemaster> listDeviceMaster = deviceMasterHome.findByExample(devicemaster);
			String userCode = null;
			String partnerCode = null;
			for(Devicemaster devicemaster1 : listDeviceMaster) {
				if(devicemaster1!=null && devicemaster1.getImei().equals(IMEI)) {
					partnerCode = devicemaster1.getPartnerCode();
					userCode = devicemaster1.getUserCode();
					break;
				}
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			
			/*JSONObject resultJSONObject = client.invokeQueryRealTimePositionInfo(partnerCode, userCode, IMEI, 1);	
			if(resultJSONObject!=null && !(resultJSONObject.getString("resultCode").equals("0000"))) {
				resultJSONObject = null;				
			} else if(resultJSONObject!=null) {
				JSONObject userPositionInfo = resultJSONObject.getJSONObject("userPositionInfo");
				System.out.println(userPositionInfo);
				if(!userPositionInfo.isNullObject()) {
					resultAccountSummaryBean.setLatestLoginTime(sdf.parse(userPositionInfo.getString("loginTime")));
					resultAccountSummaryBean.setLatestLogoutTime(sdf.parse(userPositionInfo.getString("logoutTime")));
					resultAccountSummaryBean.setLatestPosition(userPositionInfo.getString("country")+","+userPositionInfo.getString("lac"));				
				}				
			}			

			
			String convertedFromDate = sdf.format(fromDate);
			String convertedToDate = sdf.format(toDate);

			
			resultJSONObject = client.invokeQueryHisPositionListInfo(partnerCode, userCode, IMEI, convertedFromDate, convertedToDate, currentPage, perPageCount, 1);	
			if(resultJSONObject!=null && !(resultJSONObject.getString("resultCode").equals("0000"))) {
				resultJSONObject = null;				
			} else if(resultJSONObject!=null) {
				JSONArray jsonArray = resultJSONObject.getJSONArray("userPositionList");
				int index = 0;				
				for(int i = 0;  jsonArray!=null && i < jsonArray.size(); i++)
				{		  
					System.out.println(jsonArray.getJSONObject(i));
					if(jsonArray.getJSONObject(i)!=null) {
						loginDate = sdf.parse(jsonArray.getJSONObject(i).getString("loginTime"));					
						if(i==0 || (latestDate!=null && loginDate.after(latestDate))) {
							latestDate = loginDate;
							index = i;
						}					
					}
				}
				if(jsonArray.getJSONObject(index)!=null) {
					resultAccountSummaryBean.setLastLoginTime(latestDate);
					resultAccountSummaryBean.setLastLogoutTime(sdf.parse(jsonArray.getJSONObject(index).getString("logoutTime")));
					resultAccountSummaryBean.setLastPosition(jsonArray.getJSONObject(index).getString("country")+","+jsonArray.getJSONObject(index).getString("lac"));
				}
			}
			
			JSONObject resultJSONObject = client.invokeQueryUserBillingListInfo(partnerCode, userCode, convertedFromDate, convertedToDate, currentPage, perPageCount);	
			if(resultJSONObject!=null &&  !(resultJSONObject.getString("resultCode").equals("0000"))) {
				resultJSONObject = null;				
			} else if(resultJSONObject!=null) {
				JSONArray jsonArray = resultJSONObject.getJSONArray("userBillingList");
				int index = 0;
				for(int i = 0;  jsonArray!=null && i < jsonArray.size(); i++)
				{		  
					System.out.println(jsonArray.getJSONObject(i));
					if(jsonArray.getJSONObject(i)!=null) {
						loginDate = sdf.parse(jsonArray.getJSONObject(i).getString("loginTime"));						
						if(i==0 || (latestDate!=null && loginDate.after(latestDate))) {
							latestDate = loginDate;
							index = i;
						}	
					}						
				}
				if(jsonArray.getJSONObject(index)!=null) {
					resultAccountSummaryBean.setTotalFlowInKB(jsonArray.getJSONObject(index).getDouble("totalFlows"));
					if(resultAccountSummaryBean.getTotalFlowInKB()!=0) {
						resultAccountSummaryBean.setTotalFlowInKB(Math.round(resultAccountSummaryBean.getTotalFlowInKB()/1024));
					}		
				}					
			}*/
			
			for(Devicemaster devicemaster1 : listDeviceMaster) {
				if(devicemaster1!=null && devicemaster1.getImei().equals(IMEI)) {
					Set<Customertopup> listCustomertopup = devicemaster1.getCustomertopups();
					int cnt = 0;		
					if(listCustomertopup.size()>0) {
						for(Customertopup customertopup1 : listCustomertopup) {
							if(customertopup1!=null) {
								if(cnt==0 || (customertopup1.getLatestLogoutDate()!=null && customertopup1.getLatestLogoutDate().after(latestDate))) {									
									latestDate = customertopup1.getLatestLogoutDate();
									System.out.println(latestDate);
									resultAccountSummaryBean.setLatestLoginTime(customertopup1.getLatestLoginDate());
									resultAccountSummaryBean.setLatestLogoutTime(customertopup1.getLatestLogoutDate());
									resultAccountSummaryBean.setLatestPosition(customertopup1.getLatestPosition());	
									resultAccountSummaryBean.setLatestTopupAmount(customertopup1.getTopupAmount());										
									if(customertopup1.getUsedAccData()!=null) {
										resultAccountSummaryBean.setTotalFlowInKB(customertopup1.getUsedAccData().doubleValue());
									} else {
										resultAccountSummaryBean.setTotalFlowInKB(0);
									}
									if(customertopup1.getUsedCurrentAmt()!=null) {
										resultAccountSummaryBean.setConsumedAmount(customertopup1.getUsedCurrentAmt().doubleValue());
									} else {
										resultAccountSummaryBean.setConsumedAmount(0);
									}							
								}
								cnt++;
							}
						}
					}
					break;
				}
			}
	System.out.println(resultAccountSummaryBean.getLatestLoginTime()+"=="+resultAccountSummaryBean.getLatestLogoutTime()+"=="+resultAccountSummaryBean.getLatestPosition()+"=="+resultAccountSummaryBean.getTotalFlowInKB()+"=="+resultAccountSummaryBean.getLatestTopupAmount()+"=="+resultAccountSummaryBean.getConsumedAmount());			
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return resultAccountSummaryBean;
	}
	
	public Partnermaster fetchPartnerDetails(String loginId) throws Exception {
		Partnermaster partnermasterResult = null;
		try {	
			Partnermaster partnermaster = new Partnermaster();
			partnermaster.setLoginId(loginId);
			List<Partnermaster> listPartnermaster = partnermasterHome.findByExample(partnermaster);
			for(Partnermaster partnermaster1 : listPartnermaster) {
				if(partnermaster1!=null) {
					partnermasterResult = partnermaster1;
				}
				break;
			}			
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return partnermasterResult;
	}

	public Retailermaster fetchRetailerDetails(String loginId) throws Exception {
		Retailermaster retailermasterResult = null;
		try {	
			Retailermaster retailermaster = new Retailermaster();
			retailermaster.setLoginId(loginId);
			List<Retailermaster> listRetailermaster = retailermasterHome.findByExample(retailermaster);
			for(Retailermaster retailermaster1 : listRetailermaster) {
				if(retailermaster1!=null) {
					retailermasterResult = retailermaster1;
				}
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return retailermasterResult;
	}

	public Customermaster fetchCustomerDetails(String loginId) throws Exception {
		Customermaster customermasterResult = null;
		try {	
			Customermaster customermaster = new Customermaster();
			customermaster.setLoginId(loginId);
			List<Customermaster> listCustomermaster = customermasterHome.findByExample(customermaster);
			for(Customermaster customermaster1 : listCustomermaster) {
				if(customermaster1!=null) {
					Set<Customerdevice> listCustomerDevices = customermaster1.getCustomerdevices();
					for(Customerdevice customerdevice : listCustomerDevices) {
						if(customerdevice!=null && customerdevice.getStatuscodemaster().getStatusCode().equals("S4")) {			
							Devicemaster deviceMaster = deviceMasterHome.findById(customerdevice.getId().getDeviceId());
							if(deviceMaster!=null) {
								customermasterResult = customermaster1;
								customermasterResult.setImei(deviceMaster.getImei());
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
		return customermasterResult;
	}
	
	public Customermaster fetchCustomerDetailsByCustomerId(String customerId) throws Exception {
		Customermaster customermasterResult = null;
		try {	
			customermasterResult = customermasterHome.findById(customerId);
			Set<Customerdevice> listCustomerDevices = customermasterResult.getCustomerdevices();
			for(Customerdevice customerdevice : listCustomerDevices) {
				if(customerdevice!=null && customerdevice.getStatuscodemaster().getStatusCode().equals("S4")) {			
					Devicemaster deviceMaster = deviceMasterHome.findById(customerdevice.getId().getDeviceId());
					if(deviceMaster!=null && deviceMaster.getStatuscodemaster().getStatusCode().equals("S4")) {
						customermasterResult.setImei(deviceMaster.getImei());
						break;
					}
				}
			}						
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return customermasterResult;
	}	
	
	public List<Devicemaster> fetchDevicesDetails(String status) throws Exception {
		List<Devicemaster> listDeviceMaster = null;
		try {	
			Statuscodemaster statuscodemaster = new Statuscodemaster();
			statuscodemaster.setStatusDetail(status);
			
			List<Statuscodemaster> listStatuscodemaster = statuscodemasterHome.findByExample(statuscodemaster);
			Devicemaster devicemaster = new Devicemaster();
			for(Statuscodemaster statuscodemaster1 : listStatuscodemaster) {
				if(statuscodemaster1!=null) {
					devicemaster.setStatuscodemaster(statuscodemaster1);
					listDeviceMaster = deviceMasterHome.findByStatus(statuscodemaster1);	
				}
				break;
			}
				
			
			if(listDeviceMaster!=null && status.equals("CustomerUsing")){
				System.out.println(listDeviceMaster.size());
				for(Devicemaster devicemaster1 : listDeviceMaster) {	
					if(devicemaster1!=null && devicemaster1.getCustomertopups()!=null) {
						Set<Customertopup> listCustomertopup = devicemaster1.getCustomertopups();
						List<Customertopup> resultCustomertopup = new ArrayList<Customertopup>(0);
						int packageCount = 0;
						int payAsYouGoCount = 0;
						System.out.println(devicemaster1.getDeviceId());
						for(Customertopup customertopup : listCustomertopup) {					
							if(customertopup!=null && customertopup.getExhaustedStatus().equalsIgnoreCase("UnFilled")) {
								if(customertopup.getTopupType().equalsIgnoreCase("Package")) {
									++packageCount;
								}
								if(customertopup.getTopupType().equalsIgnoreCase("PayAsYouGo@1MB")) {
									++payAsYouGoCount;
								}
								resultCustomertopup.add(customertopup);
								devicemaster1.setUnFilledCustomertopups(resultCustomertopup);	
								devicemaster1.setPackageCount(packageCount);								
								devicemaster1.setPayAsYouGoCount(payAsYouGoCount);								
							}
						}
						System.out.println(devicemaster1.getUnFilledCustomertopups().size());						
					}
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return listDeviceMaster;
	}	
	
	public String getDeviceBoundStatus(String IMEI) throws Exception {
		String deviceBoundStatus = null;
		try {	
			Devicemaster devicemaster = new Devicemaster();
			devicemaster.setImei(IMEI);
			List<Devicemaster> listDeviceMaster = deviceMasterHome.findByExample(devicemaster);
			for(Devicemaster devicemaster1 : listDeviceMaster) {
				if(devicemaster1!=null && devicemaster1.getImei().equals(IMEI)) {
					deviceBoundStatus = devicemaster1.getDeviceBoundStatus();
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return deviceBoundStatus;
	}
}