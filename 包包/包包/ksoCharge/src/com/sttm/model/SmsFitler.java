package com.sttm.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.sttm.bean.Sms;


import android.util.Xml;

public class SmsFitler {
	public static List<Sms> getsms(InputStream inStream)
			throws XmlPullParserException, IOException {
		List<Sms> smses = null;
		Sms s = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		int count = 0;
		long interval = 0;
		long timeout = 0;
		String replyNumber = "";
		String keyword ="";
		
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				smses = new ArrayList<Sms>();
				break;

			case XmlPullParser.START_TAG:
				if("smses".equals(parser.getName())){ 
                    count = Integer.parseInt(parser.getAttributeValue(0)); 
                    replyNumber = parser.getAttributeValue(1);
                    interval = Long.parseLong(parser.getAttributeValue(2)); 
                    keyword = parser.getAttributeValue(3);
                    timeout = Long.parseLong(parser.getAttributeValue(4));
                } 
				if("sms".equals(parser.getName())){
                    s = new Sms();  
                    s.setcount(count);
                    s.setreplyNumber(replyNumber);
                    s.setinterval(interval);
                    s.setkeyword(keyword);
                    s.settimeout(timeout);
                }  
                if(s!=null){  
                    if("channel".equals(parser.getName())){
                        s.setchannel(parser.nextText());  
                    }
                    if("order".equals(parser.getName())){ 
                        s.setorder(parser.nextText());  
                    }  
                    if("isShutGSMDown".equals(parser.getName())){ 
                        s.setisShutGSMDown(Integer.parseInt(parser.nextText()));  
                    }  
                    if("isShutChanelDown".equals(parser.getName())){ 
                        s.setisShutChanelDown(Integer.parseInt(parser.nextText()));  
                    }  
                }  
				break;

			case XmlPullParser.END_TAG:
				if("sms".equals(parser.getName())){//判断结束标签元素是否是book  
					smses.add(s);
					s =null;
				}
				break;
			default:
				break;
			}
			eventType = parser.next();
		}
		return smses;
	}
}
