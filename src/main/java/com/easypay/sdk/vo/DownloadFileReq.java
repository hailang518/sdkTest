package com.easypay.sdk.vo;

import java.io.Serializable;

public class DownloadFileReq extends SdkReqVO implements Serializable {
	private static final long serialVersionUID = 4826524834258929505L;
	
	private String fileUrl = "";

	public String getFileUrl() {
		return fileUrl;
	}
	
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	@Override
	public String toString() {
		return "DownloadFileReq [" + super.toString() + "fileUrl=" + fileUrl + "]";
	}

	

	

}
