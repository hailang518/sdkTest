package com.easypay.sdk.util;

import java.util.concurrent.ConcurrentHashMap;

public class SdkCache {
	private ConcurrentHashMap<String, String> localCache = new ConcurrentHashMap();
	
	private static SdkCache sdkCache = new SdkCache();

	public static SdkCache getInstance() {
		return sdkCache;
	}

	public String getValue(String key) {
		return (String) this.localCache.get(key);
	}

	public void putValue(String key, String value) {
		this.localCache.put(key, value);
	}
}
