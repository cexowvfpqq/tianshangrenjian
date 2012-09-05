package com.sttm.util;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import com.android.calendar.R;

public class HttpUtil {

	public static HttpClient getHttpClent() {
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

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
			result = "ClientProtocolException";
			return result;

		} catch (IOException e) {
			result = "IOException";
			return result;

		}
		return null;
	}

	



	
	
	
	
	
	
	
	
	

	
	

	


	

}
