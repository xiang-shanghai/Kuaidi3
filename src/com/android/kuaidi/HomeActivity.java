package com.android.kuaidi;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Window;

public class HomeActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tab);
		
		init();
	}
	
	void init() {
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Tab trackTab = bar.newTab().setText(getString(R.string.tab_track))
				.setTabListener(new ClickListener(new TrackFragment()));
		Tab favoriteTab = bar.newTab().setText(getString(R.string.tab_favorite))
				.setTabListener(new ClickListener(new FavoriteFragment()));
		Tab allTab = bar.newTab().setText(getString(R.string.tab_all))
				.setTabListener(new ClickListener(new AllFragment()));
		
		bar.addTab(trackTab, 0);
		bar.addTab(favoriteTab, 1);
		bar.addTab(allTab, 2);
	}
	
	class ClickListener implements TabListener {
		Fragment fragment;
		
		public ClickListener(Fragment fragment) {
			this.fragment = fragment;
		}
		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			ft.add(R.id.fragment_content, fragment);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			ft.remove(fragment);
		}
		
	}
}
