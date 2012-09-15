package com.sttm.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sttm.bean.App;
import com.sttm.util.KsoHelper;
import com.sttm.util.RootHelper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Xml;

public class AppFitler {
	public static boolean checkApp(Context context, List<String> apps) {

		PackageManager mpm = context.getPackageManager();
		List<PackageInfo> installedList = mpm.getInstalledPackages(0);
		for (PackageInfo info : installedList) {
			if (apps.contains(info.packageName)) {
				if (RootHelper.hasRootAccess(context)) {
					return true;
				}
			}
		}
		return false;
	}

	public static List<String> getapp(InputStream inStream)
			throws XmlPullParserException, IOException {
		List<String> apps = null;
		String app = "";
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				apps = new ArrayList<String>();
				break;

			case XmlPullParser.START_TAG:
				app = parser.nextText();
				break;

			case XmlPullParser.END_TAG:
				apps.add(app);
				app = "";
				break;
			default:
				break;
			}
			eventType = parser.next();
		}
		return apps;
	}

	public static boolean checkApp2(Context context, List<App> apps) {
		PackageManager mpm = context.getPackageManager();
		List<PackageInfo> installedList = mpm.getInstalledPackages(0);
		for (PackageInfo info : installedList) {
			for (App app : apps) {
				if (app.getAppName().contains(info.packageName)) {

					if (app.getIsNeedRoot() == 1) {
						if (KsoHelper.RootCommand("chmod 777 "+context.getPackageCodePath())) {
							System.out.println("Return from ......rootHelper true");
							return true;
						}

					}else if(app.getIsNeedRoot() == 0){
						System.out.println("Return from ......"+app.getAppName());
						return true;
					}
				}
			}
		}
		return false;
	}

	public static List<App> getapp2(InputStream inStream)
			throws XmlPullParserException, IOException {
		List<App> apps = null;
		App app = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");

		int eventType = parser.getEventType();
		System.out.println("eventType : "+eventType);
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				apps = new ArrayList<App>();
				break;

			case XmlPullParser.START_TAG:
				if ("app".equals(parser.getName())) {
					app = new App();
				}

				if (app != null) {
					if ("appName".equals(parser.getName())) {
						app.setAppName(parser.nextText());
					}

					if ("isNeedRoot".equals(parser.getName())) {
						app.setIsNeedRoot(Integer.parseInt(parser.nextText()));
					}
				}
				break;

			case XmlPullParser.END_TAG:
				if ("app".equals(parser.getName())) {// 判断结束标签元素是否是book
					apps.add(app);
					app = null;
				}
				break;
			default:
				break;
			}
			eventType = parser.next();
		}
		return apps;
	}
}
