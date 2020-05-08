package com.easypay.sdk.vo;

import java.io.Serializable;

public class DownloadFileResp extends SdkRespVO implements Serializable {
	private static final long serialVersionUID = -1374924770363427895L;

	private String fileName;
	private byte[] file;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}
	
}
