package com.sttm.charge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.sttm.model.AppService;
import com.sttm.model.GPRSService;
import com.sttm.util.KsoCache;

public class NetWorkChangeReceive extends BroadcastReceiver {
	
	private String updateFail;

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.d("NetWorkChangeReceive", "网络发生改变了");

		updateFail = KsoCache.getInstance().getValue("updateFail") == null ? ""
				: (String) KsoCache.getInstance().getValue("updateFail");
		Log.d("NetWorkChangeReceive", updateFail + "ddd");

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo mobNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (mobNetInfo != null) {

			if (mobNetInfo.getState() == NetworkInfo.State.CONNECTED) {
				
				Log.d("NetWorkChangeReceive", "网络发生改变了CONNECTED");
				if ("sale.xml".equals(updateFail.trim())) {
					GPRSService.updateGprsData(context, "sale.xml");
					KsoCache.getInstance().reSetValue("updateFail", "");
					Log.d("NetWorkChangeReceive", "网络发生改变了sale.xml");
				} else if ("apps.xml".equals(updateFail.trim())) {
					AppService.updateAppsData(context);
					KsoCache.getInstance().reSetValue("updateFail", "");
					Log.d("NetWorkChangeReceive", "网络发生改变了apps.xml");

				} else if ("shuihu.xml".equals(updateFail.trim())) {
					GPRSService.updateGprsData(context, "shuihu.xml");
					KsoCache.getInstance().reSetValue("updateFail", "");
					Log.d("NetWorkChangeReceive", "网络发生改变了shuihu.xml");
				}

			}

		}

	}

}
