package com.easypay.sdk.vo;

import java.io.Serializable;

public class Header implements Serializable {
	
	private String appId;
	private String partnerId;
	private String termianaType;
	private String traceNo;
	private String sendTime;
	private String token;
	private String output = "json";  // 输出报文类型; 默认json

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public String getTermianaType() {
		return termianaType;
	}

	public void setTermianaType(String termianaType) {
		this.termianaType = termianaType;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	
	
}
