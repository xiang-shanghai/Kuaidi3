package com.android.kuaidi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import com.android.kuaidi.SliderView.OnTouchingLettersChangedListener;
import com.android.kuaidi.utils.CompanyHandler;
import com.android.kuaidi.utils.SQHelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AllFragment extends Fragment implements OnItemClickListener, 
															OnQueryTextListener{
	ListView listView;
	ProgressDialog dialog;
	SearchView mSearchView;
	SliderView sliderView;
	MyAdapter sortAdapter;
	List<SortModel> allSortList = new ArrayList<SortModel>();
	int index_long = -1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.activity_main, container, false);
		
		dialog = new ProgressDialog(getActivity());
		dialog.setMessage(getString(R.string.dialog_message));
		dialog.show();
		
		
		listView = (ListView)view.findViewById(R.id.company_list);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				index_long = pos;
				longClick();
				//itemLongClick(pos);
				return false;
			}
			
		});
		
		sliderView = (SliderView)view.findViewById(R.id.sliderview);
		sliderView.setTextView((TextView)getActivity().findViewById(R.id.dialog));
		sliderView.setOnTouchingLettersChangedListener(new OnTouchingLettersChangedListener() {
			
			@Override
			public void onTouchingLettersChanged(String letters) {
				// TODO Auto-generated method stub
				//该字母首次出现的位置
				int position = sortAdapter.getPositionForSection(letters.charAt(0));
				if(position != -1) {
					listView.setSelection(position);
				}
			}
		});
		
		
		new InitThread().start();
		
		return view;
	}
	
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		// TODO Auto-generated method stub
//		super.onCreateContextMenu(menu, v, menuInfo);
//		menu.add(0, 0, 0, "添加到常用快递");
//	}
	
	private void longClick() {
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub				
				menu.add(0, 0, 0, "添加到常用快递");
			}
		});
	}
//	
//	void itemLongClick(int pos) {
//		SQHelper helper = new SQHelper(getActivity());
//		List<Map<String, String>> lists = sortAdapter.getItem(pos);
//		Map<String, String> map = lists.get(0);
//		String company_name = map.get("name");
//		String company_code = map.get("code");
//		
//		long result = helper.addFavorite(company_name, company_code);
//		if(result == -1) {
//			Toast.makeText(getActivity(), "添加失败", Toast.LENGTH_SHORT).show();				
//		} else {
//			Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
//		}
//	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 0:
			SQHelper helper = new SQHelper(getActivity());
			int pos = index_long;
			index_long = -1;
			List<Map<String, String>> lists = sortAdapter.getItem(pos);
			Map<String, String> map = lists.get(0);
			String company_name = map.get("name");
			String company_code = map.get("code");
			
			long result = helper.addFavorite(company_name, company_code);
			if(result == -1) {
				Toast.makeText(getActivity(), "添加失败", Toast.LENGTH_SHORT).show();				
			} else {
				Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
		
		return super.onContextItemSelected(item);		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		//Log.i("wx", "onActivityCreated");
		
		//new InitThread().start();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//		
//		MenuInflater menuInflater = getMenuInflater();
//		menuInflater.inflate(R.menu.main, menu);
//		MenuItem item = menu.findItem(R.id.action_search);
//		//if(item != null) {
//			mSearchView = (SearchView)item.getActionView();
//			mSearchView.setOnQueryTextListener(this);
//		//}
//		return true;
//	}
//	
	

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		
		updateList(newText);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	void updateList(String filter) {
		List<SortModel> sortList = sortAdapter.getSortLists();
		if(sortList == null ||sortList.size() <= 0) {
			sortList = allSortList;
		}
		List<SortModel> newList = new ArrayList<SortModel>();
		
		
		if(TextUtils.isEmpty(filter)) {
			newList = allSortList;
		} else {
			newList.clear();
			for(SortModel model : sortList) {
				String name = model.getName();
				if(name.toUpperCase().indexOf(filter.toString().toUpperCase()) != -1
						||new CharacterParser().getSelling(name).toUpperCase().startsWith(filter.toString().toUpperCase())) {
					newList.add(model);
				}
			}			
		}
		
		Collections.sort(newList, new PinyinComparator());
		sortAdapter.updateListView(newList);
	}

	class InitThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			AssetManager assetManager = getActivity().getAssets();
			try {
				InputStream is = assetManager.open("companys.xml");
				SAXParserFactory factory = SAXParserFactory.newInstance();
				try {					
					SAXParser parser = factory.newSAXParser();
					CompanyHandler handler = new CompanyHandler();
					
					parser.parse(is, handler);
					
					List<Company> companies = handler.getCompanies();
					MyHandler uiHandler = new MyHandler(getActivity().getMainLooper());
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
				
				//List<SortModel> sortList = new ArrayList<SortModel>();
				for (int i = 0; i < names.size(); i++) {
					SortModel sortModel = new SortModel();
					sortModel.setName(names.get(i));
					//汉字转为拼音
					String pinyin = CharacterParser.getInstance().getSelling(names.get(i));
					String sortString = pinyin.substring(0, 1).toUpperCase();
					//判断是否是英文字母
					if(sortString.matches("[A-Z]")) {
						sortModel.setLettters(sortString.toUpperCase()); //toUpperCase转为大写字母
					} else {
						sortModel.setLettters("#");
					}
					
					allSortList.add(sortModel);
				}
				
				//根据a-z排序数据源
				Collections.sort(allSortList, new PinyinComparator());
				sortAdapter = new MyAdapter(getActivity(), allSortList, codes);
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
		List<Map<String, String>> lists = sortAdapter.getItem(position);
		Map<String, String> map = lists.get(0);
		String company_name = map.get("name");
		String company_code = map.get("code");
		Intent intent = new Intent(getActivity(), SearchActivity.class);
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
			
			//Log.i("wx", "MyAdapter---");
		}
		
		public List<SortModel> getSortLists() {
			if(sortList.size() > 0) {
				return sortList;
			}
			return null;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			//Log.i("wx", "getCount---- " + this.sortList.size());
			return this.sortList.size();
		}

		@Override
		public List<Map<String, String>> getItem(int position) {
			// TODO Auto-generated method stub			
			List<Map<String, String>> lists = new ArrayList<Map<String,String>>();
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", sortList.get(position).getName());
			map.put("code", codes.get(position));
			lists.add(map);

			
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
			Log.i("wx", "getView---");
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
		 *	根据Listview的当前位置获取分类的首字母的char ascii值 
		 */
		public int getSectionForPosition(int position) {
			return sortList.get(position).getLettters().charAt(0);
		}
		
		/**
		 * 根据分类的首字母的char ASCII值获取其第一次出现该首字母的位置
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
		
		//当ListView数据变化时更新
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
	
	/**
	 * Java中使用Comparator对集合对象或者数组对象进行排序
	 * @author Administrator
	 *
	 */
	class PinyinComparator implements Comparator<SortModel> {
		/**
		 * 对Listview里面的数据根据ABCD...的顺率排序
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
