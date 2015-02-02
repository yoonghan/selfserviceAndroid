package com.jaring.jom.selfservice.constants;

public enum Constants {
	
	BASE_URL("http://scala.jomjaring.com"),
	LOGIN_URL(BASE_URL.returnKey()+"/service/oauth/send/"),
	STOP_URL_KEYWORD("/calendar"),
	DEV_GOOGLE_KEYWORD("D_G"),
	DEV_FACEBOOK_KEYWORD("D_F"),
	FACEBOOK_LOGIN_URL(LOGIN_URL.returnKey() + DEV_FACEBOOK_KEYWORD.returnKey()),
	GOOGLE_LOGIN_URL(LOGIN_URL.returnKey() + DEV_GOOGLE_KEYWORD.returnKey());
	
	private final String constant;
	
	Constants(String constant){
		this.constant = constant;
	}
	
	public String returnKey(){
		return constant;
	}
}
