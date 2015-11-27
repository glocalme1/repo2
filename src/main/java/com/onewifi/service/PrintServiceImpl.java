package com.onewifi.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.onewifi.exception.GlocalMeException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class PrintServiceImpl implements PrintService {

	final Logger logger = Logger.getLogger(PrintServiceImpl.class);
	
	@Override
	public JasperReport compileReport( String reportFileName , String jasperFileName ) throws GlocalMeException {
		JasperReport jasperReport = null;
		logger.info("Report File Name: " + reportFileName);
		logger.info("Jasper File Name: " + jasperFileName);
		try {
			File reportFile = new File( jasperFileName );
	
			if (!reportFile.exists()) {
				System.out.println( "Jasper Doesnot exist, Compiling" );
				JasperCompileManager.compileReportToFile( reportFileName, jasperFileName );
				System.out.println( "Compilation Done" );
			}
			jasperReport = (JasperReport) JRLoader.loadObject( reportFile );
		} catch(Exception e) {
			throw new GlocalMeException();
		}
		return jasperReport;
 		
	}

	@Override
	public byte[] getReport(JasperReport jasperReport, List<?> dtoList) throws GlocalMeException {
		try {
			JRDataSource datasource = new JRBeanCollectionDataSource(dtoList, true);
			return JasperRunManager.runReportToPdf(jasperReport,  new HashMap<String,Object>(), datasource);
		} catch (JRException e) {
			e.printStackTrace();
			throw new GlocalMeException();
		}
	}





}
