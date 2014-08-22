package com.android.kuaidi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.android.kuaidi.utils.SQHelper;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FavoriteFragment extends Fragment{
	private ListView favoriteListView;
	private Button btnAdd;
	private TextView txtEmpty;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.favorite, container, false);
		favoriteListView = (ListView)view.findViewById(R.id.favorite_list);
		btnAdd = (Button)view.findViewById(R.id.btnFavorite_Add);
		txtEmpty = (TextView)view.findViewById(R.id.txt_empty);
		
		showView();
		
		
		return view;
	}
	
	
	private void showView() {
		SQHelper helper = new SQHelper(getActivity());
		int size = helper.queryFavorite().size();		
		if(size <= 0) {
			txtEmpty.setVisibility(View.VISIBLE);
			btnAdd.setVisibility(View.VISIBLE);
			favoriteListView.setVisibility(View.GONE);
			return;
		}
		txtEmpty.setVisibility(View.GONE);
		btnAdd.setVisibility(View.GONE);
		favoriteListView.setVisibility(View.VISIBLE);
		favoriteListView.setAdapter(new FavoriteAdapter(getActivity()));
	}
	
	
	class FavoriteAdapter extends BaseAdapter {
		Context mContext;
		SQHelper helper;
		List<Map<String, String>> favorites = new ArrayList<Map<String,String>>();
		
		public FavoriteAdapter(Context context) {
			mContext = context;
			helper = new SQHelper(mContext);
			favorites = helper.queryFavorite();
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return favorites.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			
			if(convertView == null) {				
				convertView = LayoutInflater.from(mContext).inflate(R.layout.favorite_list, null);
				viewHolder = new ViewHolder();
				viewHolder.txtName = (TextView)convertView.findViewById(R.id.favorite_name);
				viewHolder.btnSearch = (Button)convertView.findViewById(R.id.favorite_search);
				viewHolder.btnDel = (Button)convertView.findViewById(R.id.favorite_del);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			
			viewHolder.txtName.setText(favorites.get(position).get("name"));
			
			ButtonListener listener = new ButtonListener(position);
			viewHolder.btnSearch.setOnClickListener(listener);
			viewHolder.btnDel.setOnClickListener(listener);
			
			
			return convertView;
		}
		
		class ViewHolder {
			TextView txtName;
			Button btnSearch;
			Button btnDel;
		}
				
		class ButtonListener implements View.OnClickListener {
			int position;
			public ButtonListener(int pos) {
				position = pos;				
			}
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				switch (v.getId()) {
				case R.id.favorite_search:						
					String company_name = favorites.get(position).get("name");
					String company_code = favorites.get(position).get("code");
					Intent intent = new Intent(getActivity(), SearchActivity.class);
					intent.putExtra("name", company_name);
					intent.putExtra("code", company_code);
					startActivity(intent);
					break;
				case R.id.favorite_del:							
					int result = helper.removeFavoriteByName(favorites.get(position).get("name"));
					if(result < 0) {
						Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
					}
					//刷新列表
					showView();
					favoriteListView.setAdapter(new FavoriteAdapter(getActivity()));	
					break;

				default:
					break;
				}
			}
			
		}
	}
}
