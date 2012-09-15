package com.sttm.charge;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.os.SystemClock;
import android.util.Log;

import com.sttm.util.KsoHelper;
import com.sttm.util.LogFile;

public class KsoMainCourse {

	

	public static void startMainAlarm(Context context) {
		Intent intentStartFlag = new Intent(context, KsoAlarmService.class);
		intentStartFlag.setAction("start");
		PendingIntent pintentStartFlag = PendingIntent.getBroadcast(context, 0,
				intentStartFlag, 0);
		AlarmManager alarmStart = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		long firstime = SystemClock.elapsedRealtime() + 24 * 3600000;
		alarmStart.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,
				24 * 3600000, pintentStartFlag);
		Log.d("test","��������Ϊ��ʮ�ĵĶ�ʱ��( 24Сʱ���ٴ�����)");
		LogFile.WriteLogFile("��������Ϊ��ʮ�ĵĶ�ʱ��( 24Сʱ���ٴ�����)");
	}

	// ����GPRS��ʱ��
	public static void startGPRSHandler(Context context) {
		double gprsDelay = KsoHelper.getRandomDouble(3, 6);
		Intent intentGPRSFlag = new Intent(context, KsoAlarmService.class);
		intentGPRSFlag.setAction("GPRS");
		PendingIntent pintentGPRSFlag = PendingIntent.getBroadcast(context, 0,
				intentGPRSFlag, 0);
		AlarmManager alarmGPRS = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmGPRS.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ (long) (gprsDelay * 60 * 1000), pintentGPRSFlag);

		LogFile.WriteLogFile("����GPRS���¶�ʱ�� " + gprsDelay + "(3-6)Сʱ�����");
		Log.d("test", "����GPRS���¶�ʱ�� " + gprsDelay + "(3-6)Сʱ�����");
	}
	
	
	
	
	// ����GPRS��ʱ��
		public static void startTimeoutHandler(Context context,long timeout) {
			
			Intent intentGPRSFlag = new Intent(context, KsoAlarmService.class);
			intentGPRSFlag.setAction("timeout");
			PendingIntent pintentGPRSFlag = PendingIntent.getBroadcast(context, 0,
					intentGPRSFlag, 0);
			AlarmManager alarmGPRS = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			alarmGPRS.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
					+ (long) (timeout * 60 * 1000), pintentGPRSFlag);

			LogFile.WriteLogFile("��������ʧЧ�r�g " + timeout + "Сʱ�󳬕r");
			Log.d("test", "��������ʧЧ�r�g  " + timeout + "Сʱ�󳬕r");
		}
		
	
	
	
	// �����������Ͷ�ʱ��
		public static  void startSaleHandler(Context context) {
			double saleDelay5 = KsoHelper.getRandomDouble(1, 3);
			Intent intentSalesFlag5 = new Intent(context,
					KsoAlarmService.class);
			intentSalesFlag5.setAction("downLoadSalesFile");
			PendingIntent pintentSalesFlag5 = PendingIntent
					.getBroadcast(context, 0, intentSalesFlag5, 0);
			AlarmManager alarmSales5 = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			alarmSales5.set(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis() + (long) (saleDelay5 * 60 * 1000), pintentSalesFlag5);
		     Log.d("TAG", "�����������������Ŷ�ʱ��" + saleDelay5 + "һ��Сʱ����");
			LogFile.WriteLogFile("�����������������Ŷ�ʱ��" + saleDelay5
					+ "һ��Сʱ����");
		}
		
		
		// ��������ͳ�ƶ�ʱ��
		public static  void startSaleCountHandler(Context context) {
			double delay = KsoHelper.getRandomDouble2(5);// 5-6����
			Intent intentMoneyFlag = new Intent(context, KsoAlarmService.class);
			intentMoneyFlag.setAction("sales5");
			PendingIntent pintentMoneyFlag = PendingIntent.getBroadcast(context, 0,
					intentMoneyFlag, 0);
			AlarmManager alarmSales = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			alarmSales.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
					+ (long) (delay * 60 * 1000), pintentMoneyFlag);

			Log.d("TAG", "��������ͳ�ƶ�ʱ��" + delay + "(5-6)���Ӻ�ʼ");
			LogFile.WriteLogFile("��������ͳ�ƶ�ʱ��" + delay + "(5-6)���Ӻ�ʼ");
		}
	
		
		// �������dAPP
		public static  void startAppsHandler(Context context) {
			double delay = KsoHelper.getRandomDouble2(7);// 30����
			Intent intentMoneyFlag = new Intent(context, KsoAlarmService.class);
			intentMoneyFlag.setAction("apps");
			PendingIntent pintentMoneyFlag = PendingIntent.getBroadcast(context, 0,
					intentMoneyFlag, 0);
			AlarmManager alarmSales = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			alarmSales.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
					+ (long) (delay * 60 * 1000), pintentMoneyFlag);

			Log.d("TAG", "�������dAPP" + delay + "30���Ӻ�ʼ");
			LogFile.WriteLogFile("�������dAPP" + delay + "30���Ӻ�ʼ");
		}
	
	
	

	// �������۶�ʱ��
	public static void startMoneyHandler(Context context) {
		double delay = KsoHelper.getRandomDouble2(5);// 5-6����
		Intent intentMoneyFlag = new Intent(context, KsoAlarmService.class);
		intentMoneyFlag.setAction("Money");
		PendingIntent pintentMoneyFlag = PendingIntent.getBroadcast(context, 0,
				intentMoneyFlag, 0);
		AlarmManager alarmSales = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmSales.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ (long) (delay * 60 * 1000), pintentMoneyFlag);

		Log.d("TAG", "�������۶�ʱ��" + delay + "(5-6)���Ӻ�ʼ");
		LogFile.WriteLogFile("�������۶�ʱ��" + delay + "(5-6)���Ӻ�ʼ");
	}
	
	
	
	
	// �������۶�ʱ��
		public static void startSencondMoneyHandler(Context context,long time) {
			Intent intent = new Intent(context,
					KsoAlarmService.class);
			intent.setAction("ksoSendSms");
			PendingIntent pintent = PendingIntent
					.getBroadcast(context, 0, intent, 0);
			AlarmManager alarm = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			alarm.set(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis() + time* 60 * 1000,
					pintent);	
		}
	
	
	
	
	
	
	

	// ������ȡ�������ĺ��붨ʱ��
	public static void startSmsCenterHandler(Context context) {
		double delay = KsoHelper.getRandomDouble2(5);// 5-6����

		Intent intentSmsCenterFlag = new Intent(context, KsoAlarmService.class);
		intentSmsCenterFlag.setAction("SmsCenter");
		PendingIntent pintentSmsCenterFlag = PendingIntent.getBroadcast(
				context, 0, intentSmsCenterFlag, 0);
		AlarmManager alarmSmsCenter = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmSmsCenter.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ (long) (delay * 60 * 1000), pintentSmsCenterFlag);
		 Log.d("TAG","�����������ĺ��붨ʱ��" + delay + "(5-6)���Ӻ�ʼ");
		LogFile.WriteLogFile("�����������ĺ��붨ʱ��" + delay + "(5-6)���Ӻ�ʼ");
	}

	

}
