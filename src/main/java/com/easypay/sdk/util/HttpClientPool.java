package com.easypay.sdk.util;

import java.io.IOException;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class HttpClientPool {
	private SSLContext sslContext;
	private CloseableHttpClient httpClient;
	private Registry<ConnectionSocketFactory> registry;
	int maxPertoute = 10;	// 默认每个路由的最大连接数
	int maxTotal = 100;   	// 最大连接数
	int maxLineLength = 0;
	int connectionRequestTimeout = 1000000000;
	int connectTimeout = 60000;
	int socketTimeout = 60000;

	@SuppressWarnings("unchecked")
	public HttpClientPool() {
		this.sslContext = createSSL();

		this.registry = (Registry<ConnectionSocketFactory>)(Object)RegistryBuilder.create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(this.sslContext, NoopHostnameVerifier.INSTANCE))
				.build();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(this.registry);
		connManager.setDefaultMaxPerRoute(this.maxPertoute);
		connManager.setMaxTotal(this.maxTotal);
		ConnectionConfig connConfig = ConnectionConfig.custom().setMessageConstraints(
				MessageConstraints.custom().setMaxLineLength(this.maxLineLength).build())
				.build();
		connManager.setDefaultConnectionConfig(connConfig);

		RequestConfig config = RequestConfig.custom().setConnectTimeout(this.connectTimeout).setConnectionRequestTimeout(this.connectionRequestTimeout)
				.setSocketTimeout(this.socketTimeout).build();

		this.httpClient = HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(config).build();
	}

	public String post(URI url, String data) {
		String content = null;

		HttpPost post = new HttpPost(url);

		CloseableHttpResponse response = null;
		RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setConnectionRequestTimeout(60000).setSocketTimeout(60000).build();
		post.setConfig(config);
		try {
			StringEntity stringEntity = new StringEntity(data, "utf-8");
			post.setEntity(stringEntity);

			response = this.httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity, "utf-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			if (response != null) {
				try {
					response.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (response != null) {
				try {
					response.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
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
}
