package com.jaring.jom.selfservice.util;

import static com.jaring.jom.selfservice.constants.Parameters.COOKIE_HEADER;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.selfservice.R;

public class AuthorizationDialog {
	
	public final Context ctx;
	
	private final Dialog auth_dialog;
    private final WebView web;
    private final ProgressBar progressBar;
    
    private String cookieValue="";
    
	
	public AuthorizationDialog(Context ctx){
		this.ctx=ctx;
		this.auth_dialog = new Dialog(ctx, R.style.dialog_style);
		auth_dialog.setContentView(R.layout.auth_dialog);
		
		this.progressBar = (ProgressBar)auth_dialog.findViewById(R.id.webvloading);
	    this.web = (WebView)auth_dialog.findViewById(R.id.webv);
	    
	    web.setAnimationCacheEnabled(false);
	    web.setDrawingCacheEnabled(false);
	    web.setHapticFeedbackEnabled(false);
	    web.setSoundEffectsEnabled(false);
	    
	    
	    WebSettings webSetting = web.getSettings();
	    webSetting.setJavaScriptEnabled(false);
	    webSetting.setLoadsImagesAutomatically(false);
	    webSetting.setRenderPriority(RenderPriority.HIGH);
	    webSetting.setSavePassword(false);
	    webSetting.setDomStorageEnabled(false);
	    webSetting.setDatabaseEnabled(false);
	    webSetting.setSupportZoom(false);
	    webSetting.setSupportMultipleWindows(false);
	    
	}
	
	public void start(final String oauthURL, final String stopURL, final String cookieURL, final String cookieKey, final Class<? extends Activity> clazz ){
		
		progressBar.setVisibility(ProgressBar.VISIBLE);
		
        web.loadUrl(oauthURL);
        
        web.setWebViewClient(new WebViewClient() {
        	
        	public void onPageStarted(WebView view, String url, Bitmap favicon){
        		super.onPageStarted(view, url, favicon);
        		
        		if(url.contains(stopURL)){
        			cookieValue = getCookie(cookieURL, cookieKey);
                	click(clazz);
                	auth_dialog.dismiss();
                }
        	}
        	
        	@Override
        	public void onPageFinished(WebView view, String url) {
        		super.onPageFinished(view, url);
        		progressBar.setVisibility(ProgressBar.INVISIBLE);
        	}
          });
        auth_dialog.show();
        auth_dialog.setTitle("Authorization Window");
        auth_dialog.setCancelable(true);
    }
	
	public String getCookieValue(){
		return cookieValue;
	}
    
    private String getCookie(String siteName,String CookieName){     
        

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);

        String[] temp=cookies.split("\"");
        
        return temp.length > 1 ? temp[1]: ""; 
    }
    
	private void click( Class<? extends Activity> clazz) {
		Intent intent = new Intent(ctx, clazz);
    	intent.putExtra(COOKIE_HEADER.returnKey(), cookieValue);
    	ctx.startActivity(intent);
	}
}
