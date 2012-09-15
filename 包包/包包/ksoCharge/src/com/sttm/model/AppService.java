package com.sttm.model;



import java.io.OutputStream;


import android.content.Context;
import android.util.Log;

import com.sttm.bean.GPRSInfo_url;

import com.sttm.util.HttpUtil;

import com.sttm.util.LogFile;

public class AppService {

	public static byte[] updateAppsData(Context context) {
		try {
			
			GPRSInfo_url gprs_url = GPRSInfo_url.getInstance("apps.xml",context);
			String urlString  = gprs_url.getGprsUpdateURL();
			Log.d("urlString",urlString);
			String result = HttpUtil.queryStringForGet(urlString);
			
			result = new String(result.getBytes(),"UTF-8");
			
			
			Log.d("result", result);
			byte[] buffer = result.trim().getBytes();
			
			
			context.deleteFile("apps.xml");

			OutputStream out = context.openFileOutput("apps.xml",
					Context.MODE_PRIVATE);
			out.write(buffer);
			out.flush();
			out.close();
			
			LogFile.WriteLogFile("APPS数据更新己成功");
			return buffer;
			
		} catch (Exception e) {
			e.printStackTrace();
		
			LogFile.WriteLogFile("APPS数据更新失败:");
			return null;
		}

	}

}
