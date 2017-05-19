package com.kf.data.crawler.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTools {

	
	public static String timestamp2Date(String str_num) {  
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    if (str_num.length() == 13) {  
	        String date = sdf.format(new Date(toLong(str_num)));  
	        return date;  
	    } else {  
	        String date = sdf.format(new Date(toInt(str_num) * 1000L));  
	        return date;  
	    }  
	}  
	
	 public static long toLong(String obj) {  
	        try {  
	            return Long.parseLong(obj);  
	        } catch (Exception e) {  
	        }  
	        return 0;  
	    }  
	 
	 public static int toInt(Object obj) {  
	       if (obj == null)  
	           return 0;  
	       return toInt(obj.toString(), 0);  
	   }

	private static int toInt(String string, int i) {
		try{
			 return Integer.parseInt(string,i);  
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}  
}
