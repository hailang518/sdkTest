package com.easypay.sdk.vo;

import com.easypay.sdk.util.DateUtils;


public abstract class SdkRespVO {

	private String version = "1.0.0";
	private String sentTime = DateUtils.getApiSendTime();
	private String resultCode;
	private String resultDesc;
	//private String orgMsgType;
	//private String orgMsgId;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getSentTime() {
		return sentTime;
	}

	public void setSentTime(String sentTime) {
		this.sentTime = sentTime;
	}

	@Override
	public String toString() {
		return "version=" + version + ", sentTime=" + sentTime + ", resultCode=" + resultCode + ", resultDesc=" + resultDesc;
	}
	
}
