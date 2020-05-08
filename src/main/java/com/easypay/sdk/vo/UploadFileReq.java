package com.easypay.sdk.vo;

import java.io.Serializable;
import java.util.Arrays;

public class UploadFileReq extends SdkReqVO implements Serializable {
	private static final long serialVersionUID = 389276071199603207L;

	private String fileType = "txt";
	private String encoding = "utf-8";
	private String fileName;
	private byte[] file;
	private String offSet;
	private String group;

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOffSet() {
		return offSet;
	}

	public void setOffSet(String offSet) {
		this.offSet = offSet;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "UploadFileReq [" + super.toString() + ", fileType=" + fileType + ", encoding=" + encoding + ", fileName=" + fileName + ", file=" + Arrays.toString(file) + ", offSet=" + offSet + ", group=" + group + "]";
	}

}
