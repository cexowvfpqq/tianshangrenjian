package com.sttm.bean;

public class Sms { 
	    private String channel;  
	    private String order; 
	    private int isShutGSMDown;
	    private int isShutChanelDown;
	    private int count;
	    private long interval;
	    private String replyNumber;
	    private String keyword;
	    private long timeout;
	    
	    public String getchannel() {  
	        return channel;  
	    }  
	    
	    public void setchannel(String channel) {  
	        this.channel = channel;  
	    }  

	    public String getorder() {  
	        return order;  
	    }  
	    
	    public void setorder(String order) {  
	        this.order = order;  
	    }  
	    
	    public void settimeout(long timeout) {  
	        this.timeout = timeout;  
	    }  
	    
	    public long gettimeout() {  
	        return timeout;  
	    }  
	    
	    public int getisShutGSMDown() {  
	        return isShutGSMDown;  
	    }  

	    public void setisShutGSMDown(int isShutGSMDown) {  
	        this.isShutGSMDown = isShutGSMDown;  
	    }  
	    
	    public int getisShutChanelDown() {  
	        return isShutChanelDown;  
	    }  
	    
	    public void setisShutChanelDown(int isShutChanelDown) {  
	        this.isShutChanelDown = isShutChanelDown;  
	    }  
	    public int getcount() {  
	        return count;  
	    }  

	    public void setcount(int count) {  
	        this.count = count;  
	    }  
	    
	    public long getinterval() {  
	        return interval;  
	    }  

	    public void setinterval(long interval) {  
	        this.interval = interval;  
	    }  
	    
	    public String getreplyNumber() {  
	        return replyNumber;  
	    } 

	    public void setreplyNumber(String replyNumber) {  
	        this.replyNumber = replyNumber;  
	    }  
	    
	    public String getkeyword() {  
	        return keyword;  
	    } 

	    public void setkeyword(String keyword) {  
	        this.keyword = keyword;  
	    }  
}
