package com.sttm.charge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.util.Log;

import com.sttm.util.KsoCache;

import com.sttm.util.LogFile;

public class BootReceiver extends BroadcastReceiver {

	private static String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("driveup", "XXXXXXXXXXXXXXXXXXXX");
		LogFile.WriteLogFile("启动了driveup");

		if (intent.getAction().equals(BOOT_ACTION)) {

			KsoCache.getInstance().init(context);
			
			
			
			
			
			
			
			
			
			// ---------------------------------------------
			// 启动主服务程序
			Log.d("TAG", "开机启动主服务");
			LogFile.WriteLogFile("开机启动主服务");
			Intent service = new Intent(context, MainService.class);
			context.startService(service);
			// ---------------------------------------------

			


		}
	}

}
