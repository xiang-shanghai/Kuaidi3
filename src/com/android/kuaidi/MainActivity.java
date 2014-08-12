package com.android.kuaidi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import com.android.kuaidi.SliderView.OnTouchingLettersChangedListener;
import com.android.kuaidi.utils.CompanyHandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements OnItemClickListener, 
															OnQueryTextListener{
	ListView listView;
	ProgressDialog dialog;
	SearchView mSearchView;
	SliderView sliderView;
	MyAdapter sortAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		
		
		dialog = new ProgressDialog(this);
		dialog.setMessage(getString(R.string.dialog_message));
		dialog.show();
		
		listView = (ListView)findViewById(R.id.company_list);
		listView.setOnItemClickListener(this);
		
		sliderView = (SliderView)findViewById(R.id.sliderview);
		sliderView.setTextView((TextView)findViewById(R.id.dialog));
		sliderView.setOnTouchingLettersChangedListener(new OnTouchingLettersChangedListener() {
			
			@Override
			public void onTouchingLettersChanged(String letters) {
				// TODO Auto-generated method stub
				//����ĸ�״γ��ֵ�λ��
				int position = sortAdapter.getPositionForSection(letters.charAt(0));
				if(position != -1) {
					listView.setSelection(position);
				}
			}
		});
		
		
		new InitThread().start();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);
		MenuItem item = menu.findItem(R.id.action_search);
		//if(item != null) {
			mSearchView = (SearchView)item.getActionView();
			mSearchView.setOnQueryTextListener(this);
		//}
		return true;
	}
	
	

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		
		if(TextUtils.isEmpty(newText)) {
			
		} else {
			
		}
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
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
				List<String> codes = new ArrayList<String>();

				for(int i = 0; i < compList.size(); i++) {
					Company company = compList.get(i);
					names.add(company.getName());
					icons.add(company.getIcon());
					codes.add(company.getCode());
				}
				
				List<SortModel> sortList = new ArrayList<SortModel>();
				for (int i = 0; i < names.size(); i++) {
					SortModel sortModel = new SortModel();
					sortModel.setName(names.get(i));
					//����תΪƴ��
					String pinyin = CharacterParser.getInstance().getSelling(names.get(i));
					String sortString = pinyin.substring(0, 1).toUpperCase();
					//�ж��Ƿ���Ӣ����ĸ
					if(sortString.matches("[A-Z]")) {
						sortModel.setLettters(sortString.toUpperCase());
					} else {
						sortModel.setLettters("#");
					}
					
					sortList.add(sortModel);
				}
				
				sortAdapter = new MyAdapter(MainActivity.this, sortList, codes);
				listView.setAdapter(sortAdapter);
				
				
				
				dialog.dismiss();
				break;

			default:
				break;
			}
			
		}
	}
	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		List<Map<String, String>> lists = (List<Map<String,String>>)listView.getItemAtPosition(position);
		Map<String, String> map = lists.get(0);
		String company_name = map.get("name");
		String company_code = map.get("code");
		Intent intent = new Intent(this, SearchActivity.class);
		intent.putExtra("name", company_name);
		intent.putExtra("code", company_code);
		startActivity(intent);
	}

	class MyAdapter extends BaseAdapter implements SectionIndexer{
		Context mContext;
		List<String> names;
		List<String> icons;
		List<String> codes;
		List<SortModel> sortList;
//		public MyAdapter(Context context, List<String> names, List<String> icons, List<String> codes) {
//			this.mContext = context;
//			this.names = names;
//			this.icons = icons;
//			this.codes = codes;
//		}
		
		public MyAdapter(Context context, List<SortModel> sortList, List<String> codes) {
			this.mContext = context;
			this.sortList = sortList;
			this.codes = codes;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.sortList.size();
		}

		@Override
		public List<Map<String, String>> getItem(int position) {
			// TODO Auto-generated method stub			
			List<Map<String, String>> lists = new ArrayList<Map<String,String>>();
			
			for (int i = 0; i < getCount(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", sortList.get(i).getName());
				map.put("code", codes.get(i));
				lists.add(map);
			}
			
			return lists;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		final class ViewHolder {
			TextView txtLetter;
			TextView txtTitle;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder viewHolder = null;			
			
			if(convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.sort_list, null);
				viewHolder.txtLetter = (TextView)convertView.findViewById(R.id.catalog);
				viewHolder.txtTitle = (TextView)convertView.findViewById(R.id.title);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			
			int section = getSectionForPosition(position);
			if(position == getPositionForSection(section)) {
				viewHolder.txtLetter.setVisibility(View.VISIBLE);
				viewHolder.txtLetter.setText(sortList.get(position).getLettters());
			} else {
				viewHolder.txtLetter.setVisibility(View.GONE);
			}
			
			viewHolder.txtTitle.setText(sortList.get(position).getName());
			
			return convertView;
		}
		
		/**
		 *	����Listview�ĵ�ǰλ�û�ȡ���������ĸ��char asciiֵ 
		 */
		public int getSectionForPosition(int position) {
			return sortList.get(position).getLettters().charAt(0);
		}
		
		/**
		 * ���ݷ��������ĸ��char ASCIIֵ��ȡ���һ�γ��ָ�����ĸ��λ��
		 */
		@Override
		public int getPositionForSection(int sectionIndex) {
			// TODO Auto-generated method stub
			for (int i = 0; i < getCount(); i++) {
				String sort = sortList.get(i).getLettters();
				char firstChar = sort.toUpperCase().charAt(0);
				if(firstChar == sectionIndex) 
					return i;				
			}
			
			return -1;
		}
		
		//��ListView���ݱ仯ʱ����
		public void updateListView(List<SortModel> sortList) {
			this.sortList = sortList;
			notifyDataSetChanged();
		}
		
		@Override
		public Object[] getSections() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	class PinyinComparator implements Comparator<SortModel> {
		/**
		 * ��Listview��������ݸ���ABCD...��˳������
		 */
		@Override
		public int compare(SortModel lhs, SortModel rhs) {
			// TODO Auto-generated method stub
			if(rhs.getLettters().equals("#")) {
				return -1;
			} else if(lhs.getLettters().equals("#")) {
				return 1;
			} else {
				return lhs.getLettters().compareTo(rhs.getLettters());
			}
		}
	}
}
