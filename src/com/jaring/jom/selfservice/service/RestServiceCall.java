package com.jaring.jom.selfservice.service;

import com.jaring.jom.selfservice.constants.Commands;
import com.jaring.jom.selfservice.util.ConnectionUtil;

import android.os.AsyncTask;

public class RestServiceCall extends AsyncTask<String, String, String> {
	
	private final String AUTH_PATH = "/user/testprofile";
	private final String RESERVED_PATH = "/tools/reserve";
	
	
	public RestServiceCall(){
	}
	
	/**
	 * Run this in background to retrieve authenticated user.
	 */
	@Override
	protected String doInBackground(String... params) {
		
		String text = "";
		
		switch(Commands.valueOf(params[0])){
		case CMD_AUTHORITY:
			text = ConnectionUtil.INSTANCE.authenticateUser(AUTH_PATH);
			break;
		case CMD_BOOK_CALENDAR:
			text = obtainCalendarReserved(params[1]);
			break;
		}
		
    	return text;
	}
	
	private String obtainCalendarReserved(String cookieValue){
		return ConnectionUtil.INSTANCE.getConnection(RESERVED_PATH, cookieValue);
	}
	

}