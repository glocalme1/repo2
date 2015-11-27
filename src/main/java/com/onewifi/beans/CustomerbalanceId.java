package com.onewifi.beans;
// Generated Nov 23, 2015 4:30:43 PM by Hibernate Tools 4.3.1.Final



/**
 * CustomerbalanceId generated by hbm2java
 */
public class CustomerbalanceId  implements java.io.Serializable {


     private String customerId;
     private String deviceId;

    public CustomerbalanceId() {
    }

    public CustomerbalanceId(String customerId, String deviceId) {
       this.customerId = customerId;
       this.deviceId = deviceId;
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


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof CustomerbalanceId) ) return false;
		 CustomerbalanceId castOther = ( CustomerbalanceId ) other; 
         
		 return ( (this.getCustomerId()==castOther.getCustomerId()) || ( this.getCustomerId()!=null && castOther.getCustomerId()!=null && this.getCustomerId().equals(castOther.getCustomerId()) ) )
 && ( (this.getDeviceId()==castOther.getDeviceId()) || ( this.getDeviceId()!=null && castOther.getDeviceId()!=null && this.getDeviceId().equals(castOther.getDeviceId()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getCustomerId() == null ? 0 : this.getCustomerId().hashCode() );
         result = 37 * result + ( getDeviceId() == null ? 0 : this.getDeviceId().hashCode() );
         return result;
   }   


}


