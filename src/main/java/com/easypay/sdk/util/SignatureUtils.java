package com.easypay.sdk.util;

import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SignatureUtils {

	private static final String HmacSHA1 = "HmacSHA1";
	private static final String keyBase = "123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
	private static final int keyLenth = 32;

	// 消息认证码（带密钥的Hash函数），对于给定的data和key，签名后返回的字符串是相同的
	public static String getSignature(byte[] data, byte[] key) {
		try {
			SecretKey signingKey = new SecretKeySpec(key, HmacSHA1);
			Mac mac = Mac.getInstance(HmacSHA1);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data);
			return Base64Utils.encode(rawHmac).replace("\n", "").replace("\r", "");
		} catch (Exception e) {
		}
		return null;
	}

	public static String createKey() {
		SecureRandom r = new SecureRandom();
		StringBuffer keySb = new StringBuffer(32);
		for (int i = 0; i < keyLenth; i++) {
			int number = r.nextInt(keyBase.length());
			keySb.append(keyBase.charAt(number));
		}
		return keySb.toString();
	}
	
}
