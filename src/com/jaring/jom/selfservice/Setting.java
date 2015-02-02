package com.jaring.jom.selfservice;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.example.selfservice.R;
import com.jaring.jom.selfservice.bean.CalendarBean;
import com.jaring.jom.selfservice.bean.CalendarReaderUtil;
import com.jaring.jom.selfservice.constants.Commands;
import com.jaring.jom.selfservice.constants.Parameters;
import com.jaring.jom.selfservice.receiver.AlarmReceiver;
import com.jaring.jom.selfservice.service.RestServiceCall;
import com.jaring.jom.selfservice.util.ConnectionUtil;

import android.support.v4.app.FragmentActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import static com.jaring.jom.selfservice.bean.CalendarBean.*;
import static com.jaring.jom.selfservice.constants.Parameters.COOKIE_HEADER;


public class Setting extends FragmentActivity {

	private PendingIntent pendingIntent;
	private AlarmManager alarmmanager;
	private String cookieValue;
	private final long notifyTime = 24 * 60 * 60 * 1000;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        cookieValue = intent.getStringExtra(COOKIE_HEADER.returnKey());
        setContentView(R.layout.activity_setting);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }
    
    public void onSendMessage(View view){
    	if(ConnectionUtil.INSTANCE.haveNetworkConnection(this)){
    		obtainService();
    	}
    }
    
    public void onLogout(View view){
    	ConnectionUtil.INSTANCE.logoutConnection();
		Intent intent = new Intent(this, Login.class);
    	this.startActivity(intent);
    }
    
    private void obtainCalendar(String cookieValue){
    	AsyncTask<String, String, String> restService = new RestServiceCall().execute(Commands.CMD_BOOK_CALENDAR.name(), cookieValue);
    	try{
    		String calendarJSON = restService.get(10000, TimeUnit.MILLISECONDS);
	    	CalendarBean[] results = CalendarReaderUtil.getInstance().getBookingCalendar(calendarJSON);
	    	createNotification(results);
    	}catch(InterruptedException ie){
    		ie.printStackTrace();
    	} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
    	
    }
    

	private void createNotification(CalendarBean[] results) {
		
		Long todaysDate = CalendarReaderUtil.getInstance().getCurrentDate();
		
		for(int counter = 0; counter< results.length; counter++){
			
			CalendarBean result = results[counter];
			
			if(result.start > todaysDate){
				callNotification(result, counter);
			}
		}
		
	}
    
    private void callNotification(CalendarBean calBean, int counter){
    	        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra(BEAN_NAME, calBean);
        alarmIntent.putExtra(Parameters.COUNTER_NAME.returnKey(), counter);
        
        pendingIntent = PendingIntent.getBroadcast(this, counter, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        
        alarmmanager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    	
//    	long x = new java.util.Date().getTime();
//    	x = x+(60000*(counter));
    	
    	Log.i("Notify Time:", "Next date:" + (new java.util.Date(calBean.start - notifyTime)));
    	
        alarmmanager.set(AlarmManager.RTC_WAKEUP,
//        		SystemClock.elapsedRealtime() +
//                60 * 1000
        		calBean.start - notifyTime
//        		x
                , pendingIntent);
    }
    
    private void obtainService(){
    	
		obtainCalendar(cookieValue);
		
		TextView txtView = (TextView) findViewById(R.id.lblUpdate);
		txtView.setText("Last Updated: "+SimpleDateFormat.getDateTimeInstance().format(new java.util.Date()));	
    }
    
    
}
