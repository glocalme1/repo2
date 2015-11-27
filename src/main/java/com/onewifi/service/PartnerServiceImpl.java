package com.onewifi.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartnerServiceImpl implements PartnerService {

	
	@Autowired
	private OneWifiService oneWifiService;
	
	@Override 
	public String uploadDevices(File file) throws Exception {
		File[] files = new File[1];
		files[0] = file;
		
		return oneWifiService.uploadDevices(files);
	}

	@Override
	public String uploadRetailers(File file) throws Exception {
		File[] files = new File[1];
		files[0] = file;
		
		return oneWifiService.uploadRetailers(files);

	}
	
	
	@Override
	public String uploadRetailerDevices(File file) throws Exception {
		File[] files = new File[1];
		files[0] = file;
		
		return oneWifiService.uploadRetailerDevices(files);

	}

	@Override
	public String uploadPricing(File file) throws Exception {
		File[] files = new File[1];
		files[0] = file;
		
		return oneWifiService.uploadPricing(files);
	}



}
