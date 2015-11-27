package com.glocalme.service.dto;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.onewifi.util.DateSerializer;

public class AccountSummaryDto implements Serializable{
	
	private static final long serialVersionUID=1L;
	
	private Date latestLoginTime;
	private Date latestLogoutTime;
	private String latestPosition;
	private Date lastLoginTime;
	private Date lastLogoutTime;
	private String lastPosition;
	private double latestTopupAmount;
	private double consumedAmount;
	private double totalFlowInKB;
	public Date getLatestLoginTime() {
		return latestLoginTime;
	}
	public void setLatestLoginTime(Date latestLoginTime) {
		this.latestLoginTime = latestLoginTime;
	}
	@JsonSerialize(using=DateSerializer.class)
	public Date getLatestLogoutTime() {
		return latestLogoutTime;
	}
	public void setLatestLogoutTime(Date latestLogoutTime) {
		this.latestLogoutTime = latestLogoutTime;
	}
	public String getLatestPosition() {
		return latestPosition;
	}
	public void setLatestPosition(String latestPosition) {
		this.latestPosition = latestPosition;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	@JsonSerialize(using=DateSerializer.class)
	public Date getLastLogoutTime() {
		return lastLogoutTime;
	}
	public void setLastLogoutTime(Date lastLogoutTime) {
		this.lastLogoutTime = lastLogoutTime;
	}
	public String getLastPosition() {
		return lastPosition;
	}
	public void setLastPosition(String lastPosition) {
		this.lastPosition = lastPosition;
	}
	public double getLastTopupAmount() {
		return latestTopupAmount;
	}
	public void setLastTopupAmount(double lastTopupAmount) {
		this.latestTopupAmount = lastTopupAmount;
	}
	public double getConsumedAmount() {
		return consumedAmount;
	}
	public void setConsumedAmount(double consumedAmount) {
		this.consumedAmount = consumedAmount;
	}
	public double getTotalFlowInKB() {
		return totalFlowInKB;
	}
	public void setTotalFlowInKB(double totalFlowInKB) {
		this.totalFlowInKB = totalFlowInKB;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	

}
