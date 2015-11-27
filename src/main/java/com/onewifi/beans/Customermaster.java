package com.onewifi.beans;
// Generated Nov 5, 2015 9:32:52 AM by Hibernate Tools 4.3.1.Final


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Customermaster generated by hbm2java
 */
public class Customermaster  implements java.io.Serializable {


     private String customerId;
     private Statuscodemaster statuscodemaster;
     private String loginId;
     private String password;
     private String fullName;
     private String identityType;
     private String identityId;
     private byte[] identityImage;
     private String address;
     private Date dob;
     private Integer contactNo;
     private Date creationDate;
     private String secretQuestion1;
     private String secretQuestionAnswer1;
     private String secretQuestion2;
     private String secretQuestionAnswer2;
     private Set<Customerbalance> customerbalances = new HashSet<Customerbalance>(0);
     private Set<Customerdevice> customerdevices = new HashSet<Customerdevice>(0);
     private Set<Customertopup> customertopups = new HashSet<Customertopup>(0);
     private String imei;	 

    public Customermaster() {
    }

	
    public Customermaster(String customerId, Statuscodemaster statuscodemaster, String loginId, String password, String fullName, String identityType, String identityId, String address, Date creationDate, String secretQuestion1, String secretQuestionAnswer1, String secretQuestion2, String secretQuestionAnswer2) {
        this.customerId = customerId;
        this.statuscodemaster = statuscodemaster;
        this.loginId = loginId;
        this.password = password;
        this.fullName = fullName;
        this.identityType = identityType;
        this.identityId = identityId;
        this.address = address;
        this.creationDate = creationDate;
        this.secretQuestion1 = secretQuestion1;
        this.secretQuestionAnswer1 = secretQuestionAnswer1;
        this.secretQuestion2 = secretQuestion2;
        this.secretQuestionAnswer2 = secretQuestionAnswer2;
    }
    public Customermaster(String customerId, Statuscodemaster statuscodemaster, String loginId, String password, String fullName, String identityType, String identityId, byte[] identityImage, String address, Date dob, Integer contactNo, Date creationDate, String secretQuestion1, String secretQuestionAnswer1, String secretQuestion2, String secretQuestionAnswer2, Set<Customerbalance> customerbalances, Set<Customerdevice> customerdevices, Set<Customertopup> customertopups) {
       this.customerId = customerId;
       this.statuscodemaster = statuscodemaster;
       this.loginId = loginId;
       this.password = password;
       this.fullName = fullName;
       this.identityType = identityType;
       this.identityId = identityId;
       this.identityImage = identityImage;
       this.address = address;
       this.dob = dob;
       this.contactNo = contactNo;
       this.creationDate = creationDate;
       this.secretQuestion1 = secretQuestion1;
       this.secretQuestionAnswer1 = secretQuestionAnswer1;
       this.secretQuestion2 = secretQuestion2;
       this.secretQuestionAnswer2 = secretQuestionAnswer2;
       this.customerbalances = customerbalances;
       this.customerdevices = customerdevices;
       this.customertopups = customertopups;
    }
   
    public String getCustomerId() {
        return this.customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public Statuscodemaster getStatuscodemaster() {
        return this.statuscodemaster;
    }
    
    public void setStatuscodemaster(Statuscodemaster statuscodemaster) {
        this.statuscodemaster = statuscodemaster;
    }
    public String getLoginId() {
        return this.loginId;
    }
    
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public String getFullName() {
        return this.fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getIdentityType() {
        return this.identityType;
    }
    
    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }
    public String getIdentityId() {
        return this.identityId;
    }
    
    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }
    public byte[] getIdentityImage() {
        return this.identityImage;
    }
    
    public void setIdentityImage(byte[] identityImage) {
        this.identityImage = identityImage;
    }
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    public Date getDob() {
        return this.dob;
    }
    
    public void setDob(Date dob) {
        this.dob = dob;
    }
    public Integer getContactNo() {
        return this.contactNo;
    }
    
    public void setContactNo(Integer contactNo) {
        this.contactNo = contactNo;
    }
    public Date getCreationDate() {
        return this.creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public String getSecretQuestion1() {
        return this.secretQuestion1;
    }
    
    public void setSecretQuestion1(String secretQuestion1) {
        this.secretQuestion1 = secretQuestion1;
    }
    public String getSecretQuestionAnswer1() {
        return this.secretQuestionAnswer1;
    }
    
    public void setSecretQuestionAnswer1(String secretQuestionAnswer1) {
        this.secretQuestionAnswer1 = secretQuestionAnswer1;
    }
    public String getSecretQuestion2() {
        return this.secretQuestion2;
    }
    
    public void setSecretQuestion2(String secretQuestion2) {
        this.secretQuestion2 = secretQuestion2;
    }
    public String getSecretQuestionAnswer2() {
        return this.secretQuestionAnswer2;
    }
    
    public void setSecretQuestionAnswer2(String secretQuestionAnswer2) {
        this.secretQuestionAnswer2 = secretQuestionAnswer2;
    }
    public Set<Customerbalance> getCustomerbalances() {
        return this.customerbalances;
    }
    
    public void setCustomerbalances(Set<Customerbalance> customerbalances) {
        this.customerbalances = customerbalances;
    }
    public Set<Customerdevice> getCustomerdevices() {
        return this.customerdevices;
    }
    
    public void setCustomerdevices(Set<Customerdevice> customerdevices) {
        this.customerdevices = customerdevices;
    }
    public Set<Customertopup> getCustomertopups() {
        return this.customertopups;
    }
    
    public void setCustomertopups(Set<Customertopup> customertopups) {
        this.customertopups = customertopups;
    }

    public String getImei() {
        return this.imei;
    }
    
    public void setImei(String imei) {
        this.imei = imei;
    }
	
}

