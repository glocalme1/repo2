package com.glocalme.service.dto;

import java.io.Serializable;
import java.util.Date;
import com.onewifi.util.DateSerializer;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class TrafficHistoryDto implements Serializable{
	
	private static final long serialVersionUID=1L;
	
	private Integer billingId;
	private String userCode;
	private Date loginTime;
	private Date logoutTime;
	private String countryCode;
	private Long totalFlows;
	public Integer getBillingId() {
		return billingId;
	}
	public void setBillingId(Integer billingId) {
		this.billingId = billingId;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	
	public Date getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public Long getTotalFlows() {
		return totalFlows;
	}
	public void setTotalFlows(Long totalFlows) {
		this.totalFlows = totalFlows;
	}
}
