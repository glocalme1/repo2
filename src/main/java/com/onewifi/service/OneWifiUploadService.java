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

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.onewifi.beans.*;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.codec.binary.Base64;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

public class OneWifiUploadService {
		
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
	private static final String CONTENT_DISPOSITION = "content-disposition";
	private static final String CONTENT_DISPOSITION_FILENAME = "filename";
	private final DevicemasterHome deviceMasterHome = DevicemasterHome.getInstance();
	private final RetailermasterHome retailermasterHome = RetailermasterHome.getInstance();
	private final UserroleHome userroleHome = UserroleHome.getInstance();
	private final RetailerdeviceHome retailerdeviceHome = RetailerdeviceHome.getInstance();
	private final PricingHome pricingHome = PricingHome.getInstance();
	private final PackagemasterHome packagemasterHome = PackagemasterHome.getInstance();
	private final GlocalmeClient client = new GlocalmeClient();	
	private final OneWifiUtilityService oneWifiUtilityService = new OneWifiUtilityService();
	
	
	static {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	}

	/**
     * Returns the filename from the content-disposition header of the given part.
     */
    private String getFilename(Part part) {
        for (String cd : part.getHeader(CONTENT_DISPOSITION).split(";")) {
            if (cd.trim().startsWith(CONTENT_DISPOSITION_FILENAME)) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
	
	public String uploadDevices(File[] files) throws Exception {
		
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		XSSFWorkbook xworkbook = null;
		XSSFSheet xsheet = null;
		Iterator<Row> rowIterator = null;	
		String filename = null;
		String extension = null;
		FileInputStream xlsFile = null;
		int cnt = 0;
		String result = new String("{0");	
		StringBuffer result1 = new StringBuffer("");	
		Statuscodemaster statuscode = new Statuscodemaster();
		statuscode.setStatusCode("S2");
		Devicemaster devicemaster = null;		
		
		for(int k=0; files!=null && k < files.length; k++) { 		
			
			try {		 
				if(files[k]!=null) {
					xlsFile = new FileInputStream(files[k]);
					
					filename = files[k].getName();
					extension = filename.substring(filename.lastIndexOf('.'));
					if(extension.equals(".xls")) {
						workbook = new HSSFWorkbook(xlsFile);
						sheet = workbook.getSheetAt(0);
						rowIterator = sheet.iterator();
					} else if(extension.equals(".xlsx")) {
						xworkbook = new XSSFWorkbook(xlsFile);
						xsheet = xworkbook.getSheetAt(0);
						rowIterator = xsheet.iterator();
					}
					 
					for(int i=0; rowIterator.hasNext(); i++) {
						Row row = rowIterator.next();				
						if(i>=3) {					 
							//For each row, iterate through each columns
							Iterator<Cell> cellIterator = row.cellIterator();
							boolean data = false;
							for(int j=0;cellIterator.hasNext(); j++) {
								 
								Cell cell = cellIterator.next();
								if(data || (cell.getColumnIndex()==0 && cell.getCellType()!=Cell.CELL_TYPE_BLANK)) {
									if(j==0) {
										devicemaster = new Devicemaster();
										devicemaster.setStatuscodemaster(statuscode);
									}
									switch(cell.getCellType()) {
										case Cell.CELL_TYPE_NUMERIC:
											System.out.print(cell.getNumericCellValue() + " number \t\t");
											break;
										case Cell.CELL_TYPE_STRING:
											System.out.print(cell.getStringCellValue() + " string \t\t");
											break;
									}
									if(cell.getCellType()!=Cell.CELL_TYPE_BLANK) {
										if(j==0) {
											devicemaster.setUserCode(cell.getStringCellValue().trim());							
										} else if(j==1) {
											devicemaster.setImei(cell.getStringCellValue().substring(1).trim());	
										} else if(j==2) {
											devicemaster.setDeviceSerialNo(cell.getStringCellValue().trim());	
										} else if(j==3) {
											devicemaster.setPassword(cell.getStringCellValue().trim());	
										} else if(j==4) {
											devicemaster.setPartnerCode(cell.getStringCellValue().trim());	
										}
									}
									data = true;
								} else {
									devicemaster = null;
									data = false;
									break;
								}
							}					
							if(devicemaster!=null && devicemaster.getImei()!=null && !devicemaster.getImei().equals("")) {
								Boolean deviceValid = oneWifiUtilityService.validateIMEI(devicemaster.getImei());
								System.out.println("\n\n\n"+deviceValid+deviceValid.booleanValue()+"=="+devicemaster.getImei());
								if(deviceValid!=null && !deviceValid.booleanValue()) {
									
									System.out.println("\n\n\n");
									JSONObject retObj = client.invokeUserBinding(devicemaster.getPartnerCode(), devicemaster.getUserCode(), devicemaster.getImei(), devicemaster.getPassword(), "0");
									System.out.println("\n\nHTTP POST Response:"+retObj.toString());
									if(retObj!=null && retObj.getString("resultCode").equals("0000")) {
										JSONObject ret1Obj = client.invokeUserBinding(devicemaster.getPartnerCode(), devicemaster.getUserCode(), devicemaster.getImei(), devicemaster.getPassword(), "1");
										System.out.println("\n\nHTTP POST Response:"+ret1Obj.toString());
										if((ret1Obj!=null && ret1Obj.getString("resultCode").equals("0000")) || true) {
											devicemaster.setDeviceId("D"+i+devicemaster.getImei());
											devicemaster.setDeviceBoundStatus("UNBIND");
											deviceMasterHome.persist(devicemaster);
											result = result.substring(result.lastIndexOf("{"),1) + Integer.toString(++cnt);
										} else {
											result1.append(devicemaster.getImei() + " is not added due to an error when testing the de-activation. Result code is: "+ret1Obj.getString("resultCode")+ ". Result description is: "+ ret1Obj.getString("resultDesc")+".\n\n");											
										}
									} else {
										result1.append(devicemaster.getImei() + " is not added due to an error when testing the activation. Result code is: "+retObj.getString("resultCode")+ ". Result description is: "+ retObj.getString("resultDesc")+".\n\n");
									}
								}
								devicemaster = null;								
								data = false;								
							} 
						}							
					}	
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} 
		}
		result = result + "} Devices have been added.";
		if(!result1.equals("")) {
			result = result + "\n\n" + result1.toString();
		}
		return result;
	}
	
	public String uploadRetailers(File[] files) throws Exception {
		
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		XSSFWorkbook xworkbook = null;
		XSSFSheet xsheet = null;
		Iterator<Row> rowIterator = null;	
		String filename = null;
		String extension = null;
		FileInputStream xlsFile = null;
		String result = new String("{0");
		int cnt = 0;
		Retailermaster retailermaster = null;
		Userrole userrole = null;
		
		for(int k=0; files!=null && k < files.length; k++) { 		
			
			try {		 
				if(files[k]!=null) {
					xlsFile = new FileInputStream(files[k]);
					
					filename = files[k].getName();
					extension = filename.substring(filename.lastIndexOf('.'));
					if(extension.equals(".xls")) {
						workbook = new HSSFWorkbook(xlsFile);
						sheet = workbook.getSheetAt(1);
						rowIterator = sheet.iterator();
					} else if(extension.equals(".xlsx")) {
						xworkbook = new XSSFWorkbook(xlsFile);
						xsheet = xworkbook.getSheetAt(1);
						rowIterator = xsheet.iterator();
					}
					 
					for(int i=0; rowIterator.hasNext(); i++) {
						Row row = rowIterator.next();				
						if(i>=1) {					 
							//For each row, iterate through each columns
							Iterator<Cell> cellIterator = row.cellIterator();
							boolean data = false;
							for(int j=0;cellIterator.hasNext(); j++) {
								 
								Cell cell = cellIterator.next();
								if(data || (cell.getColumnIndex()==0 && cell.getCellType()!=Cell.CELL_TYPE_BLANK)) {
									if(j==0) {
										retailermaster = new Retailermaster();
										userrole = new Userrole();
									}
									switch(cell.getCellType()) {
										case Cell.CELL_TYPE_NUMERIC:
											System.out.print(cell.getNumericCellValue() + " number \t\t");
											break;
										case Cell.CELL_TYPE_STRING:
											System.out.print(cell.getStringCellValue() + " string \t\t");
											break;
									}
									if(cell.getCellType()!=Cell.CELL_TYPE_BLANK) {
										if(j==0) {
											retailermaster.setRetailerId(cell.getStringCellValue().trim());							
										} else if(j==1) {
											retailermaster.setCoyRetailerName(cell.getStringCellValue().trim());	
										} else if(j==2) {
											retailermaster.setCoyContactNumber(new Integer((int)cell.getNumericCellValue()));	
										} else if(j==3) {
											retailermaster.setDirectorName(cell.getStringCellValue().trim());	
										} else if(j==4) {
											retailermaster.setIdentityId(cell.getStringCellValue().trim());	
										} else if(j==5) {
											retailermaster.setAddress(cell.getStringCellValue().trim());	
										} else if(j==6) {
											retailermaster.setRetailerContactNumber(new Integer((int)cell.getNumericCellValue()));	
										} else if(j==7) {
											retailermaster.setLoginId(cell.getStringCellValue().trim());	
											userrole.setLoginId(cell.getStringCellValue().trim());
											userrole.setRoleName("Retailer");
										} else if(j==8) {
											retailermaster.setUserName(cell.getStringCellValue().trim());	
										} else if(j==9) {
											retailermaster.setPassword(GenerateHash.generateHash(cell.getStringCellValue().trim()));								
										}
									}
									data = true;
								} else {
									retailermaster = null;
									data = false;
									break;
								}
							}					
							if(retailermaster!=null && retailermaster.getLoginId()!=null) {
								Boolean retailerValid = oneWifiUtilityService.validateRetailer(retailermaster.getLoginId());
								if(retailerValid!=null && !retailerValid.booleanValue()) {
									System.out.println("\n\n\n");
									retailermaster.setCreationDate(new java.util.Date());
									retailermaster.setSecretQuestion1("What is my first school name");
									retailermaster.setSecretQuestionAnswer1("testing");
									retailermaster.setSecretQuestion2("What is my best friend name");
									retailermaster.setSecretQuestionAnswer2("testing");
									retailermasterHome.persist(retailermaster);																					

									userroleHome.persist(userrole);
									
									result = result.substring(result.lastIndexOf("{"),1) + Integer.toString(++cnt);
								}
								retailermaster = null;
								data = false;
							} 
						}							
					}	
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} 
		}
		result = result + "} Retailers have been added.\n\n";
		return result.toString();
	}
	
	public String uploadRetailerDevices(File[] files) throws Exception {
		
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		XSSFWorkbook xworkbook = null;
		XSSFSheet xsheet = null;
		Iterator<Row> rowIterator = null;	
		String filename = null;
		String extension = null;
		FileInputStream xlsFile = null;
		String result = new String("{0");
		int cnt = 0;
		Retailerdevice retailerdevice = null;
		RetailerdeviceId retailerdeviceId = null;
		
		for(int k=0; files!=null && k < files.length; k++) { 		
			
			try {		 
				if(files[k]!=null) {
					xlsFile = new FileInputStream(files[k]);
					
					filename = files[k].getName();
					extension = filename.substring(filename.lastIndexOf('.'));
					if(extension.equals(".xls")) {
						workbook = new HSSFWorkbook(xlsFile);
						sheet = workbook.getSheetAt(2);
						rowIterator = sheet.iterator();
					} else if(extension.equals(".xlsx")) {
						xworkbook = new XSSFWorkbook(xlsFile);
						xsheet = xworkbook.getSheetAt(2);
						rowIterator = xsheet.iterator();
					}
					 
					for(int i=0; rowIterator.hasNext(); i++) {
						Row row = rowIterator.next();				
						if(i>=2) {					 
							//For each row, iterate through each columns
							Iterator<Cell> cellIterator = row.cellIterator();
							boolean data = false;
							for(int j=0;cellIterator.hasNext(); j++) {
								 
								Cell cell = cellIterator.next();
								if(data || (cell.getColumnIndex()==0 && cell.getCellType()!=Cell.CELL_TYPE_BLANK)) {
									if(j==0) {
										retailerdevice = new Retailerdevice();
										retailerdeviceId = new RetailerdeviceId();
									}
									switch(cell.getCellType()) {
										case Cell.CELL_TYPE_NUMERIC:
											System.out.print(cell.getNumericCellValue() + " number \t\t");
											break;
										case Cell.CELL_TYPE_STRING:
											System.out.print(cell.getStringCellValue() + " string \t\t");
											break;
									}
									if(cell.getCellType()!=Cell.CELL_TYPE_BLANK) {
										if(j==0) {
											retailerdeviceId.setAssignerId(cell.getStringCellValue().trim());							
										} else if(j==1) {
											retailerdeviceId.setDeviceSerialNo(cell.getStringCellValue().trim());	
										} else if(j==2) {
											retailerdevice.setPurpose(cell.getStringCellValue().trim());	
										} else if(j==3) {
											retailerdevice.setDays(new Double(cell.getNumericCellValue()));	
										} else if(j==4) {
											retailerdevice.setPrice(new Double(cell.getNumericCellValue()));	
										}										
									}
									data = true;
								} else {
									retailerdevice = null;
									retailerdeviceId = null;
									data = false;
									break;
								}
							}					
							if(retailerdeviceId!=null) {
								Boolean retailerDeviceValid = oneWifiUtilityService.validateRetailerDevice(retailerdeviceId);
								boolean retailerIdValid = false;
								Boolean deviceSerialNoValid = null;
								if(retailerDeviceValid!=null && !retailerDeviceValid.booleanValue()) {
									if(retailerdevice!=null && retailerdevice.getPurpose().equalsIgnoreCase("Ownership") && retailerdeviceId.getAssignerId()!=null) {
										Boolean retailerIdValidObj = oneWifiUtilityService.validateRetailerId(retailerdeviceId.getAssignerId());
										if(retailerIdValidObj!=null) {
											retailerIdValid = retailerIdValidObj.booleanValue();									
										}
									} else {
										retailerIdValid = true;										
									}
									deviceSerialNoValid = oneWifiUtilityService.validateDeviceSerialNo(retailerdeviceId.getDeviceSerialNo());
									if(retailerIdValid && (deviceSerialNoValid!=null && deviceSerialNoValid.booleanValue())) {									
										System.out.println("\n\n\n");
										retailerdevice.setId(retailerdeviceId);
										retailerdeviceHome.persist(retailerdevice);						
										
										/*Devicemaster devicemaster = new Devicemaster();
										devicemaster.setImei(retailerdeviceId.getDeviceSerialNo());
										List<Devicemaster> listDeviceMaster = deviceMasterHome.findByExample(devicemaster);
										Statuscodemaster statuscode = new Statuscodemaster();
										statuscode.setStatusCode("S3");
										for(Devicemaster devicemaster1 : listDeviceMaster) {
											if(devicemaster1!=null) {
												devicemaster1.setStatuscodemaster(statuscode);
												deviceMasterHome.merge(devicemaster1);
											}
											break;
										}*/								
										
										result = result.substring(result.lastIndexOf("{"),1) + Integer.toString(++cnt);
									}
								}
								retailerdevice = null;
								retailerdeviceId = null;
								data = false;
							}
						}							
					}	
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} 
		}
		result = result + "} Retailer Devices have been added.\n\n";
		return result.toString();
	}
	
	public String uploadPricing(File[] files) throws Exception {
		
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		XSSFWorkbook xworkbook = null;
		XSSFSheet xsheet = null;
		Iterator<Row> rowIterator = null;	
		String filename = null;
		String extension = null;
		FileInputStream xlsFile = null;
		String result = new String("{0");
		int cnt = 0;
		PricingId pricingId = null;
		Pricing pricing = null;
		java.util.Date validDate = null;
		Packagemaster packagemaster = null;
		List<Packagemaster> listPackagemaster = packagemasterHome.findPackages();
		
		for(int k=0; files!=null && k < files.length; k++) { 		
			
			try {		 
				if(files[k]!=null) {
					xlsFile = new FileInputStream(files[k]);
					
					filename = files[k].getName();
					extension = filename.substring(filename.lastIndexOf('.'));
					if(extension.equals(".xls")) {
						workbook = new HSSFWorkbook(xlsFile);
						sheet = workbook.getSheetAt(3);
						rowIterator = sheet.iterator();
					} else if(extension.equals(".xlsx")) {
						xworkbook = new XSSFWorkbook(xlsFile);
						xsheet = xworkbook.getSheetAt(3);
						rowIterator = xsheet.iterator();
					}
					 
					for(int i=0; rowIterator.hasNext(); i++) {
						Row row = rowIterator.next();				
						if(i>=3) {					 
							//For each row, iterate through each columns
							Iterator<Cell> cellIterator = row.cellIterator();
							boolean data = false;
							for(int j=0;cellIterator.hasNext(); j++) {
								 
								Cell cell = cellIterator.next();
								if(data || (cell.getColumnIndex()==0 && cell.getCellType()!=Cell.CELL_TYPE_BLANK)) {
									if(j==0) {
										pricingId = new PricingId();
										pricing = new Pricing();
									}
									switch(cell.getCellType()) {
										case Cell.CELL_TYPE_NUMERIC:
											System.out.print(cell.getNumericCellValue() + " number \t\t");
											break;
										case Cell.CELL_TYPE_STRING:
											System.out.print(cell.getStringCellValue() + " string \t\t");
											break;
									}
									if(cell.getCellType()!=Cell.CELL_TYPE_BLANK) {										
										if(j==0) {
											pricingId.setValidDate(cell.getDateCellValue());																		
										} else if(j==1) {
											pricingId.setProductCode(cell.getStringCellValue().trim());	
										} else if(j==2) {
											pricing.setCountry(cell.getStringCellValue().trim());	
										} else if(j==3) {
											pricing.setPayAsYouGo(new Double(cell.getNumericCellValue()));	
										} else if(j==4) {			
											if(listPackagemaster!=null && listPackagemaster.get(0)!=null) {
												packagemaster = (Packagemaster)listPackagemaster.get(0);
												pricing.setDaily150mb(packagemaster.getPackageTypeCode()+":"
												+packagemaster.getPackageTypeName()+":"+new Double(cell.getNumericCellValue()));	
											}
										} else if(j==5) {
											if(listPackagemaster!=null && listPackagemaster.get(1)!=null) {
												packagemaster = (Packagemaster)listPackagemaster.get(1);
												pricing.setDays7450mb(packagemaster.getPackageTypeCode()+":"
												+packagemaster.getPackageTypeName()+":"+new Double(cell.getNumericCellValue()));	
											}
										} else if(j==6) {
											if(listPackagemaster!=null && listPackagemaster.get(2)!=null) {
												packagemaster = (Packagemaster)listPackagemaster.get(2);
												pricing.setDays301gb(packagemaster.getPackageTypeCode()+":"
												+packagemaster.getPackageTypeName()+":"+new Double(cell.getNumericCellValue()));	
											}
										} else if(j==7) {
											if(listPackagemaster!=null && listPackagemaster.get(3)!=null) {
												packagemaster = (Packagemaster)listPackagemaster.get(3);
												pricing.setDays902gb(packagemaster.getPackageTypeCode()+":"
												+packagemaster.getPackageTypeName()+":"+new Double(cell.getNumericCellValue()));	
											}
										} else if(j==8) {
											if(listPackagemaster!=null && listPackagemaster.get(4)!=null) {
												packagemaster = (Packagemaster)listPackagemaster.get(4);
												pricing.setDays1803gb(packagemaster.getPackageTypeCode()+":"
												+packagemaster.getPackageTypeName()+":"+new Double(cell.getNumericCellValue()));	
											}
										} else if(j==9) {
											if(listPackagemaster!=null && listPackagemaster.get(5)!=null) {
												packagemaster = (Packagemaster)listPackagemaster.get(5);
												pricing.setDay1Pass(packagemaster.getPackageTypeCode()+":"
												+packagemaster.getPackageTypeName()+":"+new Double(cell.getNumericCellValue()));	
											}
										} else if(j==10) {
											if(listPackagemaster!=null && listPackagemaster.get(6)!=null) {
												packagemaster = (Packagemaster)listPackagemaster.get(6);
												pricing.setDays5Pass(packagemaster.getPackageTypeCode()+":"
												+packagemaster.getPackageTypeName()+":"+new Double(cell.getNumericCellValue()));	
											}
										} else if(j==11) {
											if(listPackagemaster!=null && listPackagemaster.get(7)!=null) {
												packagemaster = (Packagemaster)listPackagemaster.get(7);
												pricing.setDays10Pass(packagemaster.getPackageTypeCode()+":"
												+packagemaster.getPackageTypeName()+":"+new Double(cell.getNumericCellValue()));	
											}
										} else if(j==12) {
											if(listPackagemaster!=null && listPackagemaster.get(8)!=null) {
												packagemaster = (Packagemaster)listPackagemaster.get(8);
												pricing.setDays20Pass(packagemaster.getPackageTypeCode()+":"
												+packagemaster.getPackageTypeName()+":"+new Double(cell.getNumericCellValue()));	
											}
										}
									}
									data = true;
								} else {
									pricingId = null;
									pricing = null;
									data = false;
									break;
								}
							}					
							if(pricingId!=null) {
								pricingId.setPricingId("PR1");								
								Boolean pricingValid = oneWifiUtilityService.validatePricing(pricingId);
								if(pricingValid!=null && !pricingValid.booleanValue()) {
									System.out.println("\n\n\n");
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
									pricingId.setValidDate(sdf.parse(sdf.format(pricingId.getValidDate())));
									pricing.setId(pricingId);
									pricingHome.persist(pricing);
									result = result.substring(result.lastIndexOf("{"),1) + Integer.toString(++cnt);
								}
								pricingId = null;
								pricing = null;
								data = false;
							}							
						}							
					}	
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} 
		}
		result = result + "} Pricing records have been added.\n\n";
		return result;
	}
			
	public File[] getFiles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		File[] files =  null;
		try {
			files = new File[request.getParts().size()];
			int i = 0;
			for (Part part : request.getParts()) {			
				// Write uploaded file.			
				String filename = getFilename(part);
				if(filename!=null && !filename.equals("")) {
					System.out.println("filename="+filename);
					filename = filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1);
				
					String prefix = filename;
					String suffix = "";
					if (filename.contains(".")) {
						prefix = filename.substring(0, filename.lastIndexOf('.'));
						suffix = filename.substring(filename.lastIndexOf('.'));
					}

					files[i] = File.createTempFile(prefix + "_", suffix, new File(request.getServletContext().getRealPath("/pages/")));
					InputStream input = new BufferedInputStream(part.getInputStream(), DEFAULT_BUFFER_SIZE);
					OutputStream output = new BufferedOutputStream(new FileOutputStream(files[i++]), DEFAULT_BUFFER_SIZE);
					byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
					for (int length = 0; ((length = input.read(buffer)) > 0);) {
						output.write(buffer, 0, length);
					}				
					if (output != null) output.close(); 
					if (input != null) input.close(); 
					part.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return files;
	}
}