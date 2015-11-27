package com.onewifi.beans;
// Generated Nov 23, 2015 4:30:43 PM by Hibernate Tools 4.3.1.Final


import java.util.Date;

/**
 * CustomertopupId generated by hbm2java
 */
public class CustomertopupId  implements java.io.Serializable {


     private String customerId;
     private String deviceId;
     private Date topupDate;

    public CustomertopupId() {
    }

    public CustomertopupId(String customerId, String deviceId, Date topupDate) {
       this.customerId = customerId;
       this.deviceId = deviceId;
       this.topupDate = topupDate;
    }
   
    public String getCustomerId() {
        return this.customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getDeviceId() {
        return this.deviceId;
    }
    
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public Date getTopupDate() {
        return this.topupDate;
    }
    
    public void setTopupDate(Date topupDate) {
        this.topupDate = topupDate;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof CustomertopupId) ) return false;
		 CustomertopupId castOther = ( CustomertopupId ) other; 
         
		 return ( (this.getCustomerId()==castOther.getCustomerId()) || ( this.getCustomerId()!=null && castOther.getCustomerId()!=null && this.getCustomerId().equals(castOther.getCustomerId()) ) )
 && ( (this.getDeviceId()==castOther.getDeviceId()) || ( this.getDeviceId()!=null && castOther.getDeviceId()!=null && this.getDeviceId().equals(castOther.getDeviceId()) ) )
 && ( (this.getTopupDate()==castOther.getTopupDate()) || ( this.getTopupDate()!=null && castOther.getTopupDate()!=null && this.getTopupDate().equals(castOther.getTopupDate()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getCustomerId() == null ? 0 : this.getCustomerId().hashCode() );
         result = 37 * result + ( getDeviceId() == null ? 0 : this.getDeviceId().hashCode() );
         result = 37 * result + ( getTopupDate() == null ? 0 : this.getTopupDate().hashCode() );
         return result;
   }   


}

