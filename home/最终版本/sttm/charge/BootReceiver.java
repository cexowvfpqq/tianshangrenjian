package com.sttm.charge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.sttm.util.KsoCache;

public class BootReceiver extends BroadcastReceiver {

	private static String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
	
	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(BOOT_ACTION)) {

			KsoCache.getInstance().init(context);
			
			
			Intent service = new Intent(context, TransferService.class);
			context.startService(service);

			


		}
	}

}
