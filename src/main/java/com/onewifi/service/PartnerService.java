package com.onewifi.service;

import java.io.File;

import org.springframework.stereotype.Service;

@Service
public interface PartnerService {
	
	public String uploadDevices(File file) throws Exception;
	
	public String uploadRetailers(File files) throws Exception;
	
	public String uploadRetailerDevices(File files) throws Exception;
	
	public String uploadPricing(File files) throws Exception;
	
}
