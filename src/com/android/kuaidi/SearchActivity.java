package com.android.kuaidi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.android.kuaidi.utils.Consants;
import com.android.kuaidi.utils.ICKDInfo;
import com.android.kuaidi.utils.ICKDParser;
import com.android.kuaidi.utils.NetworkUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity{
	TextView txtDisplay;
	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search);
		
		TextView txtCompany = (TextView)findViewById(R.id.txtCompany);
		txtDisplay = (TextView)findViewById(R.id.txtDisplay);
		
		Intent intent = getIntent();
		txtCompany.setText(intent.getExtras().getString("name"));
		
		final EditText edtNumber = (EditText)findViewById(R.id.edtNumber);
		
		
		Button btnSearch = (Button)findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(edtNumber.getText().toString().equals("")
						|| edtNumber.getText().toString() == null) {
					Toast.makeText(SearchActivity.this, 
							getString(R.string.edt_number), Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if(!new NetworkUtil(SearchActivity.this).isNetworkConnected()) {
					Toast.makeText(SearchActivity.this, 
							getString(R.string.network_not_connected), Toast.LENGTH_SHORT)
							.show();
					return;
				}
				dialog = new ProgressDialog(SearchActivity.this);
				dialog.setMessage("正在努力查询中...");
				dialog.show();
				new Thread(){
					public void run() {						
						String code = getIntent().getExtras().getString("code");
						String url = "http://api.ickd.cn/"
								+"?id="+Consants.ICKD_API
								+"&secret="+Consants.ICKD_SECRET
								+"&com="+code
								+"&nu="+edtNumber.getText().toString()
								+"&type=json&encode=utf8";
						String result = new NetworkUtil(SearchActivity.this).getResult(url);
						ICKDInfo ickdInfo = ICKDParser.getICKDInfo(result);
						
//						StringBuffer sb = new StringBuffer();
//						sb.append("查询结果：" + ickdInfo.getStatusString() + "\n");
//						sb.append("错误代码：" + ickdInfo.getErrorCodeString() + "\n");
//						sb.append("错误消息：" + ickdInfo.getMessage()+ "\n");
//						sb.append("进度：" + "\n");
//						
//						List<ICKDInfo.Data> dataList = ickdInfo.getData();	
//						
//						for (int i = 0; i < dataList.size(); i++) {
//							ICKDInfo.Data data = dataList.get(i);
//							sb.append(data.getTime() + "\n");
//							sb.append(data.getContext() + "\n");
//						}
//						
//						sb.append("快递公司：" + ickdInfo.getExpTextName() + "\n");
//						sb.append("快递单号：" + ickdInfo.getMailNo() + "\n");
//						sb.append("电话：" + ickdInfo.getTel() + "\n");
						
						StringBuffer sb = getResultByInfo(ickdInfo);
						
						MyHandler handler = new MyHandler(getMainLooper());
						Message msg = handler.obtainMessage(1);
						Bundle b = new Bundle();
						b.putString("result", sb.toString());
						msg.setData(b);
						handler.sendMessage(msg);												
					};
				}.start();
				
			}
		});
	}
	
	StringBuffer getResultByInfo(ICKDInfo info) {
		StringBuffer sb = new StringBuffer();
		//查询失败||其他问题
		if(info.getStatus()==0 || info.getStatus()==5) {
			sb.append(info.getMessage() + "\n");
			return sb;
		} else {			
			sb.append("状态：" + info.getStatusString() + "\n");
			sb.append("进度：" + "\n");
			
			List<ICKDInfo.Data> dataList = info.getData();	
			
			for (int i = 0; i < dataList.size(); i++) {
				ICKDInfo.Data data = dataList.get(i);
				sb.append(data.getTime() + "\n");
				sb.append(data.getContext() + "\n");
			}
		}			
		
		return sb;
	}
	
	class MyHandler extends Handler {
		public MyHandler(Looper looper) {
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				txtDisplay.setText(msg.getData().getString("result"));
				dialog.dismiss();
				break;

			default:
				break;
			}
		}
	}
}
