package com.easypay.sdk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.easypay.sdk.vo.DownloadFileResp;

public class HttpClientUtil {

	@SuppressWarnings("unchecked")
	public static CloseableHttpClient getCloseableHttpClient(String storePath, String storePwd){
		int connectTimeout = 10000;
		int connectionRequestTimeout = 600000;
		int socketTimeout = 600000;
		
		SSLContext sslContext = null;
		if ((StringUtils.isNotBlank(storePwd)) && (StringUtils.isNotBlank(storePath))) {
			sslContext = createSSL(storePath, storePwd);
		} else {
			sslContext = createSSL();
		}
		Registry<ConnectionSocketFactory> socketFactoryRegistry = (Registry<ConnectionSocketFactory>)(Object)RegistryBuilder.create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
				.build();

		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		ConnectionConfig connConfig = ConnectionConfig.custom().build();
		connManager.setDefaultConnectionConfig(connConfig);

		RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectionRequestTimeout).setSocketTimeout(socketTimeout).build();
		
		CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(config).build();
		
		return client;
	}
	
	public static String sendMassage(String message, String url, String storePath, String storePwd) {
		String content = null;
//		SSLContext sslContext = null;
//		if ((StringUtils.isNotBlank(storePwd)) && (StringUtils.isNotBlank(storePath))) {
//			sslContext = createSSL(storePath, storePwd);
//		} else {
//			sslContext = createSSL();
//		}
//		Registry<ConnectionSocketFactory> socketFactoryRegistry = (Registry<ConnectionSocketFactory>)(Object)RegistryBuilder.create()
//				.register("http", PlainConnectionSocketFactory.INSTANCE)
//				.register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
//				.build();
//
//		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
//
//		HttpClients.custom().setConnectionManager(connManager);
//
//		CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();

		CloseableHttpClient client = HttpClientUtil.getCloseableHttpClient(storePath, storePwd);
		
		HttpPost post = new HttpPost(url);
//		RequestConfig config = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(60000).setSocketTimeout(60000).build();
		CloseableHttpResponse response = null;
		try {
			StringEntity stringEntity = new StringEntity(message, "utf-8");

//			post.setConfig(config);
			post.setEntity(stringEntity);
			response = client.execute(post);
			HttpEntity entity = response.getEntity();

			content = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content;
	}

	public static String sendFile(byte[] file, String filename, URI url, String storePath, String storePwd) {
		String content = null;
		CloseableHttpClient client = HttpClientUtil.getCloseableHttpClient(storePath, storePwd);
		
		HttpPost post = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, filename);
			HttpEntity req = builder.build();
			
			post.setEntity(req);
			response = client.execute(post);
			HttpEntity entity = response.getEntity();

			content = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content;
	}
	
	public static DownloadFileResp downloadFile(URI url, String storePath, String storePwd) {
		DownloadFileResp out = null;
		CloseableHttpClient client = HttpClientUtil.getCloseableHttpClient(storePath, storePwd);

		HttpGet get = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity.getContentType().getValue().toString().equals(ContentType.APPLICATION_OCTET_STREAM.getMimeType())) {
				out = new DownloadFileResp();
				out.setResultCode("0000");
				out.setResultDesc("文件下载成功");
				out.setFile(IOUtils.toByteArray(entity.getContent()));
				EntityUtils.consume(entity);
			} else {
				String json = EntityUtils.toString(entity, "utf-8");
				EntityUtils.consume(entity);
				out = (DownloadFileResp) JSON.parseObject(json, DownloadFileResp.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return out;
	}

	public static String post(URI url, String data, String storePath, String storePwd) {
		String content = null;
		CloseableHttpClient client = HttpClientUtil.getCloseableHttpClient(storePath, storePwd);

		HttpPost post = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {
			StringEntity stringEntity = new StringEntity(data, "utf-8");
			post.setEntity(stringEntity);
			response = client.execute(post);
			HttpEntity entity = response.getEntity();

			content = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);  // 消费entity
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content;
	}

	public static String postJson(URI url, String data, String storePath, String storePwd) {
		String content = null;
		CloseableHttpClient client = HttpClientUtil.getCloseableHttpClient(storePath, storePwd);
		
		HttpPost post = new HttpPost(url);
		RequestConfig config = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(60000).setSocketTimeout(60000).build();
		CloseableHttpResponse response = null;
		try {
			StringEntity stringEntity = new StringEntity(data, "utf-8");
			post.setConfig(config);
			post.setEntity(stringEntity);
			post.addHeader("Content-type", "application/json");
			response = client.execute(post);
			HttpEntity entity = response.getEntity();

			content = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content;
	}

	public static String get(URI url, String storePath, String storePwd) {
		String content = null;
		CloseableHttpClient client = HttpClientUtil.getCloseableHttpClient(storePath, storePwd);
		
		HttpGet get = new HttpGet(url);
		RequestConfig config = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(60000).setSocketTimeout(60000).build();
		CloseableHttpResponse response = null;
		try {
			get.setConfig(config);
			response = client.execute(get);
			HttpEntity entity = response.getEntity();

			content = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content;
	}
	
	
	public static SSLContext createSSL() {
		try {
			SSLContext sc = SSLContext.getInstance("SSLv3");
			X509TrustManager trustManager = new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}
			};
			sc.init(null, new TrustManager[] { trustManager }, null);
			return sc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SSLContext createSSL(String storePath, String storePwd) {
		SSLContext sc = null;
		FileInputStream ins = null;
		KeyStore trustStore = null;
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			ins = new FileInputStream(new File(storePath));
			trustStore.load(ins, storePwd.toCharArray());
			sc = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sc;
	}
}
