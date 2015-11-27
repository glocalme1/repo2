package com.onewifi.service;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.onewifi.beans.Customerdevice; 
import com.onewifi.beans.Customermaster;
import com.onewifi.beans.Customertopup;
import com.onewifi.beans.Devicemaster;
import com.onewifi.beans.Partnermaster;
import com.onewifi.beans.Pricing;
import com.onewifi.beans.PricingId;
import com.onewifi.beans.RetailerdeviceId;
import com.onewifi.beans.Retailermaster;
import com.onewifi.beans.Userrole;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class OneWifiService {
		
	private final OneWifiUploadService oneWifiUploadService =  new OneWifiUploadService();
	private final OneWifiPersistService oneWifiPersistService = new OneWifiPersistService();
	private final OneWifiSelectService oneWifiSelectService = new OneWifiSelectService();
	private final OneWifiUtilityService oneWifiUtilityService = new OneWifiUtilityService();
	private final OneWifiUserService oneWifiUserService = new OneWifiUserService();
	private final GlocalmeClient client = new GlocalmeClient();
	
	static {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT")); 
	}

	public String uploadDevices(File[] files) throws Exception {
		return oneWifiUploadService.uploadDevices(files);
	}
	
	public String uploadRetailers(File[] files) throws Exception {
		return oneWifiUploadService.uploadRetailers(files); 
	}
	
	public String uploadRetailerDevices(File[] files) throws Exception {
		return oneWifiUploadService.uploadRetailerDevices(files);
	}
	
	public String uploadPricing(File[] files) throws Exception {
		return oneWifiUploadService.uploadPricing(files);
	}
			
	public File[] getFiles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return oneWifiUploadService.getFiles(request, response);
	}

	public String saveCustomer(String retailerLoginId, Customermaster customermaster) throws Exception {
		return oneWifiPersistService.saveCustomer(retailerLoginId, customermaster);
	}
	
	public String updateCustomer(Customermaster customermaster) throws Exception {
		return oneWifiPersistService.updateCustomer(customermaster);
	}	
	
	public String updateRetailer(Retailermaster retailermaster) throws Exception {
		return oneWifiPersistService.updateRetailer(retailermaster);
	}	
	
	public String updatePartner(Partnermaster partnermaster) throws Exception {
		return oneWifiPersistService.updatePartner(partnermaster);
	}	
	
	public String changeDeviceStatus(String loginId, String IMEI, String newStatus) throws Exception {
		return oneWifiPersistService.changeDeviceStatus(loginId, IMEI, newStatus);
	}
	
	public String updateCustomerDevice(Customerdevice customerdevice) throws Exception {
		return oneWifiPersistService.updateCustomerDevice(customerdevice);
	}
	
	public String changeDeviceBoundStatus(String IMEI, String newStatus) throws Exception {
		return oneWifiPersistService.changeDeviceBoundStatus(IMEI, newStatus);
	}
	
	public String saveCustomerTopup(Customertopup customertopup) throws Exception {
		return oneWifiPersistService.saveCustomerTopup(customertopup);
	}	
	
	public Boolean updateCustomerTopup(Customertopup customertopup) throws Exception {
		return oneWifiPersistService.updateCustomerTopup(customertopup);
	}
	
	public Set<Customertopup> fetchCustomerTopupHistory(String loginId, Date fromDate, Date toDate) throws Exception {		
		return oneWifiSelectService.fetchCustomerTopupHistory(loginId, fromDate, toDate);
	}

	public List<Pricing> fetchPricing() throws Exception {		
		return oneWifiSelectService.fetchPricing();
	}
	
	public List<Pricing> fetchPricing(Date validDate) throws Exception {		
		return oneWifiSelectService.fetchPricing(validDate);
	}
	
	public HashMap<String, List<String>> fetchPricingDetailsForTopup() throws Exception {		
		return oneWifiSelectService.fetchPricingDetailsForTopup();
	}

	public JSONObject fetchTrafficHistory(String IMEI, Date fromDate, Date toDate, int currentPage, int perPageCount) throws Exception {		
		return oneWifiSelectService.fetchTrafficHistory(IMEI, fromDate, toDate, currentPage, perPageCount);		
	}

	public AccountSummaryBean fetchAccountSummary(String IMEI) throws Exception {		
		return oneWifiSelectService.fetchAccountSummary(IMEI);
	}
	
	public Partnermaster fetchPartnerDetails(String loginId) throws Exception {
		return oneWifiSelectService.fetchPartnerDetails(loginId);
	}

	public Retailermaster fetchRetailerDetails(String loginId) throws Exception {
		return oneWifiSelectService.fetchRetailerDetails(loginId);
	}

	public Customermaster fetchCustomerDetails(String loginId) throws Exception {
		return oneWifiSelectService.fetchCustomerDetails(loginId);
	}
	
	public Customermaster fetchCustomerDetailsByCustomerId(String customerId) throws Exception {
		return oneWifiSelectService.fetchCustomerDetailsByCustomerId(customerId);
	}
	
	public List<Devicemaster> fetchDevicesDetails(String status) throws Exception {
		return oneWifiSelectService.fetchDevicesDetails(status); 
	}		
	
	public String getDeviceBoundStatus(String IMEI) throws Exception {
		return oneWifiSelectService.getDeviceBoundStatus(IMEI); 
	}
	
	public Userrole authenticateUser(String loginId, String password) throws Exception {
		return oneWifiUserService.authenticateUser(loginId, password);
	}
	
	public Boolean changePassword(String loginId, String password) throws Exception {
		return oneWifiUserService.changePassword(loginId, password);
	}

	public String encryptNameAndLoginId(String fullName, String loginId, String IMEI) throws Exception {
		return oneWifiUtilityService.encryptNameAndLoginId(fullName, loginId, IMEI);
	}
	
	public String[] decryptNameAndLoginId(String encryptedText) throws Exception {
		return oneWifiUtilityService.decryptNameAndLoginId(encryptedText);
	}
	
	public Boolean validateCustomer(String loginId) throws Exception {
		return oneWifiUtilityService.validateCustomer(loginId);
	}

	public String validateDevice(String loginId, String IMEI, String deviceSerialNo) throws Exception {
		return oneWifiUtilityService.validateDevice(loginId, IMEI, deviceSerialNo);
	}	
		
	public Boolean validateIMEI(String loginId, String IMEI) throws Exception {
		return oneWifiUtilityService.validateIMEI(IMEI);
	}	
	 
	public Boolean validateIMEIForRetailer(String loginId, String IMEI) throws Exception {
		return oneWifiUtilityService.validateIMEIForRetailer(loginId, IMEI);
	}
	
	public Devicemaster getDeviceForIMEI(String IMEI) throws Exception {
		return oneWifiUtilityService.getDeviceForIMEI(IMEI);
	}
	
	public Boolean validateRetailer(String loginId) throws Exception {
		return oneWifiUtilityService.validateRetailer(loginId);
	}	
	
	public Boolean validateDeviceSerialNo(String deviceSerialNo) throws Exception {
		return oneWifiUtilityService.validateDeviceSerialNo(deviceSerialNo);
	}	
	
	public Boolean validateRetailerId(String retailerId) throws Exception {
		return oneWifiUtilityService.validateRetailerId(retailerId);
	}		
	
	public Boolean validateRetailerDevice(RetailerdeviceId retailerdeviceId) throws Exception {
		return oneWifiUtilityService.validateRetailerDevice(retailerdeviceId);
	}	
	
	public Boolean validatePricing(PricingId pricingId) throws Exception {
		return oneWifiUtilityService.validatePricing(pricingId);
	}
	
	public JSONObject invokeUserBinding(String partnerCode, String userCode, String imei, String password, String operatorType) throws Exception {
		return client.invokeUserBinding(partnerCode, userCode, imei, password, operatorType);
	}

	public JSONObject invokeQueryRealTimePositionInfo(String partnerCode, String userCode, String imei, String operatorType) throws Exception {
		return client.invokeQueryRealTimePositionInfo(partnerCode, userCode, imei, operatorType);
	}	
	
	public String getTextFromImage(byte[] imageFileContent) throws Exception {		
		//return OCRRestAPI.getTextFromImage(imageFileContent);
		return null;
	}

	enum packageContents{PayAs,Day,DayPricing};
	
	public double[] parseString(String selectedPackage, String delims) throws ParseException//,int dayPackage,int  dataPackage, double dataMultiplier, double pricePackage) throws ParseException	
	{  //PK1:0-PayAs@150MB:10.0
		String[] tokens = selectedPackage.split(delims);
		String strtypePackage = tokens[0].toString(); // "PK1"
		String strdayPackage = tokens[1].toString(); // "1-Day"
		String strdataPackage = tokens[2].toString();     // "150MB"
		String strpricePackage = tokens[3].toString();    // "10.0"
		
		double dayPackage,typePackage,dataPackage,dataMultiplier,pricePackage;
		int i = 0;
		for(packageContents packgeContent1:packageContents.values())
		{
			
			if(strdayPackage.contains( packgeContent1.toString())==true) break;
			i++;
		}		
			
		typePackage = i;
		if(strdayPackage.contentEquals("Daily")==false)
			dayPackage = ((Number)NumberFormat.getInstance().parse(strdayPackage)).intValue();
		else
			dayPackage=1;
		dataPackage = ((Number)NumberFormat.getInstance().parse(strdataPackage)).intValue();
		Double doublepricePackage = Double.valueOf(strpricePackage);
		pricePackage = doublepricePackage.doubleValue();

		if(strdataPackage.contains("MB")) 
			dataMultiplier = 1000000.0 ;
		else if(strdataPackage.contains("GB"))
			dataMultiplier = 1000000000.0;
		else
			dataMultiplier = 1.0;
		
		return new double[]{(double)dayPackage,(double)dataPackage,dataMultiplier,pricePackage};
	}
	
	public void disableConnection(OneWifiService srvc,Devicemaster devicemaster,Customermaster customermaster) throws Exception
	{
		
		if(devicemaster!=null)
		{
			String operatorType = "1"; // unbind
			srvc.invokeUserBinding(devicemaster.getPartnerCode(), devicemaster.getUserCode(), devicemaster.getImei(), devicemaster.getPassword(), operatorType);			
		}
		if(customermaster!=null)
		{
			srvc.changeDeviceStatus(customermaster.getLoginId(), customermaster.getImei(), "Assigned");				
		}

	}
	public void enableConnection(OneWifiService srvc,Devicemaster devicemaster,Customermaster customermaster) throws Exception
	
	{
		
		if(devicemaster!=null)
		{
			String operatorType = "0"; // bind
			srvc.invokeUserBinding(devicemaster.getPartnerCode(), devicemaster.getUserCode(), devicemaster.getImei(), devicemaster.getPassword(), operatorType);			
		}
		if(customermaster!=null)
		{
			srvc.changeDeviceStatus(customermaster.getLoginId(), customermaster.getImei(), "CustomerUsing");				
		}
		
	}
	public Date strDateFmt(String logoutTime) throws ParseException
	{
	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date daFormat = formatter.parse(logoutTime);
		return daFormat;
	}
	
	/*
	 * PreRequiremnt
	 */

	public void runCronJob() throws Exception {
		OneWifiService srvc = new OneWifiService();
		GlocalmeClient client = new GlocalmeClient();
		Customermaster customermaster = null;
		boolean deductionMade = false; // used to exit the customertopup loop if deducted
		int maxLoopTopUp = 0;
		Customerdevice pinCustomerdevice=null;
		boolean bindingDone=false;
		String currentPosn=null;
		String endTimeToQuery=null;
		
		
		List<Devicemaster> listDeviceMaster = srvc.fetchDevicesDetails("CustomerUsing");
		System.out.println(listDeviceMaster.size());	
		for(Devicemaster devicemaster : listDeviceMaster) {				
			System.out.println(devicemaster.getUnFilledCustomertopups().size());
			System.out.println(devicemaster.getDeviceId());			
			if(devicemaster!=null && devicemaster.getUnFilledCustomertopups()!=null) {
				//Getting the unFilled Customertopups
				List<Customertopup> listCustomertopup = devicemaster.getUnFilledCustomertopups();
				maxLoopTopUp = listCustomertopup.size();  //KU: Can get the count for Package and PayAsUGo ?? Karthik
				if(listCustomertopup!=null && listCustomertopup.size()>0) {
					// if topup exist chances he should use the device. KU/Karthik, is there any device status ????
					//if(bindingDone==false){
					// enableConnection(srvc,(Devicemaster)devicemaster,(Customermaster)null); KU - On hold till below rectified.
					// bindingDone = true;
					//}
					//AccountSummaryBean accountSummaryBean = srvc.fetchAccountSummary(devicemaster.getImei());	// KA - why getting the full list			
					//currentPosn = accountSummaryBean.getLatestPosition(); //Value="CN,43048" - Country,location_area_code // KU - Possible the device is OFF
				
					//Getting the Customertopup first row
					int topupRow = 0;
					Customertopup customertopup = (Customertopup) listCustomertopup.get(topupRow);

					for(Customertopup customertopup1: listCustomertopup) { // initialise the current values of customertopup
						if(customertopup1.getExhaustedStatus().contentEquals("Filled")) continue;   // Filled not need to check
						customertopup1.setUsedCurrentData(Double.valueOf(0));
						customertopup1.setUsedCurrentAmt(Double.valueOf(0));						
						customertopup1.setUsedCurrentloopCnt(Integer.valueOf(0));						
					}
					
					//Querying the EndTimeQueried
					Date fromDate = null;
					if(devicemaster.getCustomerdevices()!=null) {
						Set<Customerdevice> listCustomerdevice = devicemaster.getCustomerdevices();
						if(listCustomerdevice!=null && listCustomerdevice.size()>0) {
							for(Customerdevice customerdevice: listCustomerdevice) {
								if(customerdevice!=null && customertopup!=null && customerdevice.getId().getCustomerId().equals(customertopup.getId().getCustomerId()) && customerdevice.getId().getDeviceId().equals(customertopup.getId().getDeviceId())) {
									fromDate = customerdevice.getEndTimeQueried();	
									pinCustomerdevice = customerdevice;									
									break;
									
								}
							}
						}
					}
					
					Calendar cal = Calendar.getInstance();
					//cal.add(Calendar.MONTH, 1);
					Date toDate = cal.getTime();
					
					//If fromDate is null, then get the customer creation date. ToDate will be current date.
					if(fromDate==null && (customertopup!=null && customertopup.getId()!=null)) {
						customermaster = srvc.fetchCustomerDetailsByCustomerId(customertopup.getId().getCustomerId());						
						fromDate = customermaster.getCreationDate();
					}
					
// KU- for testing purpose change Date
					
					fromDate=strDateFmt("20151109221250");
					
					
					//Querying the Billing history
					if(fromDate!=null && toDate!=null) {
						JSONObject resultJSONObject = srvc.fetchTrafficHistory(devicemaster.getImei(), fromDate, toDate, 1, 1);   // KA - just get one record will do
						if(resultJSONObject!=null) {
							int totalPageCount=1; // KA/KU - get all  in one page where records are small becof fast polling.
							resultJSONObject = srvc.fetchTrafficHistory(devicemaster.getImei(), fromDate, toDate, totalPageCount, resultJSONObject.getInt("totalCount"));
							
							System.out.println(resultJSONObject);
							if(resultJSONObject!=null) {
								JSONArray jsonArray = resultJSONObject.getJSONArray("userBillingList");		
								// monitor for each polling
								int usedCurrentloopCnt=0;
								double usedCurrentAmt = 0.0;
								double usedCurrentData =0.0;
								
								// monitor for each topup record
								int usedAccloopCnt=0;
								double usedAccAmt=0.0;
								double usedAccData = 0.0;							
								String billingCountry;
								String totalFlows;
								String loginTime=null;
								String logoutTime=null;
								
								//for(int i = 0;  jsonArray!=null && i < jsonArray.size(); i++)
								for(int i = jsonArray.size()-1;  jsonArray!=null && i >= 0; i--)  // KU/KA - Needed in Ascending order
								{		  
									final double KB_VALUE = 1024;
									
									if(jsonArray.getJSONObject(i)!=null) {
										
										double noLogOutData = 0.0;
										double yesLogOutData = 0.0;
										
										long billingTime, packageTime;
										billingCountry = jsonArray.getJSONObject(i).getString("country");
										totalFlows = jsonArray.getJSONObject(i).getString("totalFlows");
										loginTime = jsonArray.getJSONObject(i).getString("loginTime");
										logoutTime= jsonArray.getJSONObject(i).getString("logoutTime");
										
										if(logoutTime.length()<=0) // condition of customer login & not Logged out
											noLogOutData = Double.parseDouble(totalFlows);											
										else
											yesLogOutData = Double.parseDouble(totalFlows);
										
										//billingTime = Long.parseLong(loginTime);  // identify for time comparison with Package
										billingTime = strDateFmt(loginTime).getTime();
										deductionMade = false;
										
										int trackingLoop = maxLoopTopUp;
										

										for(Customertopup customertopup1: listCustomertopup) { // KU : Package (Ascending Order ) & PayAsUGo (Ascending Order) ?? Karthik
											trackingLoop--;
											if(customertopup1.getExhaustedStatus().contentEquals("Filled") || deductionMade == true) continue;   // Filled not need to check
											
											String packageCountry = customertopup1.getPackageCountry(); //CB-EG:Egypt
											packageTime = customertopup1.getPriceListDate().getTime();
											

											if(		(customertopup1!=null) && 
													(customertopup1.getTopupType().equals("Package")) &&
													(packageCountry.toLowerCase().contains(billingCountry.toLowerCase())) &&  // country check 
													(billingTime >=packageTime)) // time check
											{												
												String selectedPackage = customertopup1.getSelectedPackage(); //PK1:Daily@150MB:10.0, PK2:7-Day@450MB:18.24, PK3:30-Day@1GB:42.4, PK4:90-Day@2GB:78.0, PK5:180-Day@3GB:97.0
												String delims = "[:-@]+";  										
																																			
												double result[] = parseString(selectedPackage,delims); //,dayPackage,dataPackage,dataMultiplier,pricePackage);
												// only dayPackage,dataPackage,dataDivider
												// result order = double dayPackage,typePackage,dataPackage,dataMultiplier,pricePackage;
												
												int dayPackage = (int)result[0];
												int typePackage = (int)result[1];
												int dataPackage = (int)result[2];
												double dataMultiplier = result[3];
												double pricePackage = result[4];
												
												double offeredDataLimit=0.0;					

												offeredDataLimit = dataPackage * dataMultiplier;
												
												deductionMade  = true;

												
												usedAccData = customertopup1.getUsedAccData().doubleValue()*KB_VALUE;
												usedAccloopCnt = customertopup1.getUsedAccloopCnt().intValue();
												usedAccAmt =customertopup1.getUsedAccAmt().doubleValue();
												
												usedCurrentData =customertopup1.getUsedCurrentData().doubleValue()*KB_VALUE;
												usedCurrentloopCnt = customertopup1.getUsedCurrentloopCnt().intValue();
												usedCurrentAmt = customertopup1.getUsedCurrentAmt().doubleValue();
												
	
												
												usedAccData += yesLogOutData;														
												usedCurrentData += noLogOutData + yesLogOutData;														
												usedCurrentloopCnt++;		
												usedAccloopCnt++;

												
												cal = Calendar.getInstance();
											
												//long diff = cal.getTimeInMillis() - customertopup1.getTopupDate().getTime(); KU need this function ?? Karthik
												long diff= cal.getTimeInMillis()-customertopup1.getId().getTopupDate().getTime(); ///tobeuse on top
												
												long diffDays = diff / (24 * 60 * 60 * 1000);
												// Used up stoppage	
 												if(diffDays > (long)dayPackage || ((usedAccData+noLogOutData) >= offeredDataLimit)) {
													customertopup1.setExhaustedStatus("Filled");
													break;												
												}
												// KU/KA : Advice to have it in the code, // Reminder need to do
												else if(diffDays >(long) (dayPackage*0.8) || ((usedAccData+noLogOutData) >= (offeredDataLimit*0.8))){
													// KU: Send email notification  ???? Ramesh
												}
												
												
											} else if(	customertopup1!=null && 
														customertopup1.getTopupType().equals("PayAsYouGo")	&&
														(billingTime >=packageTime)) // time check
											{																	
												List<Pricing> listPricing = srvc.fetchPricing(customertopup1.getPriceListDate());
												System.out.println(listPricing.size());	
												for(Pricing pricing: listPricing) {
													if(pricing!=null) {
														
														String payAsUGocountry = pricing.getId().getProductCode();
														if((payAsUGocountry.toLowerCase().contains(billingCountry.toLowerCase()))==false) continue;

														
														
													// only dayPackage,dataPackage,dataDivider
														int dataPackage = 150;  // KA ?? need to get this
														double dataMultiplier = 1000000.00;  // KA ?? need to get this
														double offeredDataLimit = dataPackage * dataMultiplier;

														deductionMade  = true;

														
														usedAccData = customertopup1.getUsedAccData().doubleValue()*KB_VALUE;
														usedAccloopCnt = customertopup1.getUsedAccloopCnt().intValue();
														usedAccAmt =customertopup1.getUsedAccAmt().doubleValue();
														
														usedCurrentData =customertopup1.getUsedCurrentData().doubleValue()*KB_VALUE;
														usedCurrentloopCnt = customertopup1.getUsedCurrentloopCnt().intValue();
														usedCurrentAmt = customertopup1.getUsedCurrentAmt().doubleValue();
																
														usedAccData += yesLogOutData;														
														usedCurrentData += noLogOutData + yesLogOutData;														
														usedCurrentloopCnt++;		
														usedAccloopCnt++;
														

														double payAsYouGo = pricing.getPayAsYouGo().doubleValue();	
														
														double offeredPriceLimit = payAsYouGo/offeredDataLimit;
														
														usedCurrentAmt += (offeredPriceLimit * yesLogOutData);	
														usedAccAmt += (offeredPriceLimit * yesLogOutData);
														
														double noLogOutDataAmt = offeredPriceLimit * noLogOutData ;
														
														if((usedAccAmt+noLogOutDataAmt) >= customertopup1.getTopupAmount())
														{
															customertopup1.setExhaustedStatus("Filled");
															break;									
														}
														// KU/KA: Advice to have it in the code
														else if(((usedAccAmt+noLogOutDataAmt) >= (customertopup1.getTopupAmount()*0.8)) &&
																(trackingLoop<=1))  // PayAsUGo is the last record.
														{
															// KU: SEnd email notification ???? Ramesh
														}
																												
													}
													if(deductionMade==true)
														break;
												}// for(Pricing						
											}
											else if( currentPosn!=null &&
													(currentPosn.toLowerCase().contains(packageCountry.toLowerCase())))  //he is in the host country where he have package
											{
												deductionMade  = true;

											}
											if(deductionMade)
											{												
												customertopup1.setUsedCurrentAmt(Double.valueOf(usedCurrentAmt));
												customertopup1.setUsedCurrentloopCnt(Integer.valueOf(usedCurrentloopCnt));
												customertopup1.setUsedCurrentData(Double.valueOf(usedCurrentData/KB_VALUE));
												
												customertopup1.setUsedAccAmt(Double.valueOf(usedAccAmt));								
												customertopup1.setUsedAccloopCnt(Integer.valueOf(usedAccloopCnt));
												customertopup1.setUsedAccData(Double.valueOf(usedAccData/KB_VALUE));	
	
												if(bindingDone==false)
												{
													//enableConnection(srvc,(Devicemaster)devicemaster,(Customermaster)null);  // look for binding status.
													bindingDone = true;    // to be set true;
												}
												break;
											}
											else if(bindingDone==true && trackingLoop <=0)
											{
												//disableConnection(srvc,(Devicemaster)devicemaster,(Customermaster)null); // look for status.
												bindingDone = false;
											}
											
												
										} // for(customertopUp)
										//String operatorType = "1";
										//srvc.invokeUserBinding(devicemaster.getPartnerCode(), devicemaster.getUserCode(), devicemaster.getImei(), devicemaster.getPassword(), operatorType);
										//topupRow++;
										//AccountSummaryBean accountSummaryBean = srvc.fetchAccountSummary(devicemaster.getImei());
										//if(accountSummaryBean.getLatestPosition()!=null){ //Value="CN,43048" - Country,location_area_code
										//}
										//srvc.changeDeviceStatus(customermaster.getLoginId(), customermaster.getImei(), "Operatable"); 
										//srvc.updateCustomerTopup(customertopup);
									} // if (billingEntry!=NULL)
								} //for(billing)
								if(jsonArray!=null)
								{								
									
									pinCustomerdevice.setEndTimeQueried(toDate);	
									maxLoopTopUp=0;
									for(Customertopup customertopup1: listCustomertopup) {
							
										if(customertopup1.getExhaustedStatus().contentEquals("Filled")) // Filled not need to check	
											maxLoopTopUp++;
											
									}
									if(maxLoopTopUp == listCustomertopup.size())   // check if it is 0 KA.
									{
									 // no topup amt - unbind and change the status to "Assigned" 	
										if(bindingDone==true){
											//disableConnection(srvc,(Devicemaster)devicemaster,(Customermaster)customermaster);
											bindingDone=false;
										}
										
									}
									srvc.updateCustomerTopup(customertopup);
								}
								
							} // if (billingList!=NULL)			
						}// if (billingCntEntry!=NULL)	
					} // fromDate!=null && toDate!=null
				} // customertopup Size > 0
				else 
				{
					// KU: Any status infor of binding done before available to prevent repetition ???? Karthik
					if(bindingDone==true){
		//				disableConnection(srvc,(Devicemaster)devicemaster,(Customermaster)null);
						bindingDone=false;
					}
				}
			} // devicemaster.getUnFilledCustomertopups()!=null
		} // for (deviceMaster)
	}
	
	public static void main(String args[]) throws Exception {
		OneWifiService srvc = new OneWifiService();
		
		try {	
			
			 // Full path to uploaded document
			/*String filePath = "C:\\Personal\\Projects\\NRIC_FRONT.jpg";	
			byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
			System.out.println("NRIC Front OCR-Response:"+srvc.getTextFromImage(fileContent));
			
			filePath = "C:\\Personal\\Projects\\NRIC_Back.jpg";	
			fileContent = Files.readAllBytes(Paths.get(filePath));
			System.out.println("NRIC Back OCR-Response:"+srvc.getTextFromImage(fileContent));
			*/

			
			//System.out.println(srvc.changePassword("test@gmail.com", "password"));
			System.out.println(srvc.validateDevice("test@gmail.com", "356166060296562", "UKG1SLZH1504150635"));
			
			File[] files = new File[1];
			files[0] = new File("E:/Personal/Projects/OneWiFi/OneWiFi_Web/AllImportFile-2.8.xlsx");
			String result = srvc.uploadDevices(files);
			result = result + srvc.uploadRetailers(files);
			result = result + srvc.uploadRetailerDevices(files);	
			result = result + srvc.uploadPricing(files);		
			System.out.println(result);
						
			System.out.println(srvc.validateIMEIForRetailer("test@gmail.com", "356166060296562"));
			
			Customermaster customermaster = new Customermaster();
			customermaster.setIdentityType("NRIC");
			customermaster.setIdentityId("S1234567Z");
			customermaster.setIdentityImage(new String("NRIC_Image").getBytes());
			customermaster.setFullName("John M");
			customermaster.setLoginId("test@gmail.com");
			customermaster.setAddress("Blk 245|Singapore|");
			customermaster.setContactNo(12345679);
			customermaster.setImei("356166060296562");
			System.out.println(srvc.saveCustomer("Allan.Lim@expressway.com", customermaster));		
			
			String result1 = srvc.encryptNameAndLoginId("John", "test@gmail.com", "356166060296562");
			System.out.println(result1);
			String results[] = srvc.decryptNameAndLoginId(result1);
			System.out.println(results[0]+"=="+results[1]+"=="+results[2]);
			
			Customertopup customertopup = new Customertopup();
			customertopup.setTopupAmount(10);
			customertopup.setCurrency("USD");
			customertopup.setLoginId("test@gmail.com");	
			customertopup.setTopupType("PayAsYouGo@1MB");
			System.out.println(srvc.saveCustomerTopup(customertopup));
			
			Thread.sleep(2000);
			customertopup = new Customertopup();
			customertopup.setTopupAmount(25);
			customertopup.setCurrency("USD");
			customertopup.setLoginId("test@gmail.com");	
			customertopup.setTopupType("Package");
			customertopup.setPackageCountry("CB-HK;Hong Kong");
			customertopup.setSelectedPackage("PK3:30-Day@1GB:25.0");
			System.out.println(srvc.saveCustomerTopup(customertopup));

			Thread.sleep(2000);
			customertopup = new Customertopup();
			customertopup.setTopupAmount(70);
			customertopup.setCurrency("USD");
			customertopup.setLoginId("test@gmail.com");	
			customertopup.setTopupType("Package");
			customertopup.setPackageCountry("CB-SG;Singapore");
			customertopup.setSelectedPackage("PK4:90-Day@2GB:70.0");
			System.out.println(srvc.saveCustomerTopup(customertopup));
			
			Thread.sleep(2000);
			customertopup = new Customertopup();
			customertopup.setTopupAmount(20);
			customertopup.setCurrency("USD");
			customertopup.setLoginId("test@gmail.com");	
			customertopup.setTopupType("PayAsYouGo@1MB");
			System.out.println(srvc.saveCustomerTopup(customertopup));
			
			Thread.sleep(2000);
			customertopup = new Customertopup();
			customertopup.setTopupAmount(25);
			customertopup.setCurrency("USD");
			customertopup.setLoginId("test@gmail.com");	
			customertopup.setTopupType("Package");
			customertopup.setPackageCountry("CB-HK;Hong Kong");
			customertopup.setSelectedPackage("PK3:30-Day@1GB:25.0");
			System.out.println(srvc.saveCustomerTopup(customertopup));

			Thread.sleep(2000);
			customertopup = new Customertopup();
			customertopup.setTopupAmount(70);
			customertopup.setCurrency("USD");
			customertopup.setLoginId("test@gmail.com");	
			customertopup.setTopupType("Package");
			customertopup.setPackageCountry("CB-SG;Singapore");
			customertopup.setSelectedPackage("PK4:90-Day@2GB:70.0");
			System.out.println(srvc.saveCustomerTopup(customertopup));
			
			
			Calendar cal = Calendar.getInstance();
			Date toDate = cal.getTime();
				
			Calendar cal1 = Calendar.getInstance();
			cal1.add(Calendar.HOUR_OF_DAY, -1);
			Date fromDate = cal1.getTime();
			
			
			Set<Customertopup> listCustomertopup = srvc.fetchCustomerTopupHistory("test@gmail.com", fromDate, toDate);
			System.out.println(listCustomertopup+"=="+listCustomertopup.size());
			
			/*for(Customertopup customertopup1: listCustomertopup) {
				if(customertopup1.getTopupType().equalsIgnoreCase("PayAsYouGo@1MB")) {
					customertopup1.setTopupAmount(25);
					System.out.println(customertopup1.getTopupType());
					System.out.println(srvc.updateCustomerTopup(customertopup1).booleanValue());			
				}
			}*/
			
			List<Devicemaster> listDeviceMaster = srvc.fetchDevicesDetails("CustomerUsing");
			System.out.println(listDeviceMaster.size());	
			for(Devicemaster devicemaster1 : listDeviceMaster) {
				System.out.println(devicemaster1.getCustomertopups().size());
				System.out.println(devicemaster1.getDeviceId());
				if(devicemaster1.getCustomertopups()!=null) {
					Set<Customertopup> listCustomertopup1 = devicemaster1.getCustomertopups();
					for(Customertopup customertopup1 : listCustomertopup1) {					
						System.out.println(customertopup1.getTopupType()+"=="+customertopup1.getExhaustedStatus()+"=="+customertopup1.getId().getTopupDate());
					}
				}
			}


			Date validDate = null;
			List<Pricing> listPricing = srvc.fetchPricing();
			System.out.println(listPricing.size());	
			for(Pricing pricing: listPricing) {
				validDate = pricing.getId().getValidDate();
				System.out.println(pricing.getId().getValidDate());
			}
			
			listPricing = srvc.fetchPricing(validDate);
			System.out.println(listPricing.size());	
			for(Pricing pricing: listPricing) {
				System.out.println(pricing.getId().getValidDate());
			}
			
			
			HashMap<String, List<String>> mapPricing = srvc.fetchPricingDetailsForTopup();		
			for (Map.Entry<String, List<String>> entry : mapPricing.entrySet()) {
				System.out.println(entry.getKey());
				for(String str: entry.getValue()) {
					System.out.println(str);
				}
			}
			
			
			/*JSONObject resultJSONObject = srvc.fetchTrafficHistory("356166060296562", fromDate, toDate, 1, 20);
			System.out.println(resultJSONObject);
			if(resultJSONObject!=null) {
				JSONArray jsonArray = resultJSONObject.getJSONArray("userBillingList");
				for(int i = 0;  jsonArray!=null && i < jsonArray.size(); i++)
				{		  
					System.out.println(jsonArray.getJSONObject(i));
				}
			}*/
			
			System.out.println(srvc.fetchAccountSummary("356166060296562"));
			
			System.out.println(srvc.authenticateUser("onewifi@bcc.com", "passdword"));
			
			
			//srvc.runCronJob();
		} catch(Exception ex){
			System.out.println("In main transactions");
			ex.printStackTrace();
		}
		
		System.exit(0);
	}
}
