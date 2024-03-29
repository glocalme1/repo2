package com.onewifi.beans;
// Generated Nov 23, 2015 4:30:43 PM by Hibernate Tools 4.3.1.Final


import java.util.HashSet;
import java.util.Set;

/**
 * Statuscodemaster generated by hbm2java
 */
public class Statuscodemaster  implements java.io.Serializable {


     private String statusCode;
     private String statusDetail;
     private Set<Customermaster> customermasters = new HashSet<Customermaster>(0);
     private Set<Customerdevice> customerdevices = new HashSet<Customerdevice>(0);
     private Set<Devicemaster> devicemasters = new HashSet<Devicemaster>(0);

    public Statuscodemaster() {
    }

	
    public Statuscodemaster(String statusCode, String statusDetail) {
        this.statusCode = statusCode;
        this.statusDetail = statusDetail;
    }
    public Statuscodemaster(String statusCode, String statusDetail, Set<Customermaster> customermasters, Set<Customerdevice> customerdevices, Set<Devicemaster> devicemasters) {
       this.statusCode = statusCode;
       this.statusDetail = statusDetail;
       this.customermasters = customermasters;
       this.customerdevices = customerdevices;
       this.devicemasters = devicemasters;
    }
   
    public String getStatusCode() {
        return this.statusCode;
    }
    
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
    public String getStatusDetail() {
        return this.statusDetail;
    }
    
    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }
    public Set<Customermaster> getCustomermasters() {
        return this.customermasters;
    }
    
    public void setCustomermasters(Set<Customermaster> customermasters) {
        this.customermasters = customermasters;
    }
    public Set<Customerdevice> getCustomerdevices() {
        return this.customerdevices;
    }
    
    public void setCustomerdevices(Set<Customerdevice> customerdevices) {
        this.customerdevices = customerdevices;
    }
    public Set<Devicemaster> getDevicemasters() {
        return this.devicemasters;
    }
    
    public void setDevicemasters(Set<Devicemaster> devicemasters) {
        this.devicemasters = devicemasters;
    }




}


