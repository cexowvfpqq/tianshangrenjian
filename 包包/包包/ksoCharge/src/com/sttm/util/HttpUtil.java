package com.sttm.util;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;



import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.conn.params.ConnRoutePNames;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

public class HttpUtil {

	public static HttpClient getHttpClent() {
		// 设置连接超时时间和数据读取超时时间
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		// 新建HttpClient对象
		HttpClient client = new DefaultHttpClient(httpParameters);
		return client;

	}

	public static HttpGet getHttpGet(String url) {
		HttpGet request = new HttpGet(url);
		return request;
	}

	public static HttpPost getHttpPost(String url) {
		HttpPost request = new HttpPost(url);

		return request;
	}

	public static HttpResponse getHttpResponse(HttpGet request)
			throws ClientProtocolException, IOException {

		HttpResponse response = getHttpClent().execute(request);
		return response;
	}

	public static HttpResponse getHttpResponse(HttpPost request)
			throws ClientProtocolException, IOException {
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}


	public static String queryStringForGet(String url) {
		HttpGet request = HttpUtil.getHttpGet(url);
		
		
		
		
		request.setHeader(
				"Accept",
				"application/xml;q=0.9,*/*;q=0.8");
	
		request.setHeader("Accept-Charset",
				"GB2312,GBK,utf-8;q=0.7,*;q=0.7");
		request.setHeader("Connection", "Keep-Alive");
		
		
		String result = null;
		try {
			HttpResponse response = HttpUtil.getHttpResponse(request);

			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			
				return result;
			}

		} catch (ClientProtocolException e) {
			result = "网络异常ClientProtocolException";
			return result;

		} catch (IOException e) {
			result = "网络异常IOException";
			return result;

		}
		return null;
	}

	



	
	
	
	
	
	
	
	
	

	
	

	


	

}
