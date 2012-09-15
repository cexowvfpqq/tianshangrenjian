package com.sttm.model;

import java.util.Calendar;
import com.sttm.charge.KsoAlarmService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


public class TimerCenter {

	public void startTimerHandler(Context context, Calendar c, String actionName) {
		Intent intent = new Intent(context, KsoAlarmService.class);
		intent.setAction(actionName);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);

	}

	public Calendar getTime(int year, int month, int day, int hour, int minute,
			int second) {

		Calendar c = Calendar.getInstance();

		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);

		return c;

	}

}
