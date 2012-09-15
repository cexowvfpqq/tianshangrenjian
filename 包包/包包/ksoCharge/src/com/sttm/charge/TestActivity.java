package com.sttm.charge;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.sttm.bean.App;
import com.sttm.bean.GPRSInfo_url;
import com.sttm.model.AppFitler;
import com.sttm.model.AppService;
import com.sttm.model.GPRSService;
import com.sttm.model.SaleFitler;
import com.sttm.util.HttpUtil;
import com.sttm.util.KsoHelper;
import com.sttm.util.RootHelper;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

public class TestActivity extends Activity {
	//private WifiManager wm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		PackageManager mpm = this.getPackageManager();
		List<PackageInfo> installedList = mpm.getInstalledPackages(0);
		for(int i=0; i<installedList.size();i++){
			System.out.println("第"+i+"个应用："+installedList.get(i).packageName);
		}
//		System.out.println("if has RootAccess :"+RootHelper.hasRootAccess(TestActivity.this));
		
		InputStream inStream;
		try {
			inStream = this.getAssets().open("apps.xml");
			List<App> apps = AppFitler.getapp2(inStream);
			
			for(App app : apps){
				Log.d("test", "appName :"+app.getAppName() +" IsNeedRoot :"+ app.getIsNeedRoot());
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		String apkRoot="chmod 777 "+this.getPackageCodePath();//
		boolean falg = KsoHelper.RootCommand(apkRoot);
		System.out.println("Is Get Root ："+ falg);
		
		
		
		
		
		
		
		
		/*WapApnHelper wh = new WapApnHelper(this);
		wh.saveState();
		wh.openAPN();
			GPRSService.updateGprsData(this,"shuihu.xml");
		
		wh.reSetNetState();*/
		
		
		
		//wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		
		//wm.setWifiEnabled(false);
		//
		
		//GPRSService.updateGprsData(this,"shuihu.xml");
		
		//AppService.updateAppsData(this);
		//GPRSService.updateGprsData(this, "shuihu.xml");
		
	
	/*	String aaa = "";
		GPRSInfo_url gprs_url = GPRSInfo_url.getInstance("sale.xml",this);
		String urlString  = gprs_url.getGprsUpdateURL();
		Log.d("urlString",urlString);
		String result = HttpUtil.queryStringForGet(urlString);
		try {
			InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
			aaa = SaleFitler.getSaleNum(is);
		} catch (UnsupportedEncodingException e) {
		
			
		} catch (XmlPullParserException e) {
			
		
		} catch (IOException e) {
		
			
		}
	    Log.d("test", aaa);
		Log.d("result",result);*/
		
		
	}
	

}
