package com.android.kuaidi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import com.android.kuaidi.utils.CompanyHandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	ListView listView;
	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		dialog = new ProgressDialog(this);
		dialog.setMessage(getString(R.string.dialog_message));
		dialog.show();
		
		listView = (ListView)findViewById(R.id.company_list);
		
		new InitThread().start();
	}

	class InitThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			AssetManager assetManager = getAssets();
			try {
				InputStream is = assetManager.open("companys.xml");
				SAXParserFactory factory = SAXParserFactory.newInstance();
				try {					
					SAXParser parser = factory.newSAXParser();
					CompanyHandler handler = new CompanyHandler();
					
					parser.parse(is, handler);
					
					List<Company> companies = handler.getCompanies();
					MyHandler uiHandler = new MyHandler(getMainLooper());
					Message message = uiHandler.obtainMessage();
					message.what = 1;					
					message.obj = companies;
					uiHandler.sendMessage(message);
					
				} catch (Exception e) {
					// TODO: handle exception
					Log.i("wx", "=== " + e.getMessage());
				}
			} catch (IOException e) {
				// TODO: handle exception
			}
		}
	}
	
	class MyHandler extends Handler {
		public MyHandler() {
			
		}
		
		public MyHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				List<Company> compList = (List<Company>)msg.obj;
				List<String> names = new ArrayList<String>();
				List<String> icons = new ArrayList<String>();
//				ArrayList<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();				
//				Iterator<Company> iteator = compList.iterator();
//				while(iteator.hasNext()) {
//					Company company = iteator.next();
//					Map<String, Object> map = new HashMap<String, Object>();
//					
//					map.put("name", company.getName());
//					String iconName = company.getIcon();
//					try {						
//						AssetManager manager = getAssets();
//						Bitmap bm = BitmapFactory.decodeStream(manager.open(iconName));
//						map.put("icon", bm);
//					} catch (Exception e) {
//						// TODO: handle exception
//						Log.i("wx", "========= e " + e.getMessage());
//					}										
//					lists.add(map);				
//				}
//				SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, 
//						lists, 
//						R.layout.company_list, 
//						new String[]{"name", "icon"},
//						new int[]{R.id.company_name, R.id.company_icon}
//						);
//				listView.setAdapter(adapter);
				for(int i = 0; i < compList.size(); i++) {
					Company company = compList.get(i);
					names.add(company.getName());
					icons.add(company.getIcon());
				}
				MyAdapter adapter = new MyAdapter(MainActivity.this, names, icons);
				listView.setAdapter(adapter);
				dialog.dismiss();
				break;

			default:
				break;
			}
			
		}
	}
	
	class MyAdapter extends BaseAdapter {
		Context mContext;
		List<String> names;
		List<String> icons;
		
		public MyAdapter(Context context, List<String> names, List<String> icons) {
			this.mContext = context;
			this.names = names;
			this.icons = icons;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return names.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view ;
			
			view = LayoutInflater.from(mContext)
					.inflate(R.layout.company_list, null);
			((TextView)view.findViewById(R.id.company_name)).setText(names.get(position));
			ImageView imageView = (ImageView)view.findViewById(R.id.company_icon);
			try {
				Bitmap bm = BitmapFactory.decodeStream(getAssets().open(icons.get(position)));
				imageView.setImageBitmap(bm);
			} catch (Exception e) {
				// TODO: handle exception
				Log.i("wx", "adapter=== " + e.getMessage());
			}
			return view;
		}
		
	}
}
