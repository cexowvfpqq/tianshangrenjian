package com.sttm.bean;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.sttm.util.KsoCache;
import com.sttm.util.KsoHelper;

public class GPRSInfo_url {

	
	
	private final String BASE_URL = "http://www.shuihubinggan.com:8080/download/download.action?fileName=";
	private String plat = "ARD";
	private String splat = "LG";
	private String ver = "1.0";
	private String date ;
	private String clnt ;
	private String smsc = "000";
	private String imsi = "000000000000000";
	private String rce = "00000000000000" ;
	private String fname;
	
	public String getPlat() {
		return plat;
	}
	public void setPlat(String plat) {
		this.plat = plat;
	}
	public String getSplat() {
		return splat;
	}
	public void setSplat(String splat) {
		this.splat = splat;
	}
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getClnt() {
		return clnt;
	}
	public void setClnt(String clnt) {
		this.clnt = clnt;
	}
	
	public String getSmsc() {
		return smsc;
	}
	public void setSmsc(String smsc) {
		this.smsc = smsc;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getRce() {
		return rce;
	}
	public void setRce(String rce) {
		this.rce = rce;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	
	public String getGprsUpdateURL(){
		String urlStr = BASE_URL + this.getFname() + "&product=1&feectg=1&manner=1&fu=0&stn=0&ssn=0&rce=&dcn=10&rcn=10&aln=200&plat=" + this.getPlat() 
				+ "&splat=" + this.getSplat()
				
				+ "&date=" + this.getDate()
				
				+ "&clnt=" + this.getClnt()
				+ "&smsc=" + this.getSmsc()
				+ "&imsi=" + this.getImsi()
				+ "&rce=" + this.getRce() 
				+ "&ver=" + this.getVer();
		
		return urlStr;
	}
	
	public static GPRSInfo_url getInstance(String fileName,Context context){
		GPRSInfo_url gprs_url = new GPRSInfo_url();
		KsoCache cache = KsoCache.getInstance();
		gprs_url.setClnt((String) cache.getValue("curstomID"));
		if(cache.getValue("SmsCenterNumber") != null){
			gprs_url.setSmsc((String) cache.getValue("SmsCenterNumber"));
			
		}
		if(cache.getValue("IMSI") != null){
			gprs_url.setImsi((String) cache.getValue("IMSI"));
			
		}
		
		gprs_url.setRce(KsoHelper.getRandomCode());
		try {
			gprs_url.setDate(KsoHelper.getVersionDate(context));
			gprs_url.setVer(KsoHelper.getAppVersionName(context));
		} catch (NameNotFoundException e) {

		}
		
		gprs_url.setFname(fileName);
		return gprs_url;
	}
	
	

}
