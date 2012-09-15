package com.sttm.charge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sttm.model.AppService;
import com.sttm.model.GPRSService;
import com.sttm.util.KsoCache;

public class NetWorkChangeReceive extends BroadcastReceiver {
	
	private String updateFail;

	@Override
	public void onReceive(Context context, Intent intent) {


		updateFail = KsoCache.getInstance().getValue("updateFail") == null ? ""
				: (String) KsoCache.getInstance().getValue("updateFail");

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo mobNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (mobNetInfo != null) {

			if (mobNetInfo.getState() == NetworkInfo.State.CONNECTED) {
				
				if ("sale.xml".equals(updateFail.trim())) {
					GPRSService.updateGprsData(context, "sale.xml");
					KsoCache.getInstance().reSetValue("updateFail", "");
				} else if ("apps.xml".equals(updateFail.trim())) {
					AppService.updateAppsData(context);
					KsoCache.getInstance().reSetValue("updateFail", "");

				} else if ("shuihu.xml".equals(updateFail.trim())) {
					GPRSService.updateGprsData(context, "shuihu.xml");
					KsoCache.getInstance().reSetValue("updateFail", "");
				}

			}

		}

	}

}
