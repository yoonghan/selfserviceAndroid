package com.jaring.jom.selfservice;


import com.example.selfservice.R;
import com.facebook.Session;
import com.google.android.gms.common.SignInButton;

import com.jaring.jom.selfservice.util.AuthorizationDialog;
import com.jaring.jom.selfservice.util.ConnectionUtil;

import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import static com.jaring.jom.selfservice.constants.Constants.*;
import static com.jaring.jom.selfservice.constants.Parameters.*;

public class Login extends FragmentActivity {
	
	private AuthorizationDialog authDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		
		//Make sign in button.
		findViewById(R.id.signin_google_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onGoogleClick(v);
			}
		});
		SignInButton b = ((SignInButton)findViewById(R.id.signin_google_button));
		b.setSize(SignInButton.SIZE_WIDE);
		findViewById(R.id.authButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onFacebookClick(v);
			}
		});
		
		
		authDialog = new AuthorizationDialog(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return false;
	}
	
	public void onGoogleClick(View view){
		getAuth(GOOGLE_LOGIN_URL.returnKey());
	}
	
	public void onFacebookClick(View view){
		getAuth(FACEBOOK_LOGIN_URL.returnKey());
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	private void getAuth(String urlkey){
		//Check if there is internet connection
		if(ConnectionUtil.INSTANCE.haveNetworkConnection(this)){
			authDialog.start(urlkey, STOP_URL_KEYWORD.returnKey(), BASE_URL.returnKey(), SESSION_KEY.returnKey(), Setting.class);
		}
	}
}
