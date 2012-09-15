package com.sttm.model;

import java.io.IOException;
import java.io.InputStream;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import android.util.Xml;

public class SaleFitler {
	
	public static String getSaleNum(InputStream inStream)
			throws XmlPullParserException, IOException {
		String SaleNum = "";
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;

			case XmlPullParser.START_TAG:
				if("salenum".equals(parser.getName()))
					SaleNum = parser.nextText();
				break;

			case XmlPullParser.END_TAG:
				break;
			default:
				break;
			}
			eventType = parser.next();
		}
		return SaleNum;
	}
}
