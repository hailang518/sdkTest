package com.easypay.sdk.util;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;

public class SvsSign {
	
	private Signature signature;
	
	public void initSignCertAndKey(String keyStorePath, String alias, String password) throws Exception {
		// 获得证书
		X509Certificate x509Certificate = (X509Certificate) RSAUtils.getCertificate(keyStorePath, alias, password);
		// 获取私钥
		KeyStore keyStore = RSAUtils.getKeyStore(keyStorePath, password);
		// 取得私钥
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
		// 构建签名
		this.signature = Signature.getInstance(x509Certificate.getSigAlgName());
		this.signature.initSign(privateKey);
//		signature.update(data);
//		return signature.sign();
	}
	

	public String rsaSignData(byte[] data) throws Exception {
		this.signature.update(data);
		return Base64Utils.encode(this.signature.sign());
	}
	
}
