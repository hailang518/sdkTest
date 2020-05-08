package com.easypay.sdk.vo;


public abstract class SdkReqVO {

	private String traceNo;
	private String sendTime;
	private String url = "";
	private String token = "";		// 非必填，用于主动传入token

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "SdkReqVO [traceNo=" + traceNo + ", sendTime=" + sendTime + ", url=" + url + ", token=" + token + "]";
	}

}
