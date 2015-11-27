package com.glocalme.service.dto;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.onewifi.util.DateSerializer;

public class TopupHistoryDto implements Serializable{
	
	private static final long serialVersionUID=1L;

	private String topupType;
	private String packageCountry;
	private String selectedPackage;
	private String currency;
	private Date priceListDate;
	private double topupAmount;
	public String getTopupType() {
		return topupType;
	}
	public void setTopupType(String topupType) {
		this.topupType = topupType;
	}
	public String getPackageCountry() {
		return packageCountry;
	}
	public void setPackageCountry(String packageCountry) {
		this.packageCountry = packageCountry;
	}
	public String getSelectedPackage() {
		return selectedPackage;
	}
	public void setSelectedPackage(String selectedPackage) {
		this.selectedPackage = selectedPackage;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getPriceListDate() {
		return priceListDate;
	}
	public void setPriceListDate(Date priceListDate) {
		this.priceListDate = priceListDate;
	}
	public double getTopupAmount() {
		return topupAmount;
	}
	public void setTopupAmount(double topupAmount) {
		this.topupAmount = topupAmount;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
}
