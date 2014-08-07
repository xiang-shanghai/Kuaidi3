package com.android.kuaidi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.android.kuaidi.utils.NetworkUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search);
		
		TextView txtCompany = (TextView)findViewById(R.id.txtCompany);
		
		Intent intent = getIntent();
		txtCompany.setText(intent.getExtras().getString("name"));
		
		final EditText edtNumber = (EditText)findViewById(R.id.edtNumber);
		
		
		Button btnSearch = (Button)findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(edtNumber.getText().equals("")) {
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
				
				new Thread(){
					public void run() {
						//edtNumber.setText("http://wap.kuaidi100.com/wap_result.jsp?rand=20120517&id=shunfeng&fromWeb=null&&postid=117906827502");
						String code = getIntent().getExtras().getString("code");
						//String url = "http://m.kuaidi100.com/index_all.html?type=["+code+"]&postid=["+edtNumber.getText().toString()+"]&callbackurl=[wap.baidu.com]";
						//String url = "http://wap.kuaidi100.com/wap_result.jsp?rand=20120517&id=["+code+"]&fromWeb=null&&postid=["+edtNumber.getText().toString()+"]";
						//String url = "http://wap.kuaidi100.com/wap_result.jsp?rand=20120517&id=shunfeng&fromWeb=null&&postid=117906827502";
						//String url = "http://m.kuaidi100.com/index_all.html?type=shunfeng&postid=117906827502&callbackurl=wap.baidu.com";
						String url = "http://wap.kuaidi100.com/wap_result.jsp?rand=20120517&id=shunfeng&fromWeb=null&&postid=1179068275022";
						String result = new NetworkUtil(SearchActivity.this).getResult(url);
						
						
					};
				}.start();
				
			}
		});
	}
}
