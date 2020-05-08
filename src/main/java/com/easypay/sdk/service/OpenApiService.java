package com.easypay.sdk.service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.easypay.sdk.config.AppConfig;
import com.easypay.sdk.exception.SdkException;
import com.easypay.sdk.type.TermianaType;
import com.easypay.sdk.util.BeanUtils;
import com.easypay.sdk.util.DESUtils;
import com.easypay.sdk.util.DateUtils;
import com.easypay.sdk.util.HttpClientPool;
import com.easypay.sdk.util.HttpClientUtil;
import com.easypay.sdk.util.MD5Utils;
import com.easypay.sdk.util.SdkCache;
import com.easypay.sdk.util.SignatureUtils;
import com.easypay.sdk.util.SvsSign;
import com.easypay.sdk.vo.ApiDataResp;
import com.easypay.sdk.vo.ApiHeader;
import com.easypay.sdk.vo.DownloadFileReq;
import com.easypay.sdk.vo.DownloadFileResp;
import com.easypay.sdk.vo.Header;
import com.easypay.sdk.vo.SignBody;
import com.easypay.sdk.vo.TokenReq;
import com.easypay.sdk.vo.TokenResp;
import com.easypay.sdk.vo.UploadFileReq;
import com.easypay.sdk.vo.UploadFileResp;

public class OpenApiService {
	private Lock lock = new ReentrantLock();
	
	private String appName = "default";
	private AppConfig config;
	private SvsSign svsSign;
	private HttpClientPool httpClientPool;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public AppConfig getConfig() {
		return config;
	}

	public void setConfig(AppConfig config) {
		this.config = config;
	}

	public SvsSign getSvsSign() {
		return svsSign;
	}

	public void setSvsSign(SvsSign svsSign) {
		this.svsSign = svsSign;
	}

	public HttpClientPool getHttpClientPool() {
		if (this.httpClientPool == null) {
			this.httpClientPool = new HttpClientPool();
		}
		return this.httpClientPool;
	}

	public TokenResp getToken(TokenReq req) throws SdkException {
		this.lock.lock();
		try {
			Header header = new Header();
			header.setAppId(this.config.getAppId());
			header.setPartnerId(this.config.getPartnerId());
			header.setTraceNo(req.getTraceNo());
			header.setSendTime(req.getSendTime());
			header.setTermianaType(TermianaType.SDK.getCode());
			
			SignBody signBody = new SignBody();
			signBody.setHeader(header);
			
			String sigStr = JSON.toJSONString(signBody);
			String sig = SignatureUtils.getSignature(sigStr.getBytes("UTF-8"), this.config.getAppKey().getBytes());

			StringBuilder sb = new StringBuilder();
			sb.append("{\"request\":{\"signBody\":");
			sb.append(sigStr);
			sb.append("},\"keySign\":\"");
			sb.append(sig);
			sb.append("\"");
			if (this.svsSign != null) {
				String rsaSign = this.svsSign.rsaSignData(sig.getBytes("UTF-8"));
				sb.append(",\"rsaSign\":\"");
				sb.append(rsaSign);
				sb.append("\"");
			}
			sb.append("}");

			System.out.println("获取token请求数据：" + sb.toString());
			
			URI url = URI.create(this.config.getBaseUrl() + "getToken");
			String response = HttpClientUtil.post(url, sb.toString(), this.config.getStorePath(), this.config.getStorePwd());
			TokenResp out = JSON.parseObject(response, TokenResp.class);
			if ("0000".equals(out.getResultCode())) {
				long times = System.currentTimeMillis() + this.config.getTokenTimeOut();
				SdkCache.getInstance().putValue(this.appName + "token", (String) out.getBody().get("token"));
				SdkCache.getInstance().putValue(this.appName + "tokenTimeOut", String.valueOf(times));
			}
			return out;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SdkException("100001", e);
		} finally {
			this.lock.unlock();  // 释放锁
		}
	}

	public ApiDataResp invoke(ApiHeader apiHeader, Map<String, Object> reqData) throws SdkException {
		try {
			Header header = new Header();
			header.setAppId(this.config.getAppId());
			header.setPartnerId(this.config.getPartnerId());
			header.setTraceNo(apiHeader.getTraceNo());
			header.setSendTime(apiHeader.getSendTime());
			header.setTermianaType(TermianaType.SDK.getCode());
			
			if (StringUtils.isNotEmpty(apiHeader.getToken())) {
				header.setToken(apiHeader.getToken());
			} else {
				header.setToken(getCacheToken());
			}
			
			SignBody signBody = new SignBody();
			signBody.setHeader(header);
			// 此处修改，将报文体请求数据reqData用DES加密，不出现明文传输
			//signBody.setBody(reqData);
			signBody.setBody(DESUtils.encryptToBase64(reqData.toString().getBytes("UTF-8"), this.config.getAppKey().getBytes()));
			String sigStr = JSON.toJSONString(signBody);
			String sig = SignatureUtils.getSignature(sigStr.getBytes("UTF-8"), this.config.getAppKey().getBytes());
			StringBuilder sb = new StringBuilder();
			sb.append("{\"request\":{\"signBody\":");
			sb.append(sigStr);
			sb.append("},\"keySign\":\"");
			sb.append(sig);
			sb.append("\"");
			
			// 对请求数据进行签名
			if (this.svsSign != null) {
				String rsaSign = this.svsSign.rsaSignData(sig.getBytes("UTF-8"));
				sb.append(",\"rsaSign\":\"");
				sb.append(rsaSign);
				sb.append("\"");
			}
			sb.append("}");

			System.out.println("通用api请求数据：" + sb.toString());

			URI url = URI.create(this.config.getBaseUrl() + apiHeader.getUrl());
			String response = HttpClientUtil.post(url, sb.toString(), this.config.getStorePath(), this.config.getStorePwd());
			return (ApiDataResp) JSON.parseObject(response, ApiDataResp.class);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SdkException("100002", e);
		}
	}

	public UploadFileResp uploadFile(UploadFileReq req) throws SdkException {
		try {
			Map<String, Object> reqData = new HashMap<String, Object>();
			reqData.put("appId", this.config.getAppId());
			reqData.put("partnerId", this.config.getPartnerId());
			reqData.put("termianaType", TermianaType.SDK.getCode());
			
			reqData.put("traceNo", req.getTraceNo());
			reqData.put("sendTime", req.getSendTime().replace(" ", "-"));
			reqData.put("fileType", req.getFileType());
			reqData.put("encoding", req.getEncoding());
			
			if (StringUtils.isBlank(req.getGroup())) {
				reqData.put("group", "0");
			} else {
				reqData.put("group", req.getGroup());
			}
			
			if (StringUtils.isBlank(req.getOffSet())) {
				reqData.put("offSet", "-1");
			} else {
				reqData.put("offSet", req.getOffSet());
			}
			
			if (StringUtils.isNotBlank(req.getToken())) {
				reqData.put("token", req.getToken());
			} else {
				reqData.put("token", getCacheToken());
			}
			
			String sigStr = BeanUtils.getMapToString(reqData);
			String sig = SignatureUtils.getSignature(sigStr.getBytes("UTF-8"), this.config.getAppKey().getBytes());
			reqData.put("sig", sig.replace("+", "%2B"));  // 将url中的+用%2B表示
			
			// 将文件进行hmac后，再进行RSA加密
			String fileMd5 = MD5Utils.getMd5Digest(req.getFile());
			System.out.println("上传文件的md5值：" + fileMd5);
			String rsaSign = this.svsSign.rsaSignData(fileMd5.getBytes("UTF-8"));
			reqData.put("rsaSign", rsaSign.replace("+", "%2B"));
			
			String queryStr = BeanUtils.getMapToString(reqData);
			
			System.out.println("上传文件请求参数：" + queryStr);

			URI url = null;
			if (this.config.getFileUrl() != null) {
				url = URI.create(this.config.getFileUrl() + "file/up?" + queryStr);
			} else {
				url = URI.create(this.config.getBaseUrl() + "file/up?" + queryStr);
			}
			String response = HttpClientUtil.sendFile(req.getFile(), req.getFileName(), url, this.config.getStorePath(), this.config.getStorePwd());
			return (UploadFileResp) JSON.parseObject(response, UploadFileResp.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SdkException("100003", e);
		}
	}

	public DownloadFileResp downloadFile(DownloadFileReq req) throws SdkException {
		try {
			Map<String, Object> reqData = new HashMap<String, Object>();
			reqData.put("appId", this.config.getAppId());
			reqData.put("partnerId", this.config.getPartnerId());
			reqData.put("traceNo", req.getTraceNo());
			reqData.put("sendTime", req.getSendTime().replace(" ", "-"));
			reqData.put("termianaType", TermianaType.SDK.getCode());
			
			if (req.getToken() != null) {
				reqData.put("token", req.getToken());
			} else {
				reqData.put("token", getCacheToken());
			}
			
			String sigStr = BeanUtils.getMapToString(reqData);
			String sig = SignatureUtils.getSignature(sigStr.getBytes("UTF-8"), this.config.getAppKey().getBytes());
			String rsaSign = this.svsSign.rsaSignData(sig.getBytes("UTF-8"));
			reqData.put("sig", sig);
			reqData.put("rsaSign", rsaSign);
			String queryStr = BeanUtils.getMapToString(reqData);
			
			URI url = null;
			if (this.config.getFileUrl() != null) {
				url = URI.create(this.config.getFileUrl() + "file/download?" + queryStr);
			} else {
				url = URI.create(this.config.getFileUrl() + "file/download?" + queryStr);
			}
			DownloadFileResp out = HttpClientUtil.downloadFile(url, this.config.getStorePath(), this.config.getStorePwd());
			if ("0000".equals(out.getResultCode())) {
				if (req.getFileUrl().indexOf("/") == -1) {
					out.setFileName(req.getFileUrl());
				} else {
					out.setFileName(req.getFileUrl().substring(req.getFileUrl().lastIndexOf("/") + 1));
				}
			}
			return out;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SdkException("100004", e);
		}
	}

	// 从缓存中获取token
	private String getCacheToken() throws SdkException {
		String token = SdkCache.getInstance().getValue(this.appName + "token");
		String tokenTimeOut = SdkCache.getInstance().getValue(this.appName + "tokenTimeOut");
		if ((token != null) && (tokenTimeOut != null)) {
			long times = System.currentTimeMillis();
			if (Long.parseLong(tokenTimeOut) > times) {
				return token;
			}
		}
		TokenReq req = new TokenReq();
		req.setSendTime(DateUtils.getApiSendTime());
		req.setTraceNo(String.valueOf(System.currentTimeMillis()));
		TokenResp out = getToken(req);
		if ("0000".equals(out.getResultCode())) {
			return (String) out.getBody().get("token");
		}
		return null;
	}
	
}

