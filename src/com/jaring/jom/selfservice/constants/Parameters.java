package com.jaring.jom.selfservice.constants;

public enum Parameters {
	COOKIE_HEADER("Set-Cookies"),
	COUNTER_NAME("CounterName"),
	SESSION_KEY("PLAY_SESSION");
	
	private final String param;
	
	Parameters(String param){
		this.param = param;
	}
	
	public String returnKey(){
		return param;
	}
}
