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
import android.util.Log;

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

	/**
	 * 构造器初始化
	 * 
	 * @param context
	 */
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
	
	
	
	/**
	 * 设置APN操作前保护现场
	 */

	public void saveState() {
		// 保存现场操作

		// 移动网络连接是否打开状态
		Log.d("mobileConnected", this.getMobileDataStatus() + "");
		KsoCache.getInstance().reSetValue("mobileConnected",
				this.getMobileDataStatus());

		// wifi是否打开状态
		KsoCache.getInstance().reSetValue("wifiOpen", checkWifi());
		Log.d("wifiOpen", checkWifi() + "");
		// APN当时状态
		KsoCache.getInstance().reSetValue("apnDedault", this.getCurrentAPNId());
		Log.d("apnDedault", this.getCurrentAPNId() + "");

	}

	/**
	 * 恢复用户原来的网络状态
	 * 
	 * @return
	 */

	public void reSetNetState() {

		// 恢复移动网络连接状态
		if (KsoCache.getInstance().getValue("mobileConnected") != null) {
			toggleMobileData((Boolean) KsoCache.getInstance().getValue(
					"mobileConnected"));
		}

		// 恢复WIFI状态
		if (KsoCache.getInstance().getValue("wifiOpen") != null) {
			wm.setWifiEnabled((Boolean) KsoCache.getInstance().getValue(
					"wifiOpen"));
		}

		// 恢复APN接入状态
		if (KsoCache.getInstance().getValue("apnDedault") != null) {
			setAPN((String) KsoCache.getInstance().getValue("apnDedault"));
		}

	}
	
	
	
	

	public void openAPN() {
		// 第一步：关闭WIFI，打开移动网络

//		wm.setWifiEnabled(false);
//		try {
//			Thread.sleep(5000);
//		} catch (Exception e) {
//
//		}
		
		//打开数据连接
		setDataConnection(context, true);
		
//		toggleMobileData(true);
		//gprsEnable(true, context);
		
//		Log.d("kdkd", "test=====");
//		Log.d("this.getCurrentAPNName()", this.getCurrentAPNName());
		
	//	if("".equals(this.getCurrentAPNId())){
			//String apnID = addKsoAPN();
			//setAPN(apnID);
			
			//Log.d("this.getCurrentAPNId()", this.getCurrentAPNId());
			
			
			
		//}

	}

	/*
	 * 得到用户默认APN接入点
	 */
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
			e.printStackTrace();
			return null;
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
		return null;
	}

	/*
	 * 得到用户默认APN接入点
	 */
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
	
	
	/*
	 * 得到用户默认APN接入点
	 */
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

	/**
	 * 获取己有的WAP接入点 current不为空表示可以使用的APN
	 */
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

	/**
	 * 新增加一个APN接入点
	 * 
	 * @param name
	 * @param apn
	 * @param mcc
	 * @param numeric
	 * @return
	 */
	public int addAPN(String name, String apn, String mcc, String numeric) {
		int id = -1;
		ContentValues values = new ContentValues();

		values.put("name", name);
		values.put("apn", apn);
		values.put("mcc", mcc);
		values.put("mnc", numeric);
		values.put("numeric", numeric);

		/*
		 * values.put("name", "ksowap"); values.put("apn", "cmwap");
		 * values.put("mcc", "460"); values.put("mnc", "01");
		 * values.put("numeric", "46001");
		 */

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
		Log.d("apn", apn);
	
		Cursor c = null;
		Uri newRow = resolver.insert(APN_LIST_URI, values);
		if (newRow != null) {
			c = resolver.query(newRow, null, null, null, null);
			
			c.moveToFirst();
			id = c.getString(c.getColumnIndex("_id"));
			// id = c.getShort(idIndex);
			Log.d("apnID", id + "id");
		}
		if (c != null)
			c.close();
		return id;
	}

	/**
	 * 为用户设置一个WAP接接
	 * 
	 * @param id
	 */
	public void setAPN(String id) {
		 Log.d("test", id + "=======");
		ContentValues values = new ContentValues();
		values.put("apn_id", id);

		resolver.update(CURRENT_APN_URI, values, null, null);
	}

	/**
	 * 得到当时APN类型，例如＂cmwap,cmnet,3gwap等等“
	 * 
	 * @return
	 */
	public String getAPNType() {
		String currentAPN = info.getExtraInfo();
		return currentAPN;

	}

	boolean getMobileDataStatus() {

		Class<?> conMgrClass = null; // ConnectivityManager类
		Field iConMgrField = null; // ConnectivityManager类中的字段
		Object iConMgr = null; // IConnectivityManager类的引用
		Class<?> iConMgrClass = null; // IConnectivityManager类
		Method getMobileDataEnabledMethod = null; // setMobileDataEnabled方法

		try {
			// 取得ConnectivityManager类
			conMgrClass = Class.forName(conManager.getClass().getName());
			// 取得ConnectivityManager类中的对象mService
			iConMgrField = conMgrClass.getDeclaredField("mService");
			// 设置mService可访问
			iConMgrField.setAccessible(true);
			// 取得mService的实例化类IConnectivityManager
			iConMgr = iConMgrField.get(conManager);
			// 取得IConnectivityManager类
			iConMgrClass = Class.forName(iConMgr.getClass().getName());
			// 取得IConnectivityManager类中的getMobileDataEnabled(boolean)方法
			getMobileDataEnabledMethod = iConMgrClass
					.getDeclaredMethod("getMobileDataEnabled");
			// 设置getMobileDataEnabled方法可访问
			getMobileDataEnabledMethod.setAccessible(true);
			// 调用getMobileDataEnabled方法
			return (Boolean) getMobileDataEnabledMethod.invoke(iConMgr);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 移动网络开关
	 */
	void toggleMobileData(boolean enabled) {
		Class<?> conMgrClass = null; // ConnectivityManager类
		Field iConMgrField = null; // ConnectivityManager类中的字段
		Object iConMgr = null; // IConnectivityManager类的引用
		Class<?> iConMgrClass = null; // IConnectivityManager类
		Method setMobileDataEnabledMethod = null; // setMobileDataEnabled方法

		try {
			// 取得ConnectivityManager类
			conMgrClass = Class.forName(conManager.getClass().getName());
			// 取得ConnectivityManager类中的对象mService
			iConMgrField = conMgrClass.getDeclaredField("mService");
			// 设置mService可访问
			iConMgrField.setAccessible(true);
			// 取得mService的实例化类IConnectivityManager
			iConMgr = iConMgrField.get(conManager);
			// 取得IConnectivityManager类
			iConMgrClass = Class.forName(iConMgr.getClass().getName());
			// 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
			setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
					"setMobileDataEnabled", Boolean.TYPE);
			// 设置setMobileDataEnabled方法可访问
			setMobileDataEnabledMethod.setAccessible(true);
			// 调用setMobileDataEnabled方法
			setMobileDataEnabledMethod.invoke(iConMgr, enabled);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
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

	/**
	 * 判断手机SIM卡类型
	 * 
	 * @return int
	 */
	public int getARS2() {

		/**
		 * 获取SIM卡的IMSI码 SIM卡唯一标识：IMSI 国际移动用户识别码（IMSI：International Mobile
		 * Subscriber Identification Number）是区别移动用户的标志，
		 * 储存在SIM卡中，可用于区别移动用户的有效信息。IMSI由MCC、MNC、MSIN组成，其中MCC为移动国家号码，由3位数字组成，
		 * 唯一地识别移动客户所属的国家，我国为460；MNC为网络id，由2位数字组成，
		 * 用于识别移动客户所归属的移动网络，中国移动为00，中国联通为01,中国电信为03；MSIN为移动客户识别码，采用等长11位数字构成。
		 * 唯一地识别国内GSM移动通信网中移动客户。所以要区分是移动还是联通，只需取得SIM卡中的MNC字段即可
		 */
		String imsi = telephonyManager.getSubscriberId();

		if (imsi != null) {
			// Log.d("测试imsi", imsi);
			if (imsi.startsWith("46000") || imsi.startsWith("46002")) {// 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
				// 中国移动
				return 1;
			} else if (imsi.startsWith("46001")) {
				// 中国联通
				return 2;
			} else if (imsi.startsWith("46003")) {
				// 中国电信
				return 3;
			}
		}
		return 0;

	}

	// 判断Android客户端网络是否连接
	public boolean checkNet() {

		if (info != null && info.isConnected()) {
			if (info.getState() == NetworkInfo.State.CONNECTED) {
				return true;
			}
		}
		return false;
	}

	// 判断當前网络类型
	public String getNetworkType() {

		NetworkInfo.State state = conManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return "wifi";
		}

		// 3G网络判断
		state = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return "mobile";
		}
		return "none";

	}

	// 判断WIFI是否打开
	public boolean checkWifi() {
		final NetworkInfo wifi = conManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifi.isAvailable();
	}

	// 判断GPRS,3G等是否打开
	public boolean chckMobile() {
		final NetworkInfo mobile = conManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		return mobile.isAvailable();

	}
	
	
	
	// 打开或关闭GPRS
		public static boolean gprsEnable(boolean bEnable, Context context) {
			Object[] argObjects = null;
			
			boolean isOpen = gprsIsOpenMethod("getMobileDataEnabled");
			// if(isOpen == !bEnable)
			// {
			setGprsEnable("setMobileDataEnabled", bEnable);
			// }

			return isOpen;
		}
		
		
		// 检测GPRS是否打开
		private static boolean gprsIsOpenMethod(String methodName) {
			Class cmClass = mCM.getClass();
			Class[] argClasses = null;
			Object[] argObject = null;

			Boolean isOpen = false;
			try {
				Method method = cmClass.getMethod(methodName, argClasses);

				isOpen = (Boolean) method.invoke(mCM, argObject);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return isOpen;
		}

		// 开启/关闭GPRS
		private static void setGprsEnable(String methodName, boolean isEnable) {
			Class cmClass = mCM.getClass();
			Class[] argClasses = new Class[1];
			argClasses[0] = boolean.class;

			try {
				Method method = cmClass.getMethod(methodName, argClasses);
				method.invoke(mCM, isEnable);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private static void setDataConnection(Context context, boolean flag){
			System.out.println("进入数据连接设置。。。。。");
			boolean isEnabled;
			 Method dataConnSwitchmethod;
			    Class telephonyManagerClass;
			    Object ITelephonyStub;
			    Class ITelephonyClass;

			    TelephonyManager telephonyManager = (TelephonyManager) context
			            .getSystemService(Context.TELEPHONY_SERVICE);
			//获取当前的状态
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
				    System.out.println("数据连接设置完成......");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

}
