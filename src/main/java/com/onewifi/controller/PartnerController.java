package com.onewifi.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.glocalme.service.dto.CustomerDto;
import com.onewifi.beans.Partnermaster;
import com.onewifi.beans.Retailermaster;
import com.onewifi.constants.AppConstants;
import com.onewifi.service.OneWifiService;
import com.onewifi.service.PartnerService;
import com.onewifi.service.PrintServiceImpl;
import com.onewifi.util.OneWifiUtil; 

@Controller
@Scope("request")
@RequestMapping("/partner")
public class PartnerController {
 
	final Logger logger = Logger.getLogger(PartnerController.class);
	
	@Autowired
	private PartnerService partnerService;
	
	@Autowired	
	private OneWifiService oneWifiService;	
	
    @RequestMapping(value = "/upload/devices", method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> uploadDevices(MultipartHttpServletRequest request) {
    	logger.info("PartnerController - Upload Devices (/upload/devices) - Start" );
    	Map<String,Object> response = new HashMap<String,Object>();

    	Iterator<String> itr = request.getFileNames();
    	MultipartFile multipartFile = null;
    	boolean fileFound = false;
        try {
        	if (itr != null && itr.hasNext()) {
        		multipartFile = request.getFile(itr.next()); //Get the file.
        		if (multipartFile.getName() != null) { 
        			fileFound = true;
        		}
        	}
        } catch (NoSuchElementException e) {
        	response.put(AppConstants.RESPONSE_STATUS, "ERROR");
        	response.put(AppConstants.RESPONSE_MESSAGE, "Error Reading the uploaded File");
			return response;
        }	   
        
        if (!fileFound) {
        	response.put(AppConstants.RESPONSE_STATUS, "ERROR");
        	response.put(AppConstants.RESPONSE_MESSAGE, "Choose the file to upload"); 
        	return response;     	
        }
        
    	
    	// If Validations pass
    	String message = null;
    	try {
        	response.put("STATUS", "SUCCESS");    		
			message = partnerService.uploadDevices(OneWifiUtil.multipartToFile(multipartFile));
			if (message != null && message.contains("error")) {
				response.put("STATUS", "ERROR");
			}
		} catch (IllegalStateException e) {
			response.put("STATUS", "ERROR");
			e.printStackTrace();
		} catch (IOException e) {
			response.put("STATUS", "ERROR");
			e.printStackTrace();
		} catch (Exception e) {
			response.put("STATUS", "ERROR");
			e.printStackTrace();
		}
    	response.put("MESSAGE", message);
    	return response;
    }

    @RequestMapping(value = "/upload/retailers", method = RequestMethod.POST)
    public  @ResponseBody Map<String,Object> uploadRetailers(MultipartHttpServletRequest request) {
    	logger.info("PartnerController - Upload Retailers");
    	Map<String,Object> response = new HashMap<String,Object>();    

    	
    	Iterator<String> itr = request.getFileNames();
    	MultipartFile multipartFile = null;
    	boolean fileFound = false;
        try {
        	if (itr != null && itr.hasNext()) {
        		multipartFile = request.getFile(itr.next()); //Get the file.
        		if (multipartFile.getName() != null) { 
        			fileFound = true;
        		}
        	}
        } catch (NoSuchElementException e) {
        	response.put(AppConstants.RESPONSE_STATUS, "ERROR");
        	response.put(AppConstants.RESPONSE_MESSAGE, "Error Reading the uploaded File");
			return response;
        }	   
        
        if (!fileFound) {
        	response.put(AppConstants.RESPONSE_STATUS, "ERROR");
        	response.put(AppConstants.RESPONSE_MESSAGE, "Choose the file to upload"); 
        	return response;     	
        }    	
    	
    	String message = null;
    	try {
        	response.put("STATUS", "SUCCESS");    		
    		message = partnerService.uploadRetailers(OneWifiUtil.multipartToFile(multipartFile));
		} catch (IllegalStateException e) {
			response.put("STATUS", "ERROR");			
			e.printStackTrace();
		} catch (IOException e) {
			response.put("STATUS", "ERROR");
			e.printStackTrace();
		} catch (Exception e) {
			response.put("STATUS", "ERROR");
			e.printStackTrace();
		}    	
    	response.put("MESSAGE", message);    	
    	return response;
    	
    }    

    @RequestMapping(value = "/upload/retailerdevices", method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> uploadRetailerDevices(MultipartHttpServletRequest request) {
    	logger.info("PartnerController - Upload Retailer Devices");
    	Map<String,Object> response = new HashMap<String,Object>();    

    	
    	
    	Iterator<String> itr = request.getFileNames();
    	MultipartFile multipartFile = null;
    	boolean fileFound = false;
        try {
        	if (itr != null && itr.hasNext()) {
        		multipartFile = request.getFile(itr.next()); //Get the file.
        		if (multipartFile.getName() != null) { 
        			fileFound = true;
        		}
        	}
        } catch (NoSuchElementException e) {
        	response.put(AppConstants.RESPONSE_STATUS, "ERROR");
        	response.put(AppConstants.RESPONSE_MESSAGE, "Error Reading the uploaded File");
			return response;
        }	   
        
        if (!fileFound) {
        	response.put(AppConstants.RESPONSE_STATUS, "ERROR");
        	response.put(AppConstants.RESPONSE_MESSAGE, "Choose the file to upload"); 
        	return response;     	
        }        	
    	
    	String message = null;    	
    	try {
        	response.put("STATUS", "SUCCESS");        		
			message = partnerService.uploadRetailerDevices(OneWifiUtil.multipartToFile(multipartFile));
		} catch (IllegalStateException e) {
			response.put("STATUS", "ERROR");			
			e.printStackTrace();
		} catch (IOException e) {
			response.put("STATUS", "ERROR");			
			e.printStackTrace();
		} catch (Exception e) {
			response.put("STATUS", "ERROR");			
			e.printStackTrace();
		}       	
    	response.put("MESSAGE", message);    	
    	return response;
    }        
    
    @RequestMapping(value = "/upload/pricing", method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> uploadPricing(MultipartHttpServletRequest request) {
    	System.out.println("PartnerController - Upload Pricing");
    	Map<String,Object> response = new HashMap<String,Object>();    

    	Iterator<String> itr = request.getFileNames();
    	MultipartFile multipartFile = null;
    	boolean fileFound = false;
        try {
        	if (itr != null && itr.hasNext()) {
        		multipartFile = request.getFile(itr.next()); //Get the file.
        		if (multipartFile.getName() != null) { 
        			fileFound = true;
        		}
        	}
        } catch (NoSuchElementException e) {
        	response.put(AppConstants.RESPONSE_STATUS, "ERROR");
        	response.put(AppConstants.RESPONSE_MESSAGE, "Error Reading the uploaded File");
			return response;
        }	   
        
        if (!fileFound) {
        	response.put(AppConstants.RESPONSE_STATUS, "ERROR");
        	response.put(AppConstants.RESPONSE_MESSAGE, "Choose the file to upload"); 
        	return response;     	
        }       	
    	
    	String message = null;       	
    	try {
        	response.put("STATUS", "SUCCESS");       		
			message = partnerService.uploadPricing(OneWifiUtil.multipartToFile(multipartFile));
		} catch (IllegalStateException e) {
			response.put("STATUS", "ERROR");						
			e.printStackTrace();
		} catch (IOException e) {
			response.put("STATUS", "ERROR");						
			e.printStackTrace();
		} catch (Exception e) {
			response.put("STATUS", "ERROR");						
			e.printStackTrace();
		}       	
    	response.put("MESSAGE", message);    	
    	return response;    	

    }     
    
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> updatePartner(@RequestBody CustomerDto customerDto) {
    	logger.info("PartnerController - (/update) updatePartner - Start");
		Map<String,Object> result = new HashMap<String,Object>();
		String updateStatus = null;
		try {

			String validationResult = validatePartnerUpdate(customerDto);
			if (validationResult != null) {
				result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
				result.put(AppConstants.RESPONSE_MESSAGE, validationResult);
				return result;
			}
			
			Partnermaster partnermaster = new Partnermaster();
			partnermaster.setPartnerId(customerDto.getPartnerId());
			partnermaster.setLoginId(customerDto.getEmail());
			partnermaster.setFirstName(customerDto.getFirstName());
			partnermaster.setLastName(customerDto.getLastName());
			partnermaster.setAddress(customerDto.getFullAddress());
			
			updateStatus = oneWifiService.updatePartner(partnermaster);
			if ("SUCCESS".equalsIgnoreCase(updateStatus)) {
				result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_SUCCESS);
				result.put(AppConstants.RESPONSE_MESSAGE, AppConstants.PARTNER_UPDATE_SUCCESSMSG);
			} else {
				result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
				result.put(AppConstants.RESPONSE_MESSAGE,updateStatus);				
			} 
		} catch(Exception e) {
			logger.error("PartnerController - (/update) updatePartner - Failed");
			logger.error(e.getMessage());
			e.printStackTrace();
			result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
			result.put(AppConstants.RESPONSE_MESSAGE, e.toString());			
		}
		return result;
    }
    

    public String validatePartnerUpdate(CustomerDto customerdto) {
	
		//List<String> errors = new ArrayList<String>();
		if (customerdto.getPartnerId() == null) {
			return "Unknown Error";
		}
		
		if (customerdto.getEmail() == null || customerdto.getEmail().trim().length() == 0) {
			return "Email is mandatory";
		}
		
		if (customerdto.getFirstName() == null || customerdto.getFirstName().trim().length() == 0) {
			return "First Name is mandatory";
		}
		
		if (customerdto.getLastName() == null || customerdto.getLastName().trim().length() == 0) {
			return "Last Name is mandatory";
		}
		if (customerdto.getFullAddress() == null || customerdto.getFullAddress().trim().length() == 0) {
			return "Address is mandatory";
		}
		
		return null;
    }
    
    /*
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {

    	System.out.println( " File Upload is Called " );
    	//MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    	
    	MultipartHttpServletRequest multipartRequest = new DefaultMultipartHttpServletRequest(request);
    	
    	Iterator<String> fileNames =  multipartRequest.getFileNames();
    
    	while( fileNames.hasNext() ){
    		System.out.println(fileNames.next());
    	}
    
    	MultipartFile multipartFile = multipartRequest.getFile("file");    	
    	System.out.println(multipartFile);
    	ByteArrayInputStream stream = null;
    	String fileContents = null;
    	try {
    		stream = new   ByteArrayInputStream(multipartFile.getBytes());
    		fileContents = IOUtils.toString(stream, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	System.out.println(fileContents);
    	return null;
    }
    */
}
