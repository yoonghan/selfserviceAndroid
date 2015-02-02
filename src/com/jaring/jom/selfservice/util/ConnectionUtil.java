package com.jaring.jom.selfservice.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.example.selfservice.R;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;

public enum ConnectionUtil {
	INSTANCE;
	
	private final String BASE_URL ="scala.jomjaring.com";
	private final String BASE_URL_PATH = "http://"+BASE_URL+"";
	
	private final DefaultHttpClient httpClient;
	private final String SESSION_KEY = "PLAY_SESSION";
	
	/**
	 * Set connection
	 */
	private final int CONN_TIMEOUT = 10 * 1000;
	private final int SO_TIMEOUT = 30 * 1000;
	
	private ConnectionUtil(){
		httpClient = new DefaultHttpClient();
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, CONN_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);
		
		httpClient.setRedirectHandler(new DefaultRedirectHandler() {
			
		    @Override
		    public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
		        boolean isRedirect = super.isRedirectRequested(response, context);
		        
		        if (!isRedirect) {
		            int responseCode = response.getStatusLine().getStatusCode();
		            if (responseCode == 301 || responseCode == 302) {
		                return true;
		            }
		        }
		        return isRedirect;
		    }
		    
		});
	}
	
	/**
	 * Retrieve only ascii characters given.
	 * @param entity
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
    	InputStream in = entity.getContent();
    	StringBuffer out = new StringBuffer(100);
    	
    	try{
	    	
	    	int n = 1;
	    	while (n>0) {
	    		byte[] b = new byte[4096];
	    		n =  in.read(b);
	
	    		if (n>0) out.append(new String(b, 0, n));
	    	}
    	}finally{
    		in.close();
    	}

    	return out.toString();
	}
	
	public DefaultHttpClient getHttpClient(){
		return httpClient;
	}
	
	public DefaultHttpClient getConnection(){
		return httpClient;
	}
	
	public void logoutConnection(){
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}
	
	/**
	 * Call GET method with cookie value 
	 * @param url
	 * @param cookieValue
	 * @param cookieDomain
	 * @return
	 */
	public String getConnection(String relativeUrl, String cookieValue){
		CookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie(SESSION_KEY, cookieValue);
		cookie.setDomain(BASE_URL);
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		httpClient.setCookieStore(cookieStore);
		
		HttpContext localContext = new BasicHttpContext();
		
		HttpGet httpGet = new HttpGet(BASE_URL_PATH+relativeUrl);
		String text = null;
    	try {
	    	HttpResponse response = httpClient.execute(httpGet, localContext);
	    	HttpEntity entity = response.getEntity();
	    	text = getASCIIContentFromEntity(entity);
    	} catch (Exception e) {
    		Log.e("Call Rest",e.getLocalizedMessage());
    	}
    	return text;
	}
	
	/**
	 * Authenticate user, via testing.
	 * @return
	 */
	public String authenticateUser(String relativeUrl){
		
		CookieStore cookieStore = new BasicCookieStore();
		httpClient.setCookieStore(cookieStore);
		
		HttpContext localContext = new BasicHttpContext();
		
		HttpGet httpGet = new HttpGet(BASE_URL_PATH+relativeUrl);
		
    	String cookieResult = "";
    	try {
	    	httpClient.execute(httpGet, localContext);
	    	List<Cookie> cookie = cookieStore.getCookies();
	    	cookieResult = cookie.get(0).getValue();
    	} catch (Exception e) {
    		return e.getLocalizedMessage();
    	}

    	return cookieResult;
	}
	
	
	public boolean haveNetworkConnection(Context ctx) {
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    
	    if((haveConnectedWifi|| haveConnectedMobile)==false){
	    	final Dialog connectionDialog = new Dialog(ctx);
	    	connectionDialog.setContentView(R.layout.connectivity_dialog);
	    	final Button button = (Button) connectionDialog.findViewById(R.id.btnOK);
	    	button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					connectionDialog.dismiss();
				}
			});
	    	connectionDialog.show();
	    	connectionDialog.setTitle("Error");
	    	connectionDialog.setCancelable(false);
	    }
	    
	    return haveConnectedWifi || haveConnectedMobile;
	}
}
