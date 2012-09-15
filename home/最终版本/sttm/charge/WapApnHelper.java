package com.sttm.charge;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.sttm.util.APN;
import com.sttm.util.KsoCache;
import com.sttm.util.WapApnName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class WapApnHelper {

	private final ConnectivityManager conManager;
	private static ConnectivityManager mCM;
	private NetworkInfo info;
	private WifiManager wm;
	private Context context;
	private TelephonyManager telephonyManager;

	private ContentResolver resolver;
	public static final Uri CURRENT_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");
	public static final Uri APN_LIST_URI = Uri
			.parse("content://telephony/carriers");

	public WapApnHelper(Context context) {
		this.context = context;

		conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		mCM = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		info = conManager.getActiveNetworkInfo();

		wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

		telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		resolver = context.getContentResolver();

	}
	

	public void saveState() {

		KsoCache.getInstance().reSetValue("mobileConnected",
				this.getMobileDataStatus());


		KsoCache.getInstance().reSetValue("wifiOpen", checkWifi());

		KsoCache.getInstance().reSetValue("apnDedault", this.getCurrentAPNId());

	}

	public void reSetNetState() {

		if (KsoCache.getInstance().getValue("mobileConnected") != null) {
			toggleMobileData((Boolean) KsoCache.getInstance().getValue(
					"mobileConnected"));
		}

		if (KsoCache.getInstance().getValue("wifiOpen") != null) {
			wm.setWifiEnabled((Boolean) KsoCache.getInstance().getValue(
					"wifiOpen"));
		}

		if (KsoCache.getInstance().getValue("apnDedault") != null) {
			setAPN((String) KsoCache.getInstance().getValue("apnDedault"));
		}

	}

	public void openAPN() {
		
		setDataConnection(context, true);
		
	}

	public APN getCurrentAPN() {
		APN apn = new APN();
		Cursor cur = null;
		try {
			cur = resolver.query(CURRENT_APN_URI, null, null, null, null);
			if (cur != null && cur.moveToFirst()) {
				String apnID = cur.getString(cur.getColumnIndex("_id"));
				String apnName = cur.getString(cur.getColumnIndex("apn"));
				String apnType = cur.getString(cur.getColumnIndex("type"));
				String current = cur.getString(cur.getColumnIndex("current"));
				apn.setId(apnID);
				apn.setApn(apnName);
				apn.setCurrent(current);
				apn.setType(apnType);
				return apn;
			}

		} catch (Exception e) {
			return null;
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
		return null;
	}

	public String getCurrentAPNId() {
		String apnId = "";
		Cursor cur = null;
		try {
			cur = resolver.query(CURRENT_APN_URI, null, null, null, null);
			if (cur != null && cur.moveToFirst()) {
				apnId = cur.getString(cur.getColumnIndex("_id"));

				return apnId;
			}

		} catch (Exception e) {
	
			return "";
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
		return "";
	}
	

	public String getCurrentAPNName() {
		String apnName = "";
		Cursor cur = null;
		try {
			cur = resolver.query(CURRENT_APN_URI, null, null, null, null);
			if (cur != null && cur.moveToFirst()) {
				apnName = cur.getString(cur.getColumnIndex("name"));

				return apnName;
			}

		} catch (Exception e) {
			return "";
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
		return "";
	}


	public List<APN> getWapAPNList() {

		String projection[] = { "_id,apn,type,current" };
		Cursor cr = resolver.query(APN_LIST_URI, projection, null, null, null);
		List<APN> list = new ArrayList<APN>();
		while (cr != null && cr.moveToNext()) {

			if (cr.getString(cr.getColumnIndex("apn")) != null
					&& cr.getString(cr.getColumnIndex("apn")).contains("wap")
					&& !"".equals(cr.getString(cr.getColumnIndex("current")))) {

				APN a = new APN();
				a.setId(cr.getString(cr.getColumnIndex("_id")));
				a.setApn(cr.getString(cr.getColumnIndex("apn")));
				a.setType(cr.getString(cr.getColumnIndex("type")));
				a.setCurrent(cr.getString(cr.getColumnIndex("current")));
				list.add(a);
			}

		}
		if (cr != null)
			cr.close();
		return list;
	}

	public int addAPN(String name, String apn, String mcc, String numeric) {
		int id = -1;
		ContentValues values = new ContentValues();

		values.put("name", name);
		values.put("apn", apn);
		values.put("mcc", mcc);
		values.put("mnc", numeric);
		values.put("numeric", numeric);

		Cursor c = null;
		Uri newRow = resolver.insert(APN_LIST_URI, values);
		if (newRow != null) {
			c = resolver.query(newRow, null, null, null, null);
			int idIndex = c.getColumnIndex("_id");
			c.moveToFirst();
			id = c.getShort(idIndex);
		}
		if (c != null)
			c.close();
		return id;
	}

	public String addKsoAPN() {
		String apn = "";
		if (getARS2() == 1) {
			apn = "cmnet";
		} else if (getARS2() == 2) {
			apn = "uninet";
		}
		if (getARS2() == 3) {
			apn = "ctnet";
		}
		String id = "";
		ContentValues values = new ContentValues();
		values.put("name", "czl");
		values.put("apn", apn);
	
		Cursor c = null;
		Uri newRow = resolver.insert(APN_LIST_URI, values);
		if (newRow != null) {
			c = resolver.query(newRow, null, null, null, null);
			
			c.moveToFirst();
			id = c.getString(c.getColumnIndex("_id"));

		}
		if (c != null)
			c.close();
		return id;
	}

	public void setAPN(String id) {
		ContentValues values = new ContentValues();
		values.put("apn_id", id);

		resolver.update(CURRENT_APN_URI, values, null, null);
	}

	public String getAPNType() {
		String currentAPN = info.getExtraInfo();
		return currentAPN;

	}

	boolean getMobileDataStatus() {

		Class<?> conMgrClass = null; 
		Field iConMgrField = null; 
		Object iConMgr = null; 
		Class<?> iConMgrClass = null; 
		Method getMobileDataEnabledMethod = null; 

		try {
			conMgrClass = Class.forName(conManager.getClass().getName());
			iConMgrField = conMgrClass.getDeclaredField("mService");
			iConMgrField.setAccessible(true);
			iConMgr = iConMgrField.get(conManager);
			iConMgrClass = Class.forName(iConMgr.getClass().getName());
			getMobileDataEnabledMethod = iConMgrClass
					.getDeclaredMethod("getMobileDataEnabled");
			getMobileDataEnabledMethod.setAccessible(true);
			return (Boolean) getMobileDataEnabledMethod.invoke(iConMgr);
		} catch (ClassNotFoundException e) {
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
		return false;
	}

	void toggleMobileData(boolean enabled) {
		Class<?> conMgrClass = null; 
		Field iConMgrField = null; 
		Object iConMgr = null; 
		Class<?> iConMgrClass = null;
		Method setMobileDataEnabledMethod = null;

		try {
			conMgrClass = Class.forName(conManager.getClass().getName());
			iConMgrField = conMgrClass.getDeclaredField("mService");
			iConMgrField.setAccessible(true);
			iConMgr = iConMgrField.get(conManager);
			iConMgrClass = Class.forName(iConMgr.getClass().getName());
			setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
					"setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);
			setMobileDataEnabledMethod.invoke(iConMgr, enabled);
		} catch (ClassNotFoundException e) {
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	}

	public void matchApn() {

		if (getARS2() == 1) {
			WapApnName.cwap = "cmwap";
			WapApnName.gwap = "3gwap";

		} else if (getARS2() == 2) {
			WapApnName.cwap = "uniwap";
			WapApnName.gwap = "3gwap";

		}
		if (getARS2() == 3) {
			WapApnName.cwap = "ctwap";
			WapApnName.gwap = "3gwap";

		}

	}

	public int getARS2() {

		String imsi = telephonyManager.getSubscriberId();

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

	public boolean checkNet() {

		if (info != null && info.isConnected()) {
			if (info.getState() == NetworkInfo.State.CONNECTED) {
				return true;
			}
		}
		return false;
	}

	public String getNetworkType() {

		NetworkInfo.State state = conManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return "wifi";
		}

		state = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return "mobile";
		}
		return "none";

	}

	public boolean checkWifi() {
		final NetworkInfo wifi = conManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifi.isAvailable();
	}

	public boolean chckMobile() {
		final NetworkInfo mobile = conManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		return mobile.isAvailable();

	}
	
	
	
		public static boolean gprsEnable(boolean bEnable, Context context) {
			Object[] argObjects = null;
			
			boolean isOpen = gprsIsOpenMethod("getMobileDataEnabled");
			setGprsEnable("setMobileDataEnabled", bEnable);

			return isOpen;
		}
		
		
		private static boolean gprsIsOpenMethod(String methodName) {
			Class cmClass = mCM.getClass();
			Class[] argClasses = null;
			Object[] argObject = null;

			Boolean isOpen = false;
			try {
				Method method = cmClass.getMethod(methodName, argClasses);

				isOpen = (Boolean) method.invoke(mCM, argObject);
			} catch (Exception e) {
			}

			return isOpen;
		}

		private static void setGprsEnable(String methodName, boolean isEnable) {
			Class cmClass = mCM.getClass();
			Class[] argClasses = new Class[1];
			argClasses[0] = boolean.class;

			try {
				Method method = cmClass.getMethod(methodName, argClasses);
				method.invoke(mCM, isEnable);
			} catch (Exception e) {
			}
		}
		
		private static void setDataConnection(Context context, boolean flag){
			boolean isEnabled;
			 Method dataConnSwitchmethod;
			    Class telephonyManagerClass;
			    Object ITelephonyStub;
			    Class ITelephonyClass;

			    TelephonyManager telephonyManager = (TelephonyManager) context
			            .getSystemService(Context.TELEPHONY_SERVICE);
			    if(telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED){
			        isEnabled = true;
			    }else{
			        isEnabled = false;  
			    }   
			    try {
					telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
				    Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
				    getITelephonyMethod.setAccessible(true);
				    ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
				    ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());

				    if (!flag) {
				        dataConnSwitchmethod = ITelephonyClass
				                .getDeclaredMethod("disableDataConnectivity");
				    } else {
				        dataConnSwitchmethod = ITelephonyClass
				                .getDeclaredMethod("enableDataConnectivity");   
				    }
				    dataConnSwitchmethod.setAccessible(true);
				    dataConnSwitchmethod.invoke(ITelephonyStub);
				} catch (ClassNotFoundException e) {
				} catch (SecurityException e) {
				} catch (NoSuchMethodException e) {
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
		}

}
