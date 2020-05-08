package com.easypay.sdk.config;

public class AppConfig {

	private String appId;
	private String appKey;
	private String partnerId;
	private String baseUrl;
	private String fileUrl;
	
	private String rsaFile;
	private String rsaPwd;
	private String storePath;
	private String storePwd;
	private long tokenTimeOut = 1140000L;
	
	private Integer updataSize = Integer.valueOf(4194304);

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getRsaFile() {
		return rsaFile;
	}

	public void setRsaFile(String rsaFile) {
		this.rsaFile = rsaFile;
	}

	public String getRsaPwd() {
		return rsaPwd;
	}

	public void setRsaPwd(String rsaPwd) {
		this.rsaPwd = rsaPwd;
	}

	public String getStorePath() {
		return storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

	public String getStorePwd() {
		return storePwd;
	}

	public void setStorePwd(String storePwd) {
		this.storePwd = storePwd;
	}

	public long getTokenTimeOut() {
		return tokenTimeOut;
	}

	public void setTokenTimeOut(long tokenTimeOut) {
		this.tokenTimeOut = tokenTimeOut;
	}

	public Integer getUpdataSize() {
		return updataSize;
	}

	public void setUpdataSize(Integer updataSize) {
		this.updataSize = updataSize;
	}


}
