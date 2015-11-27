package com.onewifi.beans;
// Generated Nov 23, 2015 3:35:31 PM by Hibernate Tools 4.3.1.Final


import java.util.Date;

/**
 * Customertopup generated by hbm2java
 */
public class Customertopup  implements java.io.Serializable {


     private CustomertopupId id;
     private Customermaster customermaster;
     private Devicemaster devicemaster;
     private String topupType;
     private Date priceListDate;
     private String packageCountry;
     private String selectedPackage;
     private String currency;
     private double topupAmount;
     private Integer usedCurrentloopCnt;
     private Double usedCurrentAmt;
     private Double usedCurrentData;
     private Integer usedAccloopCnt;
     private Double usedAccAmt;
     private Double usedAccData;
     private String exhaustedStatus;
     private Date latestLoginDate;
     private Date latestLogoutDate;
     private String latestPosition;
	 private String loginId;

    public Customertopup() {
    }

	
    public Customertopup(CustomertopupId id, Customermaster customermaster, Devicemaster devicemaster, String currency, double topupAmount) {
        this.id = id;
        this.customermaster = customermaster;
        this.devicemaster = devicemaster;
        this.currency = currency;
        this.topupAmount = topupAmount;
    }
    public Customertopup(CustomertopupId id, Customermaster customermaster, Devicemaster devicemaster, String topupType, Date priceListDate, String packageCountry, String selectedPackage, String currency, double topupAmount, Integer usedCurrentloopCnt, Double usedCurrentAmt, Double usedCurrentData, Integer usedAccloopCnt, Double usedAccAmt, Double usedAccData, String exhaustedStatus, Date latestLoginDate, Date latestLogoutDate, String latestPosition) {
       this.id = id;
       this.customermaster = customermaster;
       this.devicemaster = devicemaster;
       this.topupType = topupType;
       this.priceListDate = priceListDate;
       this.packageCountry = packageCountry;
       this.selectedPackage = selectedPackage;
       this.currency = currency;
       this.topupAmount = topupAmount;
       this.usedCurrentloopCnt = usedCurrentloopCnt;
       this.usedCurrentAmt = usedCurrentAmt;
       this.usedCurrentData = usedCurrentData;
       this.usedAccloopCnt = usedAccloopCnt;
       this.usedAccAmt = usedAccAmt;
       this.usedAccData = usedAccData;
       this.exhaustedStatus = exhaustedStatus;
       this.latestLoginDate = latestLoginDate;
       this.latestLogoutDate = latestLogoutDate;
       this.latestPosition = latestPosition;
    }
   
    public CustomertopupId getId() {
        return this.id;
    }
    
    public void setId(CustomertopupId id) {
        this.id = id;
    }
    public Customermaster getCustomermaster() {
        return this.customermaster;
    }
    
    public void setCustomermaster(Customermaster customermaster) {
        this.customermaster = customermaster;
    }
    public Devicemaster getDevicemaster() {
        return this.devicemaster;
    }
    
    public void setDevicemaster(Devicemaster devicemaster) {
        this.devicemaster = devicemaster;
    }
    public String getTopupType() {
        return this.topupType;
    }
    
    public void setTopupType(String topupType) {
        this.topupType = topupType;
    }
    public Date getPriceListDate() {
        return this.priceListDate;
    }
    
    public void setPriceListDate(Date priceListDate) {
        this.priceListDate = priceListDate;
    }
    public String getPackageCountry() {
        return this.packageCountry;
    }
    
    public void setPackageCountry(String packageCountry) {
        this.packageCountry = packageCountry;
    }
    public String getSelectedPackage() {
        return this.selectedPackage;
    }
    
    public void setSelectedPackage(String selectedPackage) {
        this.selectedPackage = selectedPackage;
    }
    public String getCurrency() {
        return this.currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public double getTopupAmount() {
        return this.topupAmount;
    }
    
    public void setTopupAmount(double topupAmount) {
        this.topupAmount = topupAmount;
    }
    public Integer getUsedCurrentloopCnt() {
        return this.usedCurrentloopCnt;
    }
    
    public void setUsedCurrentloopCnt(Integer usedCurrentloopCnt) {
        this.usedCurrentloopCnt = usedCurrentloopCnt;
    }
    public Double getUsedCurrentAmt() {
        return this.usedCurrentAmt;
    }
    
    public void setUsedCurrentAmt(Double usedCurrentAmt) {
        this.usedCurrentAmt = usedCurrentAmt;
    }
    public Double getUsedCurrentData() {
        return this.usedCurrentData;
    }
    
    public void setUsedCurrentData(Double usedCurrentData) {
        this.usedCurrentData = usedCurrentData;
    }
    public Integer getUsedAccloopCnt() {
        return this.usedAccloopCnt;
    }
    
    public void setUsedAccloopCnt(Integer usedAccloopCnt) {
        this.usedAccloopCnt = usedAccloopCnt;
    }
    public Double getUsedAccAmt() {
        return this.usedAccAmt;
    }
    
    public void setUsedAccAmt(Double usedAccAmt) {
        this.usedAccAmt = usedAccAmt;
    }
    public Double getUsedAccData() {
        return this.usedAccData;
    }
    
    public void setUsedAccData(Double usedAccData) {
        this.usedAccData = usedAccData;
    }
    public String getExhaustedStatus() {
        return this.exhaustedStatus;
    }
    
    public void setExhaustedStatus(String exhaustedStatus) {
        this.exhaustedStatus = exhaustedStatus;
    }
    public Date getLatestLoginDate() {
        return this.latestLoginDate;
    }
    
    public void setLatestLoginDate(Date latestLoginDate) {
        this.latestLoginDate = latestLoginDate;
    }
    public Date getLatestLogoutDate() {
        return this.latestLogoutDate;
    }
    
    public void setLatestLogoutDate(Date latestLogoutDate) {
        this.latestLogoutDate = latestLogoutDate;
    }
    public String getLatestPosition() {
        return this.latestPosition;
    }
    
    public void setLatestPosition(String latestPosition) {
        this.latestPosition = latestPosition;
    }

	public String getLoginId() {
        return this.loginId;
    }
    
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }



}

