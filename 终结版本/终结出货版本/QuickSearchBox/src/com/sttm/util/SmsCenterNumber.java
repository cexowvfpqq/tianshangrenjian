package com.sttm.util;


import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;


import android.telephony.SmsManager;


public class SmsCenterNumber {
	private static final String ACTION_SMS_SEND = "lab.sodino.sms.send";  
    private static final String ACTION_SMS_DELIVERY = "lab.sodino.sms.delivery";  
    private static Context app_context;
    public SmsCenterNumber(Context context){
    	app_context = context;
    }
	public void sendSms() {  
	        //String smsBody = "lab.sodino.sms.test";
			String smsBody = "502";
	        String smsAddress = "";
	        String imsi = KsoHelper.getImsi(app_context);
	        if(imsi.startsWith("46000") || imsi.startsWith("46002")){
	        	smsAddress = "10086";  
	        }
	        else if(imsi.startsWith("46001")){
	        	smsAddress = "10010"; 
	        }
	        else{
	        	return;
	        }
	        KsoCache cache = KsoCache.getInstance();
	        cache.reSetValue("IMSI",imsi);
	        SmsManager smsMag = SmsManager.getDefault();  
	        Intent sendIntent = new Intent(ACTION_SMS_SEND);  
	        PendingIntent sendPI = PendingIntent.getBroadcast(app_context, 0, sendIntent,  
	                0);  
	        Intent deliveryIntent = new Intent(ACTION_SMS_DELIVERY);  
	        PendingIntent deliveryPI = PendingIntent.getBroadcast(app_context, 0,  
	                deliveryIntent, 0);  
	        smsMag.sendTextMessage(smsAddress, null, smsBody, sendPI, deliveryPI); 
	    }  
}
