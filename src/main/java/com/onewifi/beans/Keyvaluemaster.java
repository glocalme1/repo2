package com.onewifi.beans;
// Generated Nov 23, 2015 4:30:43 PM by Hibernate Tools 4.3.1.Final



/**
 * Keyvaluemaster generated by hbm2java
 */
public class Keyvaluemaster  implements java.io.Serializable {


     private String id;
     private String type;
     private String keyInfo;
     private String value;
     private int sortOrder;

    public Keyvaluemaster() {
    }

    public Keyvaluemaster(String id, String type, String keyInfo, String value, int sortOrder) {
       this.id = id;
       this.type = type;
       this.keyInfo = keyInfo;
       this.value = value;
       this.sortOrder = sortOrder;
    }
   
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public String getKeyInfo() {
        return this.keyInfo;
    }
    
    public void setKeyInfo(String keyInfo) {
        this.keyInfo = keyInfo;
    }
    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    public int getSortOrder() {
        return this.sortOrder;
    }
    
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }




}


