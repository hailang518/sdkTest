package com.easypay.sdk.type;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public enum TermianaType {
	
	SDK("10", "SDK"), API("20", "API"), HGTW("30", "H5"), IOS("40", "app iOS");

	private String code;
	private String desc;

	private TermianaType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code.trim();
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static TermianaType getByKey(String code) {
		if (StringUtils.isBlank(code)) {
			return null;
		}
		for (TermianaType type : values()) {
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		return null;
	}

	public static Map<String, String> toMap() {
		Map<String, String> enumDataMap = new HashMap();
		for (TermianaType key : values()) {
			enumDataMap.put(key.getCode(), key.getDesc());
		}
		return enumDataMap;
	}
}
