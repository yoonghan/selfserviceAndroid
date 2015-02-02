package com.jaring.jom.selfservice;

import java.text.SimpleDateFormat;

import com.example.selfservice.R;
import com.jaring.jom.selfservice.bean.CalendarBean;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.widget.EditText;

public class DisplayEvent extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_event);
		
		Intent intent = getIntent();
		CalendarBean cb = (CalendarBean)intent.getSerializableExtra(CalendarBean.BEAN_NAME);

    	EditText editText = (EditText) findViewById(R.id.edtTitle);
    	editText.setText(cb.title);
    	editText = (EditText) findViewById(R.id.edtDescription);
    	editText.setText(cb.desc);
    	editText = (EditText) findViewById(R.id.edtDate);
    	editText.setText(SimpleDateFormat.getDateInstance().format(new java.util.Date(cb.start)));
    	editText = (EditText) findViewById(R.id.edtStartTime);
    	editText.setText(SimpleDateFormat.getTimeInstance().format(new java.util.Date(cb.start)));
    	editText = (EditText) findViewById(R.id.edtEndTime);
    	editText.setText(SimpleDateFormat.getTimeInstance().format(new java.util.Date(cb.end)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return false;
	}

}
