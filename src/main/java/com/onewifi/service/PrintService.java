package com.onewifi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.onewifi.exception.GlocalMeException;

import net.sf.jasperreports.engine.JasperReport;

@Service
public interface PrintService {
	
	public JasperReport compileReport( String reportName , String jasperFileName) throws GlocalMeException;
	
	public byte[] getReport( JasperReport jasperReport, List<?> dtoList) throws GlocalMeException;

}
