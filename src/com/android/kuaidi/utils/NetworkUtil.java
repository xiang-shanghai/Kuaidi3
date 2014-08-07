package com.android.kuaidi.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkUtil {

	Context mContext;
	public NetworkUtil(Context context) {
		mContext = context;
	}
	
	public boolean isNetworkConnected() {
		ConnectivityManager manager = (ConnectivityManager)mContext.getSystemService(
				Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		
		return info.isConnectedOrConnecting();
	}
	
	public String getResult(String url) {		
		StringBuffer result = new StringBuffer();
		try {
			URL path = new URL(url);
			Log.i("wx", "111");
			HttpURLConnection conn = (HttpURLConnection)path.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5*1000);
			Log.i("wx", "==== respose :" + conn.getResponseCode());
			InputStream is = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = "";
			try {
				while((line = reader.readLine()) != null) {
					result.append(line + "\n");
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.i("wx", "00000000: " + e.getMessage());
			}
			
			Log.i("wx", "result: " + result);
			conn.disconnect();
		} catch (Exception e) {
			Log.i("wx", "==>  getResult: " + e.getMessage());
			// TODO: handle exception
		}
		
		return result.toString();
	}
}
