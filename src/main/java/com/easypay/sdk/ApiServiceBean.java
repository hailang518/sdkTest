package com.easypay.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.easypay.sdk.config.AppConfig;
import com.easypay.sdk.service.OpenApiService;
import com.easypay.sdk.util.SvsSign;

public class ApiServiceBean {
	private Map<String, AppConfig> confs = new ConcurrentHashMap();
	private Map<String, SvsSign> svsSigns = new ConcurrentHashMap();

	private static OpenApiService service;

	public OpenApiService getService(String sdkConfigPath) {
		if (service != null) {
			return service;
		}
		
		service = new OpenApiService();
		service.setAppName("default");
		if (this.confs.size() <= 0) {
			initConfig(sdkConfigPath);
		}
		AppConfig conf = (AppConfig) this.confs.get(service.getAppName());
		SvsSign svsSign = (SvsSign) this.svsSigns.get(service.getAppName());

		service.setConfig(conf);
		service.setSvsSign(svsSign);
		return service;
	}

	public OpenApiService getService(String appName, String sdkConfigPath) {
		OpenApiService service = new OpenApiService();
		service.setAppName(appName);
		if (this.confs.size() <= 0) {
			initConfig(sdkConfigPath);
		}
		AppConfig conf = (AppConfig) this.confs.get(service.getAppName());
		SvsSign svsSign = (SvsSign) this.svsSigns.get(service.getAppName());

		service.setConfig(conf);
		service.setSvsSign(svsSign);
		return service;
	}

	public void initConfig(String sdkConfigPath) {
		try {
			Properties ps = new Properties();
			InputStream is = new FileInputStream(new File(sdkConfigPath));
			ps.load(is);
			is.close();

			AppConfig conf = new AppConfig();
			conf.setAppId(ps.getProperty("appId"));
			conf.setAppKey(ps.getProperty("appKey"));
			conf.setPartnerId(ps.getProperty("partnerId"));
			conf.setBaseUrl(ps.getProperty("baseUrl"));
			conf.setFileUrl(ps.getProperty("fileUrl"));

			conf.setRsaFile(ps.getProperty("rsaFile"));
			conf.setRsaPwd(ps.getProperty("rsaPwd"));
			conf.setStorePath(ps.getProperty("storePath"));
			conf.setStorePwd(ps.getProperty("storePwd"));
			// 默认十分钟，单位ms
			conf.setTokenTimeOut(600000L);
			String timeOut = ps.getProperty("tokenTimeOut");
			if ((timeOut != null) && (timeOut.trim() != "")) {
				conf.setTokenTimeOut(Long.parseLong(timeOut));
			}
			this.confs.put("default", conf);
			if (StringUtils.isNotBlank(conf.getRsaFile()) && StringUtils.isNotBlank(conf.getRsaPwd())) {
				SvsSign svsSign = new SvsSign();
				svsSign.initSignCertAndKey(conf.getRsaFile(), conf.getPartnerId(), conf.getRsaPwd());
				this.svsSigns.put("default", svsSign);
			}
			
			/*String appnames = ps.getProperty("app.names");
			if ((appnames != null) && (appnames.indexOf(',') > 1)) {
				String[] names = appnames.split(",");
				for (String name : names) {
					AppConfig appConf = new AppConfig();
					appConf.setAppid(ps.getProperty("app." + name + ".appid"));
					appConf.setOpenid(ps.getProperty("app." + name + ".openid"));
					appConf.setBaseurl(ps.getProperty("baseurl"));
					appConf.setFileurl(ps.getProperty("fileurl"));
					appConf.setHgtwurl(ps.getProperty("hgtwurl"));

					appConf.setAppkey(ps.getProperty("app." + name + ".appkey"));
					appConf.setCfcafile(ps.getProperty("app." + name + ".cfcafile"));
					appConf.setCfcapwd(ps.getProperty("app." + name + ".cfcapwd"));

					String apptimeout = ps.getProperty("app." + name + ".tokenTimeOut");
					if ((apptimeout != null) && (apptimeout.trim() != "")) {
						appConf.setTokenTimeOut(Long.parseLong(apptimeout));
					}
					appConf.setTokenTimeOut(1140000L);
					this.confs.put(name, appConf);
					if ((appConf.getCfcafile() != null) && (appConf.getCfcafile().trim() != "")) {
						SvsSign sig = new SvsSign();
						sig.initSignCertAndKey(conf.getCfcafile(), conf.getCfcapwd());
						this.svsSigs.put(name, sig);
					}
				}
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, AppConfig> getConfs() {
		return confs;
	}

	public void setConfs(Map<String, AppConfig> confs) {
		this.confs = confs;
	}

	public Map<String, SvsSign> getSvsSigns() {
		return svsSigns;
	}

	public void setSvsSigns(Map<String, SvsSign> svsSigns) {
		this.svsSigns = svsSigns;
	}

}
