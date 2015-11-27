package com.onewifi.service;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.MultipartConfigElement;
import javax.servlet.annotation.MultipartConfig;

import com.onewifi.beans.*;

@WebServlet(name = "UploadServlet", urlPatterns = {"/uploadServlet"})
@MultipartConfig(fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
public class UploadServlet extends HttpServlet {
	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.getRequestDispatcher("/pages/upload.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
		try {
			OneWifiService oneWifiService = new OneWifiService();
			if(request.getParameter("action").equals("BuyDevice")) {		
				Customermaster customermaster = new Customermaster();
				customermaster.setIdentityType("NRIC");
				customermaster.setIdentityId("S1234567Z");
				customermaster.setIdentityImage(new String("NRIC_Image").getBytes());
				customermaster.setFullName("John M");
				customermaster.setLoginId("test@gmail.com");
				customermaster.setAddress("Blk 245|Singapore|");
				customermaster.setContactNo(12345679);
				customermaster.setImei("356166060296562");
				oneWifiService.saveCustomer("Allan.Lim@expressway.com", customermaster);
			} else {
				String result = "";
				File[] files = oneWifiService.getFiles(request, response);
		
				if(files!=null  && files.length>0) {
				System.out.println(request.getParameter("action"));
					if(request.getParameter("action").equals("Devices")) {		
					
						result = oneWifiService.uploadDevices(files);				
						
					} else if(request.getParameter("action").equals("Retailers")) {
						
						result = oneWifiService.uploadRetailers(files);						
					
					} else if(request.getParameter("action").equals("RetailerDevices")) {
						
						result = oneWifiService.uploadRetailerDevices(files);
					
					} else if(request.getParameter("action").equals("Pricing")) {
					
						result = oneWifiService.uploadPricing(files);						
					} else if(request.getParameter("action").equals("All")) {		
					
						result = result + oneWifiService.uploadDevices(files);
						result = result + oneWifiService.uploadRetailers(files);
						result = result + oneWifiService.uploadRetailerDevices(files);	
						result = result + oneWifiService.uploadPricing(files);		
					} else if(request.getParameter("action").equals("fetchPricing")) {		
						System.out.println(oneWifiService.fetchPricing().size());	
					}
					if(result.trim().equals("")) {
						request.setAttribute("status", "success");					
					} else {
						request.setAttribute("status", "failure");		
					}
					request.setAttribute("result", result);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
        request.getRequestDispatcher("/pages/upload.jsp").forward(request, response);
    }		
}