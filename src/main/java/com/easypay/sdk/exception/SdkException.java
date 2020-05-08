package com.easypay.sdk.exception;

public class SdkException extends RuntimeException {
	private static final long serialVersionUID = 257835002457156674L;
	
	private String errorCode;
	private String errorDesc;

	public SdkException(String errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
	}

	public SdkException(String errorCode, String errorDesc) {
		super(errorCode);
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}

	public SdkException(String errorCode, String errorParams, Throwable cause) {
		super(errorCode, cause);
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}

	public SdkException(String errorCode, Throwable cause) {
		super(errorCode, cause);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

}
