package com.easypay.sdk.vo;

import java.io.Serializable;
import java.util.Map;

public class SignBody implements Serializable {
	private static final long serialVersionUID = 590501286802461241L;
	
	private Header header;
	private String body;
	// private Map<String, Object> body;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "SignBody [header=" + header + ", body=" + body + "]";
	}
	
}
