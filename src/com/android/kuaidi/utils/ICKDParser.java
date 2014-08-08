package com.android.kuaidi.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


import android.util.Log;

public class ICKDParser {
	
	public static ICKDInfo getICKDInfo(String value) {
		ICKDInfo info = new ICKDInfo();
		
		try {
			JSONObject object = new JSONObject(value);
						
			info.setStatus(object.getInt("status"));
			info.setMessage(object.getString("message"));
			info.setErrorCode(object.getInt("errCode"));
			info.setMailNo(object.getString("mailNo"));
			info.setExpTextName(object.getString("expTextName"));
			info.setTel(object.getString("tel"));
			
			Log.i("wx", "mailNo:" + object.getString("mailNo"));
			Log.i("wx", "setExpTextName:" + object.getString("expTextName"));
			Log.i("wx", "tel:" + object.getString("tel"));
			
			List<ICKDInfo.Data> dataList = new ArrayList<ICKDInfo.Data>();
			JSONArray dataArray = object.getJSONArray("data");
			for (int i = 0; i < dataArray.length(); i++) {
				JSONObject dataObject = (JSONObject)dataArray.opt(i);
				ICKDInfo.Data data = new ICKDInfo().new Data();
				data.setTime(dataObject.getString("time"));
				data.setContext(dataObject.getString("context"));
				dataList.add(data);
			}
			info.setData(dataList);
			
			Log.i("wx", "000  " + info.toString());
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("wx", "====== json " + e.getMessage());
		}
		
		
		
		return info;
	}
}
