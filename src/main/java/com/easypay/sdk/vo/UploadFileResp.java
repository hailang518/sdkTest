package com.easypay.sdk.vo;

import java.io.Serializable;

public class UploadFileResp extends SdkRespVO implements Serializable {
	private static final long serialVersionUID = -1092907853766798168L;
	
	private String fileName = "";
	private String fileId = "";
	private String resourceId = "";
	private String resPath = "";
	private String resName = "";

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getResPath() {
		return resPath;
	}

	public void setResPath(String resPath) {
		this.resPath = resPath;
	}

	@Override
	public String toString() {
		return "UploadFileResp [" + super.toString() + ", fileName=" + fileName + ", fileId=" + fileId + ", resourceId=" + resourceId + ", resPath=" + resPath + ", resName=" + resName + "]";
	}

}
