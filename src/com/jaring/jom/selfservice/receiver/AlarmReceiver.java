package com.jaring.jom.selfservice.receiver;

import com.jaring.jom.selfservice.DisplayEvent;
import com.jaring.jom.selfservice.bean.CalendarBean;
import com.jaring.jom.selfservice.constants.Parameters;

import android.support.v4.app.NotificationCompat;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.R.drawable.*;
import static com.jaring.jom.selfservice.bean.CalendarBean.*;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
    public void onReceive(Context ctx, Intent intent) {
		
		final int counter = intent.getIntExtra(Parameters.COUNTER_NAME.returnKey(), 0);
		final CalendarBean cb = (CalendarBean)intent.getSerializableExtra(BEAN_NAME);
		
		Intent resultIntent = new Intent(ctx, DisplayEvent.class);
    	resultIntent.putExtra(CalendarBean.BEAN_NAME, cb);
				
    	PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    	NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(ctx)
    		 .setAutoCancel(true)
    	     .setSmallIcon(star_on)
    	     .setContentTitle(cb.title)
    	     .setWhen(System.currentTimeMillis())
    	     .setContentIntent(pendingIntent)
    	     .setTicker("You've Got A Message")
    	     .setContentText(cb.desc);
    	    mBuilder.setContentIntent(pendingIntent);

    	    NotificationManager mNotificationManager =  (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
    	    // mId allows you to update the notification later on.
    	    mNotificationManager.notify(counter, mBuilder.build());

    }
}
