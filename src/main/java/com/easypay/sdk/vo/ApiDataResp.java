package com.easypay.sdk.vo;

import java.io.Serializable;
import java.util.Map;

public class ApiDataResp extends SdkRespVO implements Serializable {
	private static final long serialVersionUID = 4635014495557496186L;
	
	private Map<String, String> body;
	
	public Map<String, String> getBody() {
		return body;
	}

	public void setBody(Map<String, String> body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "ApiDataResp [" + super.toString() + ", body=" + body + "]";
	}
	
}
