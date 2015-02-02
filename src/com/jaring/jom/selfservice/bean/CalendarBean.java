package com.jaring.jom.selfservice.bean;

import java.io.Serializable;

public class CalendarBean implements Serializable{
	
	private static final long serialVersionUID = 3682154965244247699L;
	
	public final static String BEAN_NAME = "CalendarBean";
	
	public final static String TITLE = "title";
	public final static String START = "start";
	public final static String END = "end";
	public final static String ALL_DAY = "allDay";
	public final static String DESC = "desc";
	public final static String AVAILABILITY = "availability";
	public final static String ID = "_id";
	
	public final String title;
	public final Long start;
	public final Long end;
	public final Boolean allDay;
	public final String desc;
	public final Integer availability;
	public final String id;
	
	public CalendarBean(String title, Long start, Long end, Boolean allDay, String desc, Integer availability, String id){
		this.title = title;
		this.start = start;
		this.end = end;
		this.allDay = allDay;
		this.desc = desc;
		this.availability = availability;
		this.id = id;
	}
	
	@Override
	public String toString(){
		return "title:"+title+","
				+"start:"+start+","
				+"end:"+end+","
				+"allDay:"+allDay+","
				+"desc:"+desc+","
				+"avail:"+availability+","
				+"id:"+id;
		
	}
}
