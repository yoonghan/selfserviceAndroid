package com.jaring.jom.selfservice.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public enum CalendarReaderUtil {

	INSTANCE;
	
	final static CalendarBean[] DEFAULT_CALBEAN = new CalendarBean[0];
	
	public static CalendarReaderUtil getInstance(){
		return INSTANCE;
	}
	
	public Long getCurrentDate(){
		return (new java.util.Date()).getTime();
	}

	public CalendarBean[] getBookingCalendar(String jsonResult){
		
		CalendarBean[] calBean = DEFAULT_CALBEAN;
		
		if(jsonResult != null){
			try{
				JSONArray  jArray = new JSONArray(jsonResult);
				
				calBean = new CalendarBean[jArray.length()];
				
				for(int loop=0; loop < jArray.length(); loop++){
					JSONObject jobj = jArray.getJSONObject(loop);
					CalendarBean calendar = new CalendarBean(jobj.getString(CalendarBean.TITLE),
							jobj.getLong(CalendarBean.START), jobj.getLong(CalendarBean.END),
							jobj.getBoolean(CalendarBean.ALL_DAY),
							jobj.getString(CalendarBean.DESC),
							jobj.getInt(CalendarBean.AVAILABILITY),
							jobj.getString(CalendarBean.ID)
							);
					calBean[loop] = calendar;
				}
			}catch(Exception e){
				calBean = DEFAULT_CALBEAN;
    			Log.e("Error", jsonResult);
    		}
		}
		
		return calBean;
	}
}
