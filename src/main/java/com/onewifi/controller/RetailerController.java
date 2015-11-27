package com.onewifi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.glocalme.service.dto.AddressDto;
import com.glocalme.service.dto.CustomerDto;
import com.glocalme.service.dto.UserLogin;
import com.google.gson.Gson;
import com.onewifi.beans.Customermaster;
import com.onewifi.beans.Retailermaster;
import com.onewifi.constants.AppConstants;
import com.onewifi.constants.ReportConstants;
import com.onewifi.service.EmailService;
import com.onewifi.service.OneWifiService;
import com.onewifi.service.PrintService;
import com.onewifi.util.EncryptionUtil;
import com.onewifi.util.OneWifiUtil;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@Scope("request")
@RequestMapping("/retailer")
public class RetailerController {
	
	@Autowired
	private PrintService printService;

	@Autowired 
	private EmailService emailService;
	
	@Autowired	
	private OneWifiService oneWifiService;
	
	final static Logger logger = Logger.getLogger(RetailerController.class);
	
    @RequestMapping(value = "/print", method = RequestMethod.POST)
    public void print(@RequestBody CustomerDto customerDto, 
    		HttpServletRequest servletRequest,
    		HttpServletResponse servletResponse ) {
    	
    	logger.info("RetailerController - (/print) Print - Start");
    	logger.info(customerDto);	
    	
    	
    	String jasperFileName = servletRequest.getServletContext().getRealPath("/") + ReportConstants.JASPER_FOLDER + "/" + ReportConstants.RETAILER_BUYDEVICE + ".jasper";
    	String reportFileName = servletRequest.getServletContext().getRealPath("/") + ReportConstants.REPORT_FOLDER + "/" + ReportConstants.RETAILER_BUYDEVICE + ".jrxml";
    	JasperReport jasperReport = null;
    	try {
    		jasperReport = printService.compileReport( reportFileName , jasperFileName );
    		
    	    List<CustomerDto> list = new ArrayList<CustomerDto>();
    	    list.add(customerDto);
    		
    	    
    	    JRDataSource datasource = new JRBeanCollectionDataSource(list, true);
    		byte[] bytes = JasperRunManager.runReportToPdf(jasperFileName,  new HashMap<String,Object>(), datasource);

    		servletResponse.setContentType("application/pdf");
    		servletResponse.setContentLength(bytes.length);

    		ServletOutputStream servletOutputStream = servletResponse.getOutputStream();
		    servletOutputStream.write(bytes, 0, bytes.length);
		    servletOutputStream.flush();
		    servletOutputStream.close();


		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> updateRetailer(@RequestBody CustomerDto customerDto) {
    	logger.info("RetailerController - (/update) updateRetailer - Start");
		Map<String,Object> result = new HashMap<String,Object>();
		String updateStatus = null;
		try {
			String validationResult = validateRetailerUpdate(customerDto);
			if (validationResult != null) {
				result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
				result.put(AppConstants.RESPONSE_MESSAGE, validationResult);
				return result;
			}
			
			Retailermaster retailermaster = new Retailermaster();
			retailermaster.setCoyRetailerName(customerDto.getFullName());
			retailermaster.setRetailerContactNumber(Integer.parseInt(customerDto.getMobileNumber()));			
			retailermaster.setAddress(customerDto.getFullAddress());
			retailermaster.setLoginId(customerDto.getEmail());
			retailermaster.setDirectorName(customerDto.getDirectorName());
			retailermaster.setCoyContactNumber(Integer.parseInt(customerDto.getCoyContactNumber()));
			retailermaster.setRetailerId(customerDto.getRetailerId());		
			
			updateStatus = oneWifiService.updateRetailer(retailermaster);
			if ("SUCCESS".equalsIgnoreCase(updateStatus)) {
				result.put(AppConstants.RESPONSE_STATUS, AppConstants.RETAILER_UPDATE_SUCCESSMSG);
			} else {
				result.put(AppConstants.RESPONSE_STATUS, updateStatus);
			} 
		} catch(Exception e) {
			logger.error("RetailerController - (/update) updateRetailer - Failed");
			logger.error(e.getMessage());
			e.printStackTrace();
			result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
			result.put(AppConstants.RESPONSE_MESSAGE, e.toString());			
		}
		return result;
    }
	
    
    @RequestMapping(value="/scanid", method = RequestMethod.POST)
    public @ResponseBody String doUpload(MultipartHttpServletRequest request) {      
    	Iterator<String> itr = request.getFileNames();
    	MultipartFile file = null;
        try {
            file = request.getFile(itr.next()); //Get the file.
        	System.out.println(file.getOriginalFilename());
        	System.out.println(file.getName());
        } catch (NoSuchElementException e) {
        }
    	String param = (String)request.getParameter("obj");
    	System.out.println(param.toString());
    	Gson gson = new Gson();
    	CustomerDto customerDto = gson.fromJson(param, CustomerDto.class);
    	System.out.println(customerDto.getFullName());
    	System.out.println(customerDto.getAddressLine1());    	
    	System.out.println(itr);
    	return "Great";
    }    
    
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> save(MultipartHttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		
    	Iterator<String> itr = request.getFileNames();
    	MultipartFile multipartFile = null;
    	boolean fileFound = false;
        try {
        	if (itr != null && itr.hasNext()) {
        		multipartFile = request.getFile(itr.next()); //Get the file.
        		System.out.println(multipartFile.getOriginalFilename());
        		System.out.println(multipartFile.getName());
        		if (multipartFile.getName() != null) { 
        			fileFound = true;
        		}
        	}
        } catch (NoSuchElementException e) {
			result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
			result.put(AppConstants.RESPONSE_MESSAGE, "Error Reading the Scanned File");
			return result;
        }		
		
    	String param = (String)request.getParameter("obj");
    	Gson gson = new Gson();    	
    	CustomerDto customerDto = gson.fromJson(param, CustomerDto.class);
    	AddressDto addressDto = getAddressDto(customerDto);
    	String addressStr = gson.toJson(addressDto, AddressDto.class);
    	
    	System.out.println("Customer DTO From Request");
    	System.out.println(ReflectionToStringBuilder.toString(customerDto));
    	
    	System.out.println("Address DTO Str:"+addressStr);
    	
		String validationStatus = validateInputForSaveCustomer(customerDto);
		if (validationStatus != null) {
			result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
			result.put(AppConstants.RESPONSE_MESSAGE, validationStatus);
			return result;
		}   
		if (!fileFound) {
			result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
			result.put(AppConstants.RESPONSE_MESSAGE, "Scanned Identity File Not Found");
			return result;			
		}
		
		try {
			
			logger.info("Saving Customer - Start");
			
			Customermaster customermaster = getCustomerMasterForSave(customerDto);
			customermaster.setIdentityImage(FileUtils.readFileToByteArray(OneWifiUtil.multipartToFile(multipartFile)));
			customermaster.setAddress(addressStr);
			/*
			System.out.println("Bytes:");
			System.out.println(customermaster.getIdentityImage().length);
			*/
			UserLogin userLogin = getUserLoginFromSession(request);
			
			String saveStatus = oneWifiService.saveCustomer(userLogin.getUserName() , customermaster);
			
			System.out.println("Customer Save Successfully");			
			boolean recordSaved = false;
			if ("Customer saved successfully".equalsIgnoreCase(saveStatus)) {
				System.out.println("Customer Save Successfully");
				result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_SUCCESS);		
				recordSaved = true;
			} else {
				result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
				result.put(AppConstants.RESPONSE_MESSAGE, saveStatus);
			}
			
			logger.info("Sending Email Activation - Start");
    		if (recordSaved) {
	    		logger.info("Emailing Customer for Device Activation - Start");
				Map<String,Object> model = new HashMap<String,Object>();	  
				ReflectionToStringBuilder.toString(customerDto);
				model.put("identityScanDto", customerDto);
				//model.put("baseurl", AppConstants.ACTIVATION_URL);
				String activationUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/#/personal/activate/";
				model.put("baseurl", activationUrl);
				model.put("encryptedlogin", EncryptionUtil.encryptNameAndLoginId(customerDto.getFullName(), customerDto.getEmail()));
				try {
					String toAddress = customerDto.getEmail();
					emailService.sendEmail(toAddress, "New Device/User Activiation - OneWifi" , AppConstants.EMAIL_TEMPLATE_USERREGISTRATION , model);
					result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_SUCCESS);	
				} catch (Exception e) {
					e.printStackTrace();
					String msg = "ERROR WHILE SENDING ACTIVATION EMAIL";
					result.put(AppConstants.RESPONSE_MESSAGE, msg);
					result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);	
				}
	    		logger.info("Email Sent to Customer for Device Activation - End");
	    		result.put(AppConstants.RESPONSE_MESSAGE, "Device Bought Successfully");	
    		}
    		logger.info("Sending Email Activation - End");
		
		} catch(Exception e) {
			e.printStackTrace();
			String errorMsg = e.toString();
			result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
			result.put(AppConstants.RESPONSE_MESSAGE, errorMsg);			
		}
		return result;
    }

    @RequestMapping(value = "/getHash", method = RequestMethod.POST)
    public @ResponseBody String getHashvalue(@RequestParam("hashValue") String hashValue) {
    	
    	logger.info("RetailerController - getHashvalue - Start");
    	String loginId = null;
    	String imei = null;
    	String serialNo = null;
    	String resultStatus = null;    	
    	try {
    		String[] decryptedValue = oneWifiService.decryptNameAndLoginId(hashValue);
        	if(decryptedValue!=null && decryptedValue.length==3) {
        		loginId = decryptedValue[0];
        		imei = decryptedValue[1];
        		serialNo = decryptedValue[2];
        	}
    		resultStatus = oneWifiService.validateDevice(loginId, imei, serialNo);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	if (resultStatus!=null && resultStatus.equalsIgnoreCase("Bought")) {    		
    		return loginId;//"SUCCESS";
    	} else {
    		return "FAILURE";
    	}
    	
    	/*
    	logger.info("RetailerController - getHashvalue - Start");
    	String decryptedValue = EncryptionUtil.decryptNameAndLoginId(hashValue);
    	logger.info("RetailerController - getHashvalue - End");
    	return decryptedValue;
    	*/
    }
    
    private String validateInputForSaveCustomer(CustomerDto customerDto) {
    	if (customerDto.getEmail() == null || customerDto.getEmail().trim().length() == 0) {
    		return "Email is Mandatory";
    	}
    	
    	if (customerDto.getImei() == null || customerDto.getImei().trim().length() == 0) {
    		return "IMEI is Mandatory";
    	}
    	try {
    		Integer.valueOf(customerDto.getMobileNumber());
    	} catch(Exception e) {
    		return "Invalid Mobile Number";
    	}
    	
    	return null;
    }
    
	public UserLogin getUserLoginFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		SecurityContext securityContext = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
		if (securityContext == null) {
			logger.info("User Not Logged in");
			return new UserLogin(false, null,null);
		} else {
			logger.info("User Logged in");
			logger.info(securityContext.getAuthentication().getName());
			UserLogin userLogin = (UserLogin)session.getAttribute("LOGGEDIN_USER");
			return userLogin;
		}
	}    
    
    public AddressDto getAddressDto(CustomerDto customerDto) {
    	AddressDto addressDto = new AddressDto();
    	addressDto.setAddressLine1(customerDto.getAddressLine1());
    	addressDto.setAddressLine2(customerDto.getAddressLine2());
    	addressDto.setAddressLine3(customerDto.getAddressLine3());
    	addressDto.setSelectedCountry(customerDto.getCountry());
    	addressDto.setState(customerDto.getState());
    	addressDto.setZipCode(customerDto.getZipCode());
    	addressDto.setCity(customerDto.getCity());
    	return addressDto;
    }
    
    public Customermaster getCustomerMasterForSave(CustomerDto customerDto) {
    	Customermaster customermaster = new Customermaster();
    	customermaster.setLoginId(customerDto.getEmail());
    	customermaster.setImei(customerDto.getImei());
    	customermaster.setContactNo(Integer.valueOf(customerDto.getMobileNumber()));
    	customermaster.setAddress(customerDto.getAddressLine1());
    	customermaster.setIdentityId(customerDto.getIdNumber());
    	customermaster.setIdentityType(customerDto.getIdType());
    	customermaster.setFullName(customerDto.getFullName());
    	return customermaster;
    }
    
    public String validateRetailerUpdate(CustomerDto customerdto) {
		if (customerdto.getRetailerId() == null) {
			return "Unknown Error";
		}
		
		if (customerdto.getEmail() == null || customerdto.getEmail().trim().length() == 0) {
			return "Email is mandatory";
		}
		
		if (customerdto.getFullName() == null || customerdto.getFullName().trim().length() == 0) {
			return "Retailer Name is mandatory";
		}
		
		if (customerdto.getFullAddress() == null || customerdto.getFullAddress().trim().length() == 0) {
			return "Retailer Address is mandatory";
		}
		
		if (customerdto.getDirectorName() == null || customerdto.getDirectorName().trim().length() == 0) {
			return "Director Name is mandatory";
		}

		if (customerdto.getMobileNumber() == null || customerdto.getMobileNumber().trim().length() == 0) {
			return "Retailer Contact Number is mandatory";
		}		
    	try {
    		Integer.valueOf(customerdto.getMobileNumber());
    	} catch(Exception e) {
    		return "Invalid Retailer Contact Number";
    	}
    	
		if (customerdto.getCoyContactNumber() == null || customerdto.getCoyContactNumber().trim().length() == 0) {
			return "Retailer Contact Number is mandatory";
		}		
    	try {
    		Integer.valueOf(customerdto.getCoyContactNumber());
    	} catch(Exception e) {
    		return "Invalid COY Contact Number";
    	}		
    	
		return null;
    }
    
    /*
    public static void main(String[] args) throws JRException, IOException {
    	JasperReport jasperReport = JasperCompileManager.compileReport("D:\\Temp\\Projects\\GLocalMe\\src\\main\\webapp\\resources\\jrxml\\Registration.jrxml");
    	List<IdentityScanDto> list = new ArrayList<IdentityScanDto>();
    	IdentityScanDto identityScanDto = new IdentityScanDto();
    	identityScanDto.setFullName("Test");
    	identityScanDto.setIdNumber("S7575768A");
    	identityScanDto.setEmail("rameshs24@gmail.com");
    	identityScanDto.setAddressLine1("BLK 367A, #08-115");
    	identityScanDto.setAddressLine2("TAMPINES STREET 34");
    	identityScanDto.setAddressLine3("SINGAPORE - 521367");
    	list.add(identityScanDto);
    	
    	JRDataSource datasource = new JRBeanCollectionDataSource(list, true);
    	//JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,new HashMap(), new JREmptyDataSource());
    	JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,new HashMap<String,Object>(),datasource);
    	JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\data\\sample.pdf");
    }
    */
}
