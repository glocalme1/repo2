package com.onewifi.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.glocalme.service.dto.AccountSummaryDto;
import com.glocalme.service.dto.AddressDto;
import com.glocalme.service.dto.CustomerDto;
import com.glocalme.service.dto.PackagePricingDto;
import com.glocalme.service.dto.TopupHistoryDto;
import com.glocalme.service.dto.TrafficHistoryDto;
import com.glocalme.service.dto.UserLogin;
import com.google.gson.Gson;
import com.onewifi.beans.Customermaster;
import com.onewifi.beans.Customertopup;
import com.onewifi.beans.Partnermaster;
import com.onewifi.beans.Pricing;
import com.onewifi.beans.PricingHome;
import com.onewifi.beans.Retailermaster;
import com.onewifi.constants.AppConstants;
import com.onewifi.service.AccountSummaryBean;
import com.onewifi.service.OneWifiService;
import com.onewifi.util.DateSerializer;
import com.onewifi.util.OneWifiUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Controller
@Scope("request")
@RequestMapping("/personal")
public class PersonalInfoController {

	@Autowired	
	private OneWifiService oneWifiService;
	
	final static Logger logger = Logger.getLogger(PersonalInfoController.class);	
	
	@RequestMapping(value="/update", method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> updateCustomer(@RequestBody CustomerDto customerDto) {
    	logger.info("PersonalInfoController - (/update) updateCustomer - Start");
		Map<String,Object> result = new HashMap<String,Object>();
		String updateStatus = null;
		try {
			String validationResult = validateCustomerUpdate(customerDto);
			if (validationResult != null) {
				result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
				result.put(AppConstants.RESPONSE_MESSAGE, validationResult);
				return result;
			}
			
			Customermaster customermaster = new Customermaster();
			
			updateStatus = oneWifiService.updateCustomer(customermaster);
			if ("SUCCESS".equalsIgnoreCase(updateStatus)) {
				result.put(AppConstants.RESPONSE_STATUS, AppConstants.CUSTOMER_UPDATE_SUCCESSMSG);
			} else {
				result.put(AppConstants.RESPONSE_STATUS, updateStatus);
			} 
		} catch(Exception e) {
			logger.error("PersonalInfoController - (/update) updateCustomer - Failed");
			logger.error(e.getMessage());
			e.printStackTrace();
			result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
			result.put(AppConstants.RESPONSE_MESSAGE, e.toString());			
		}
		return result;
    }	
	
    @RequestMapping(value = "/getinfo", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody CustomerDto getInformation(@RequestParam("login") String loginId,
    						HttpServletRequest request) {
    	System.out.println("PersonalInfoController - Get Information");
		String loggedInUserRole = null;
    	UserLogin userLogin = (UserLogin) request.getSession().getAttribute("LOGGEDIN_USER");
		if (userLogin != null) {
			if (userLogin.isAuthenticated()) {
				loggedInUserRole = userLogin.getRole();
			}
		}
		CustomerDto customerDto = null;
		try {
			customerDto = getPersonDetails(loginId,loggedInUserRole);
			userLogin.setImie(customerDto.getImei());
			request.getSession().setAttribute("LOGGEDIN_USER",userLogin);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Exception "+e);
		}
		return customerDto;
    }
    
    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    public @ResponseBody String validateDevice(@RequestParam("imei") String imei,
            @RequestParam("serialno") String serialNo, 
            @RequestParam("login") String loginId ) {
    	String resultStatus = null;
    	try {
    		resultStatus = oneWifiService.validateDevice(loginId, imei, serialNo); 
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
       	if (resultStatus!=null && resultStatus.equalsIgnoreCase("Bought")) {
    		try {
				oneWifiService.changeDeviceStatus(loginId, imei, "Assigned");
			} catch (Exception e) {
				e.printStackTrace();
				return "FAILURE";				
			}			
    		return "SUCCESS";       		
    	} else {
    		return "FAILURE";
    	}
    }
    
    @RequestMapping(value = "/setpassword", method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> changePassword(@RequestParam("passwd") String password,
            @RequestParam("login") String loginId ) {
    	Boolean result = false;
    	logger.info("Change Password - Called");
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	String statusMsg = "Password Changed Successfully";
    	try {
    		result = oneWifiService.changePassword(loginId, password);
    	} catch(Exception e) {
    		e.printStackTrace();
    		statusMsg = e.getMessage();
    	}
    	if (result == true) {
    		resultMap.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_SUCCESS);
    	} else {
    		resultMap.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
    	}
		resultMap.put(AppConstants.RESPONSE_MESSAGE, statusMsg);
    	return resultMap;
    }    
    
    @RequestMapping(value = "/topuphistory", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody Map<String,Object> getTopupHistory(@RequestParam("fromDate") String fromDateStr,
            @RequestParam("toDate") String toDateStr, HttpServletRequest request) {
    	logger.info("getTopupHistory - Called , fromDate: " + fromDateStr + ",toDate:"+toDateStr);
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	String statusMsg = "Topup History Fetched Successfully";
    	Set<Customertopup> topupHistoryList = null;
    	try {
    		
        	Date fromDate = new Date(Long.parseLong(fromDateStr)); // OneWifiUtil.getDateFromStr(fromDateStr, "yyyy-MM-dd HH:mm:ss");
        	Date toDate = new Date(Long.parseLong(toDateStr)); // OneWifiUtil.getDateFromStr(toDateStr, "yyyy-MM-dd HH:mm:ss");
        	
        	UserLogin userLogin = (UserLogin) request.getSession().getAttribute("LOGGEDIN_USER");
        	String loginId = null;
    		if (userLogin != null) {
    			if (userLogin.isAuthenticated()) {
    				loginId = userLogin.getUserName();
    				if (loginId == null) {
    					loginId = "test1@gmail.com";
    				}
    			}
    		}
    		
    		logger.info("loginId:"+loginId+",fromDate:"+fromDate+",ToDate:"+toDate);
    		topupHistoryList = oneWifiService.fetchCustomerTopupHistory(loginId, fromDate, toDate);

    		System.out.println("Number of TopupHistory Records: "+topupHistoryList.size());
    		if (topupHistoryList != null && topupHistoryList.size() > 0) {
    			List<TopupHistoryDto> topupHistoryResult = new ArrayList<TopupHistoryDto>();
    			for (Customertopup customerTopup: topupHistoryList) {
    				topupHistoryResult.add(getCustomerTopupHistoryDto(customerTopup));
    			}
    			resultMap.put(AppConstants.RESPONSE_DATAKEY, topupHistoryResult);
    			resultMap.put("TOTALRECORDCOUNT", topupHistoryList.size());
    		}
    		else {
    			resultMap.put("TOTALRECORDCOUNT", 0);
    		}
    		
    		// Convert topupHistoryList to Front-End DTOs
    		resultMap.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_SUCCESS);
    	} catch(Exception e) {
    		e.printStackTrace();
    		statusMsg = e.getMessage();
    		resultMap.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
    	}
    	resultMap.put(AppConstants.RESPONSE_MESSAGE, statusMsg);
    	return resultMap;
    }
    
    @RequestMapping(value = "/topup", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody Map<String,Object> saveTopup(
    		@RequestParam(value="selectedPackage", required= false) String packageStr,
            @RequestParam(value="selectedCountry", required= false) String country, 
            @RequestParam(value="topupAmount", required= false) String topupamout,
            @RequestParam(value="type", required= false) String topupTypeSelected,
            @RequestParam(value="packagecost", required= false) String packagecost,              
            HttpServletRequest request) {
    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    	System.out.println(topupamout);
    	System.out.println(topupTypeSelected);
    	logger.info("Save Topup Method is called");
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	String statusMsg = "Topup History Fetched Successfully";
    	
    	UserLogin userLogin = (UserLogin) request.getSession().getAttribute("LOGGEDIN_USER");
    	String loginId = null;
		if (userLogin != null) {
			if (userLogin.isAuthenticated()) {
				loginId = userLogin.getUserName();
			}
		}    	

		String validationResult = validateInputForTopup(packageStr,country,topupamout,topupTypeSelected);
		if (validationResult != null) {
    		resultMap.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
    		resultMap.put(AppConstants.RESPONSE_MESSAGE, validationResult);	    
    		return resultMap;
		}
	    		
    	Customertopup customerTopup = new Customertopup();
    	customerTopup.setLoginId(loginId);
    	String topupType = null;
    	if (topupTypeSelected != null) {
    		if ("1".equalsIgnoreCase(topupTypeSelected)) {
    			topupType = "PayAsYouGo";
    			customerTopup.setTopupAmount(Double.parseDouble(topupamout));
    		} else if ("2".equalsIgnoreCase(topupTypeSelected)) {
    			topupType = "Package";
    			customerTopup.setPackageCountry(country);
    			customerTopup.setSelectedPackage(packageStr);
    			customerTopup.setTopupAmount(Double.parseDouble(packagecost));
    		} 
    	}
		customerTopup.setTopupType(topupType);
		
		try {
			statusMsg = oneWifiService.saveCustomerTopup(customerTopup);
			if (statusMsg != null && statusMsg.contains("completed")) {
				statusMsg = "Topup Done Successfully";
	    		resultMap.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_SUCCESS);
	    		resultMap.put(AppConstants.RESPONSE_MESSAGE, statusMsg);	    		
			} else {
	    		resultMap.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
	    		resultMap.put(AppConstants.RESPONSE_MESSAGE, statusMsg);	    					
			}
		} catch(Exception e) {
    		e.printStackTrace();
    		statusMsg = e.getMessage();
    		resultMap.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
    		resultMap.put(AppConstants.RESPONSE_MESSAGE, statusMsg);
		}
		
    	return resultMap;
    }
    
    private String validateInputForTopup(
    		String packageStr, 
    		String country,
    		String topupamout,
    		String topupTypeSelected) {
    	
    	if (topupTypeSelected == null || topupTypeSelected.trim().length() == 0 || "undefined".equalsIgnoreCase(topupTypeSelected)) {
    		return "Top-up Type is mandatory";
    	}
    	
    	if ("1".equalsIgnoreCase(topupTypeSelected)) {
    		if (topupamout == null || "undefined".equalsIgnoreCase(topupamout)) {
    			return "Topup Amount is mandatory";
    		} else {
    			try {
    				Double a = Double.parseDouble(topupamout);
    			} catch(Exception e) {
    				return "Topup Amount is not valid";
    			}
    		}
    	}
    	if ("2".equalsIgnoreCase(topupTypeSelected)) {
        	if (packageStr == null || packageStr.trim().length() == 0 || "undefined".equalsIgnoreCase(packageStr) || "-1".equalsIgnoreCase(packageStr.trim())) {
        		return "Choose a valid Package";
        	}    	
        	if (country == null || country.trim().length() == 0 || "undefined".equalsIgnoreCase(country) || "-1".equalsIgnoreCase(country.trim())) {
        		return "Choose a valid Country";
        	}            	
    	}

    	return null;
    }
    
    @RequestMapping(value = "/traffichistory", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody Map<String,Object> getTrafficHistory(
    		@RequestParam(value="fromDate", required= false) String fromDateStr,
            @RequestParam(value="toDate", required= false) String toDateStr, 
            @RequestParam("currentPage") String currentPageStr, 
            @RequestParam("perPageCountStr") String perPageCountStr, 
            @RequestParam("requestType") String requestType, 
            HttpServletRequest request) {
    	logger.info("getTrafficHistory - Called , fromDate: " + fromDateStr + ",toDate:"+toDateStr);
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	String statusMsg = "Traffic History Fetched Successfully";
    	JSONObject trafficHistory = null;
    	try {
        	Date fromDate = null;
        	Date toDate = null;
    		if ("DateRange".equals(requestType)) {
    			Long fromDateMilliseconds = Long.parseLong(fromDateStr);
    			Long toDateMilliseconds = Long.parseLong(toDateStr);
    			fromDate = new Date(fromDateMilliseconds);
    			toDate = new Date(toDateMilliseconds);
    		} else if ("Today".equals(requestType)) {
    			fromDate = OneWifiUtil.getStartOfDay(new Date());
    			toDate =  OneWifiUtil.getEndOfDay(new Date());
    		} else if ("Week".equals(requestType)) {
    			fromDate = OneWifiUtil.getWeekStartDate();
    			toDate = OneWifiUtil.getWeekEndDate();
    		} else if ("Month".equals(requestType)) {
    			fromDate = OneWifiUtil.getFirstDateofMonth();
    			toDate = OneWifiUtil.getLastDateOfMonth();
    		}
        	int currentPage = Integer.parseInt(currentPageStr);
        	int perPageCount = Integer.parseInt(perPageCountStr);
        	
        	UserLogin userLogin = (UserLogin) request.getSession().getAttribute("LOGGEDIN_USER");
        	String imie = null;
    		if (userLogin != null) {
    			if (userLogin.isAuthenticated()) {
    				imie = userLogin.getImie();
    				if (imie == null) { imie = "356166060296562"; } // TODO: Hard-coded for Testing if imie is not set
    			}
    		}
    		
    		logger.info("Calling FetchTraffic History, imie: "+ imie+ ",fromDate:"+
    						fromDate+",toDate:"+toDate+",currentPage:"+currentPage+",perPagecount:"+perPageCount);
    		
    		trafficHistory = oneWifiService.fetchTrafficHistory(imie, fromDate, toDate, currentPage, perPageCount);
    		
    		if (trafficHistory != null) {
    			JSONArray billingList = null;  
    			List<TrafficHistoryDto> trafficHistoryList = null;
    			if (trafficHistory.has("userBillingList")) {
    				try {
    					billingList =  trafficHistory.getJSONArray("userBillingList");
    				}catch(Exception e) {}
	    			trafficHistoryList = new ArrayList<TrafficHistoryDto>();
	    			
	    			if (billingList != null && 	billingList.size() > 0 ) {
		    			for(int i = 0; i < billingList.size(); i++)
		    			{
		    			      JSONObject billingListItem = billingList.getJSONObject(i);
		    			      trafficHistoryList.add(getTrafficHistoryDtoFromJSON(billingListItem));	
		    			}
	    			}
    			}
    			if (billingList != null && 	billingList.size() > 0 ) {
    				resultMap.put(AppConstants.RESPONSE_DATAKEY, trafficHistoryList);
    			} else if (billingList == null || 	billingList.size() == 0 ) {
        			resultMap.put("TOTALPAGECOUNT",0);
        			resultMap.put("TOTALRECORDCOUNT",0);        				
    			}
    			
    			int totalPageCount = trafficHistory.getInt("totalPageCount");
    			int totalRecordCount = trafficHistory.getInt("totalCount");

    			resultMap.put("TOTALPAGECOUNT",totalPageCount);
    			resultMap.put("TOTALRECORDCOUNT",totalRecordCount);    
    		}
    		
    		if (trafficHistory == null) {
    			resultMap.put("TOTALPAGECOUNT",0);
    			resultMap.put("TOTALRECORDCOUNT",0);    			
    		}
    		
    		resultMap.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_SUCCESS);
    	} catch(Exception e) {
    		e.printStackTrace();
    		statusMsg = e.getMessage();
    		resultMap.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
    	}
    	resultMap.put(AppConstants.RESPONSE_MESSAGE, statusMsg);
    	return resultMap;
    }    
    
    @RequestMapping(value = "/accountsummary", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody Map<String,Object> getAccountSummary(HttpServletRequest request) {
    	logger.info("PersonalInfoController - (/accountsummary) getAccountSummary - Start");
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	String statusMsg = "Account Summary Fetched Successfully";
    	AccountSummaryDto accountSummary = null;
    	try {
        	UserLogin userLogin = (UserLogin) request.getSession().getAttribute("LOGGEDIN_USER");
        	String imie = null;
    		if (userLogin != null) {
    			if (userLogin.isAuthenticated()) {
    				imie = userLogin.getImie();
    				if (imie == null) { imie = "356166060296562"; } // TODO: Hard-coded for Testing if imie is not set	
    			}
    		}
    		logger.info("Calling FetchAccountSummary with IMEI "+ imie);
    		// Convert accountSummary to Front-End DTOs    		
    		accountSummary = getAccountSummaryDto(oneWifiService.fetchAccountSummary(imie));
    		resultMap.put(AppConstants.RESPONSE_DATAKEY, accountSummary);

    		resultMap.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_SUCCESS);
    	} catch(Exception e) {
    		e.printStackTrace();
    		logger.error("Error in getAccountSummary: "+e.getMessage());
    		statusMsg = e.getMessage();
    		resultMap.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
    	}
    	resultMap.put(AppConstants.RESPONSE_MESSAGE, statusMsg);
    	logger.info("PersonalInfoController - (/accountsummary) getAccountSummary - End");    	
    	return resultMap;    	
    }
    
    @RequestMapping(value = "/gettopup", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody Map<String,Object> getTopupPricing() {
    	logger.info("PersonalInfoController - (/gettopup) getTopupPricing - Called");
    	System.out.println("Get Topup Pricing is called");
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	String statusMsg = "Pricing Details Fetched Successfully";
    	try{
    		
    		Map<String,List<String>> packageMaster = new HashMap<String,List<String>>() ; // Country, List of Packages   		
    		Map<String,List<PackagePricingDto>> packageInfoList = new HashMap<String,List<PackagePricingDto>>(); // Country, Package Pricing
 
    		PricingHome pricingHome = new PricingHome();
    		List<Pricing> listPricing = pricingHome.findLatestPricingDetails();
    		
    		
    		Map<String,String> payasYouGo = getPayAsGoPricing(listPricing); // Country, Rates
    		Map<String,String> packagePricing = getPackagePricing(listPricing); // Package, Rates
    		Map<String,List<String>> countryPackages = getCountryPackages(listPricing); // Country and List of Packages 
    		
    		if (countryPackages != null && countryPackages.size() > 0) {
    			List<String> countriesWithPackages = new ArrayList<String>();
				for (Map.Entry<String, List<String>> entry : countryPackages.entrySet())
				{
					countriesWithPackages.add(entry.getKey());
				}
				resultMap.put("COUNTRYWITHPACKAGES",countriesWithPackages);  
    		}
    		
    		
    		if (payasYouGo == null) {
    			resultMap.put("PAYASYOUGO_COUNT",0);
    		} else {
    			resultMap.put("PAYASYOUGO_COUNT",payasYouGo.size());
    		}
    				
    		if (packagePricing == null) {
    			resultMap.put("PACKAGEPRICING_COUNT",0);
    		} else {
    			resultMap.put("PACKAGEPRICING_COUNT",packagePricing.size());
    		}
    		
    		if (countryPackages == null) {
    			resultMap.put("COUNTRYPACKAGES_COUNT",0);
    		} else {
    			resultMap.put("COUNTRYPACKAGES_COUNT",countryPackages.size());
    		}    		
    		
    		resultMap.put("PAYASYOUGO",payasYouGo);
    		resultMap.put("COUNTRYPACKAGES",countryPackages);    
    		resultMap.put("PACKAGEPRICING", packagePricing);
    		resultMap.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_SUCCESS);
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    		statusMsg = e.getMessage();
    		resultMap.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
    	}
    	resultMap.put(AppConstants.RESPONSE_MESSAGE, statusMsg);
    	return resultMap;
    }
    
    public Map<String,List<String>> getCountryPackages(List<Pricing> listPricing) {
    	Map<String,List<String>> packageInfo= new HashMap<String,List<String>>();
    	if (listPricing != null && listPricing.size() > 0) {
			for(Pricing pricing: listPricing) {
				Map<String,String> productMap = getProductMap(pricing,false);
				String country = pricing.getCountry();				
				for (Map.Entry<String, String> entry : productMap.entrySet())
				{
					if (packageInfo.get(country) != null) {
						packageInfo.get(country).add(entry.getKey());
					} else {
						List<String> packageListForCountry = new ArrayList<String>();
						packageListForCountry.add(entry.getKey());
						packageInfo.put(country,packageListForCountry);
					}
				}
			}
    	} 
    	return packageInfo;
    }
    
    public Map<String,String> getPackagePricing(List<Pricing> listPricing) {
    	Map<String,String> packageInfo= new HashMap<String,String>();
    	if (listPricing != null && listPricing.size() > 0) {
			for(Pricing pricing: listPricing) {
				Map<String,String> productMap = getProductMap(pricing,true);
				if (productMap != null && productMap.size() > 0) {
					packageInfo.putAll(productMap);
				}
			}
    	}    
    	return packageInfo;
    }
    
    public Map<String,String> getProductMap(Pricing pricing, boolean appendCountry) {
    	Map<String,String> products = new HashMap<String,String>();
    	String[] product1 = (pricing.getDaily150mb() != null && ((pricing.getDaily150mb().split(":")).length==3) ) ? pricing.getDaily150mb().split(":"): null;
    	String[] product2 = (pricing.getDays7450mb() != null && ((pricing.getDays7450mb().split(":")).length==3) ) ? pricing.getDays7450mb().split(":"): null;
    	String[] product3 = (pricing.getDays301gb() != null && ((pricing.getDays301gb().split(":")).length==3) ) ? pricing.getDays301gb().split(":"): null;
    	String[] product4 = (pricing.getDays902gb() != null && ((pricing.getDays902gb().split(":")).length==3) ) ? pricing.getDays902gb().split(":"): null;
    	String[] product5 = (pricing.getDays1803gb() != null && ((pricing.getDays1803gb().split(":")).length==3) ) ? pricing.getDays1803gb().split(":"): null;    	
    	
    	List<String[]> productList = new ArrayList<String[]>();
    	if (product1 != null) {productList.add(product1);}
    	if (product2 != null) {productList.add(product2);}
    	if (product3 != null) {productList.add(product3);}
    	if (product4 != null) {productList.add(product4);}
    	if (product5 != null) {productList.add(product5);}

    	for (String[] arr: productList) {
    		String key = null;
    		if (appendCountry) {
    			key = pricing.getCountry() + ":" + arr[0] + ":" + arr[1];
    		} else {
    			key = arr[0] + ":" + arr[1];
    		}
    		String value = arr[2];
    		products.put(key, value);
    	}
    	return products;
    }
    
    public Map<String,String> getPayAsGoPricing(List<Pricing> listPricing) {
    	Map<String,String> payasYouGo = new HashMap<String,String>();
    	if (listPricing != null && listPricing.size() > 0) {
			for(Pricing pricing: listPricing) {
				payasYouGo.put(pricing.getCountry(),""+pricing.getPayAsYouGo());
			}
    	}
    	return payasYouGo;
    }
    
    
    
    public CustomerDto getPersonDetails(String loginId, String loggedInUserRole) throws Exception {
		CustomerDto customerDto = null;
		if (AppConstants.ROLE_USER.equalsIgnoreCase(loggedInUserRole)) {
	    	Customermaster customerMaster = null;
			try {
				customerMaster = oneWifiService.fetchCustomerDetails(loginId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			customerDto = getCustomerDto(customerMaster,AppConstants.ROLE_USER);
		}
		else if(AppConstants.ROLE_PARTNER.equalsIgnoreCase(loggedInUserRole)) {
			Partnermaster partnerMaster = null;
			try {
				partnerMaster = oneWifiService.fetchPartnerDetails(loginId);
			} catch (Exception e) {
				e.printStackTrace();
			}			
			customerDto = getCustomerDto(partnerMaster,AppConstants.ROLE_PARTNER); 
		}
		else if(AppConstants.ROLE_RETAILER.equalsIgnoreCase(loggedInUserRole)) {
			Retailermaster retailerMaster = null;
			try {
				retailerMaster = oneWifiService.fetchRetailerDetails(loginId);
			} catch (Exception e) {
				e.printStackTrace();
			}			
			customerDto = getCustomerDto(retailerMaster,AppConstants.ROLE_RETAILER);
		}
		return customerDto;
    }
    
    private CustomerDto getCustomerDto(Object personDetails, String roleName) {
    	CustomerDto customerDto = new CustomerDto();
    	if (AppConstants.ROLE_PARTNER.equalsIgnoreCase(roleName)) {
    		Partnermaster partnerMaster = (Partnermaster) personDetails;
    		String fullName = partnerMaster.getFirstName() + " " + partnerMaster.getLastName();
    		customerDto.setFirstName(partnerMaster.getFirstName());
    		customerDto.setLastName(partnerMaster.getLastName());
    		customerDto.setFullAddress(partnerMaster.getAddress());
    		customerDto.setEmail(partnerMaster.getLoginId());
    		customerDto.setPartnerId(partnerMaster.getPartnerId());
    		customerDto.setFullName(fullName);
    	}
    	else if (AppConstants.ROLE_RETAILER.equalsIgnoreCase(roleName)) {
    		Retailermaster retailerMaster = (Retailermaster) personDetails;
    		customerDto.setFullName(retailerMaster.getCoyRetailerName());
    		customerDto.setMobileNumber(""+retailerMaster.getRetailerContactNumber());
    		customerDto.setFullAddress(retailerMaster.getAddress());
    		customerDto.setRetailerId(retailerMaster.getRetailerId());
    		customerDto.setEmail(retailerMaster.getLoginId());
    		customerDto.setDirectorName(retailerMaster.getDirectorName());
    		//customerDto.setCoyName(retailerMaster.getCoyRetailerName());
    		customerDto.setCoyContactNumber(""+retailerMaster.getCoyContactNumber());
    	}
    	else if (AppConstants.ROLE_USER.equalsIgnoreCase(roleName)) {
    		Customermaster customerMaster = (Customermaster) personDetails;
			customerDto.setFullName(customerMaster.getFullName());
			customerDto.setImei(customerMaster.getImei());
			customerDto.setMobileNumber(""+customerMaster.getContactNo());
			customerDto.setIdType(customerMaster.getIdentityType());
			customerDto.setIdNumber(customerMaster.getIdentityId());
			customerDto.setEmail(customerMaster.getLoginId());
			try{
				String addressStr = customerMaster.getAddress();
				Gson gson = new Gson();  
				AddressDto addressDto = gson.fromJson(addressStr,AddressDto.class);
				customerDto.setAddressLine1(addressDto.getAddressLine1());
				customerDto.setAddressLine2(addressDto.getAddressLine2());
				customerDto.setAddressLine3(addressDto.getAddressLine3());
				customerDto.setCountry(addressDto.getSelectedCountry());
				customerDto.setCity(addressDto.getCity());
				customerDto.setState(addressDto.getState());
				customerDto.setZipCode(addressDto.getZipCode());
			} catch(Exception e) {
			}
			
    	}
    	return customerDto;
    }    
    
    private TrafficHistoryDto getTrafficHistoryDtoFromJSON(JSONObject billingListItem) {
    	/*
    	Sample Output:
    	{"billingId":4464574,"userCode":"onewifi001@bcc.com","loginTime":"20151022055528","logoutTime":"20151022060408","country":"CN","totalFlows":288027,"upFlows":0,"downFlows":0,"charge":0.01},
    	*/
    	
    	TrafficHistoryDto trafficHistory = new TrafficHistoryDto();
    	trafficHistory.setBillingId(billingListItem.getInt("billingId"));
    	trafficHistory.setCountryCode(billingListItem.getString("country"));
    	String loginTimeStr = billingListItem.getString("loginTime");
    	System.out.println("LoginTimeStr::"+loginTimeStr);
    	if (loginTimeStr != null) {
    		try {
        		trafficHistory.setLoginTime(OneWifiUtil.getDateFromStr(loginTimeStr, "yyyyMMddHHmmss"));    			
    		} catch(Exception e) {
    			System.out.println(e.toString());
    		}
    	}
    	
    	String logoutTimeStr = billingListItem.getString("logoutTime");
    	System.out.println("LogoutTimeStr::"+logoutTimeStr);    	
    	if (logoutTimeStr != null) {
    		try {
    			trafficHistory.setLogoutTime(OneWifiUtil.getDateFromStr(logoutTimeStr, "yyyyMMddHHmmss"));    		
    		} catch(Exception e) {
    			System.out.println(e.toString());
    		}    		
    	}
    	trafficHistory.setTotalFlows(billingListItem.getLong("totalFlows"));
    	trafficHistory.setUserCode(billingListItem.getString("userCode"));
    	
    	System.out.println(">>>"+ReflectionToStringBuilder.toString(trafficHistory));
    	return trafficHistory;
    }
    
    private TopupHistoryDto getCustomerTopupHistoryDto(Customertopup customerTopup) {
    	TopupHistoryDto topupHistoryDto = new TopupHistoryDto();
    	topupHistoryDto.setCurrency(customerTopup.getCurrency());
    	topupHistoryDto.setTopupType(customerTopup.getTopupType());
    	topupHistoryDto.setPackageCountry(customerTopup.getPackageCountry());    	
    	topupHistoryDto.setSelectedPackage(customerTopup.getSelectedPackage());
    	topupHistoryDto.setPriceListDate(customerTopup.getPriceListDate());
    	topupHistoryDto.setTopupAmount(customerTopup.getTopupAmount());
    	return topupHistoryDto;
    }
    
    private AccountSummaryDto getAccountSummaryDto(AccountSummaryBean accountSummaryBean) {
    	AccountSummaryDto accountSummaryDto = new AccountSummaryDto();
    	accountSummaryDto.setLatestLoginTime(accountSummaryBean.getLatestLoginTime());
    	accountSummaryDto.setLatestLogoutTime(accountSummaryBean.getLatestLogoutTime());
    	accountSummaryDto.setLatestPosition(accountSummaryBean.getLatestPosition());
    	accountSummaryDto.setLastTopupAmount(accountSummaryBean.getLatestTopupAmount());
    	accountSummaryDto.setConsumedAmount(accountSummaryBean.getConsumedAmount());
    	accountSummaryDto.setTotalFlowInKB(accountSummaryBean.getTotalFlowInKB());
   	
    	return accountSummaryDto;
    }
    
    public String validateCustomerUpdate(CustomerDto customerdto) {
    	return null;
    }    
}
