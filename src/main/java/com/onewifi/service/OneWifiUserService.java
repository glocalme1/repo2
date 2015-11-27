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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

public class OneWifiUserService {
		
	private final RetailermasterHome retailermasterHome = RetailermasterHome.getInstance();
	private final UserroleHome userroleHome = UserroleHome.getInstance();
	private final CustomermasterHome customermasterHome = CustomermasterHome.getInstance();
	private final PartnermasterHome partnermasterHome = PartnermasterHome.getInstance();
	
	static {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	}

	public Userrole authenticateUser(String loginId, String password) throws Exception {
		Userrole userrole = null;
		try {	
			String psdHash = GenerateHash.generateHash(password);
			userrole = userroleHome.findById(loginId);
			boolean authenticated = false;
			if(userrole!=null) {
				if(userrole.getRoleName().equals("Partner")) {
					Partnermaster partnermaster = new Partnermaster();
					partnermaster.setLoginId(loginId);
					List<Partnermaster> listPartnermaster = partnermasterHome.findByExample(partnermaster);
					for(Partnermaster partnermaster1 : listPartnermaster) {
						if(partnermaster1!=null && partnermaster1.getPassword().equals(psdHash)) {
							authenticated = true;
							break;
						}
					}
				} else if(userrole.getRoleName().equals("Retailer")) {
					Retailermaster retailermaster = new Retailermaster();
					retailermaster.setLoginId(loginId);
					List<Retailermaster> listRetailermaster = retailermasterHome.findByExample(retailermaster);
					for(Retailermaster retailermaster1 : listRetailermaster) {
						if(retailermaster1!=null && retailermaster1.getPassword().equals(psdHash)) {
							authenticated = true;
							break;
						}
					}
				} else if(userrole.getRoleName().equals("User")) {
					Customermaster customermaster = new Customermaster();
					customermaster.setLoginId(loginId);
					List<Customermaster> listCustomermaster = customermasterHome.findByExample(customermaster);
					for(Customermaster customermaster1 : listCustomermaster) {
						if(customermaster1!=null && customermaster1.getPassword().equals(psdHash)) {
							authenticated = true;
							break;
						}
					}
				}
			}
			
			if(!authenticated) {
				userrole=null;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return userrole;
	}
	
	public Boolean changePassword(String loginId, String password) throws Exception {
		Boolean psdChanged = new Boolean(false);
		try {	
			String psdHash = GenerateHash.generateHash(password);
			Userrole userrole = userroleHome.findById(loginId);
			boolean authenticated = false;
			if(userrole!=null) {
				if(userrole.getRoleName().equals("Partner")) {
					Partnermaster partnermaster = new Partnermaster();
					partnermaster.setLoginId(loginId);
					List<Partnermaster> listPartnermaster = partnermasterHome.findByExample(partnermaster);
					for(Partnermaster partnermaster1 : listPartnermaster) {
						if(partnermaster1!=null) {
							partnermaster1.setPassword(psdHash);
							partnermasterHome.merge(partnermaster1);
							psdChanged = new Boolean(true);
							break;
						}
					}
				} else if(userrole.getRoleName().equals("Retailer")) {
					Retailermaster retailermaster = new Retailermaster();
					retailermaster.setLoginId(loginId);
					List<Retailermaster> listRetailermaster = retailermasterHome.findByExample(retailermaster);
					for(Retailermaster retailermaster1 : listRetailermaster) {
						if(retailermaster1!=null) {
							retailermaster1.setPassword(psdHash);
							retailermasterHome.merge(retailermaster1);
							psdChanged = new Boolean(true);					
							break;
						}
					}				
				} else if(userrole.getRoleName().equals("User")) {
					Customermaster customermaster = new Customermaster();
					customermaster.setLoginId(loginId);
					List<Customermaster> listCustomermaster = customermasterHome.findByExample(customermaster);
					for(Customermaster customermaster1 : listCustomermaster) {
						if(customermaster1!=null) {
							customermaster1.setPassword(psdHash);
							customermasterHome.merge(customermaster1);
							psdChanged = new Boolean(true);					
							break;
						}
					}
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return psdChanged;
	}
}