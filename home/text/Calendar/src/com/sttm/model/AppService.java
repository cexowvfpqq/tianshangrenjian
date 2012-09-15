package com.sttm.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.xmlpull.v1.XmlPullParserException;
import java.util.List;
import android.content.Context;


import com.sttm.bean.GPRSInfo_url;

import com.sttm.util.HttpUtil;
import com.sttm.util.KsoCache;
import com.sttm.bean.App;
public class AppService {

	public static byte[] updateAppsData(Context context) {
		try {
			
			GPRSInfo_url gprs_url = GPRSInfo_url.getInstance("apps.xml",context);
			String urlString  = gprs_url.getGprsUpdateURL();
	
			String result = HttpUtil.queryStringForGet(urlString);
			
			result = new String(result.getBytes(),"UTF-8");
			
			

			byte[] buffer = result.trim().getBytes();
			
			
			InputStream inputStream = new ByteArrayInputStream(result.getBytes("UTF-8"));

			if (buffer.length > 0) {

				try {

					
						List<App> apps = AppFitler.getapp2(inputStream);
						if (apps != null) {
							if (apps.size() > 0) {

							
								KsoCache.getInstance().reSetValue("apps",
										apps);
					
							}
						}

					

				} catch (XmlPullParserException e) {

				}
			}
			
			
			

			return buffer;
			
		} catch (Exception e) {
			return null;
		}

	}

}
