package com.android.kuaidi.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQHelper extends SQLiteOpenHelper {
	Context mContext;
	private static final String DATABASE_NAME = "kuaidi.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_FAVORITE = "favorite";
	

	public SQHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE "+ TABLE_FAVORITE + 
				"(_id INTEGER PRIMARY KEY," +
				"name TEXT," +
				"code TEXT);");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if(oldVersion != DATABASE_VERSION) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
			onCreate(db);
		}
	}
	
	public long addFavorite(String name, String code) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();		
		values.put("name", name);
		values.put("code", code);
		long result = db.insert(TABLE_FAVORITE, null, values);
		
		db.close();
		return result;
	}
	
	public int removeFavoriteByName(String name) {
		SQLiteDatabase db = getWritableDatabase();
		
		int result = db.delete(TABLE_FAVORITE, "name=?", new String[]{name});		
		return result;
	}
	
	public int removeFavorite() {
		SQLiteDatabase db = getWritableDatabase();
		int result = db.delete(TABLE_FAVORITE, "1", null);
		
		return result;
	}
	
	public List<Map<String, String>> queryFavorite() {
		List<Map<String, String>> lists = new ArrayList<Map<String,String>>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_FAVORITE, null, null, null, null, null, null);
		
		cursor.moveToFirst();
		while(cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", cursor.getString(cursor.getColumnIndex("name")));
			map.put("code", cursor.getString(cursor.getColumnIndex("code")));
			lists.add(map);
		}
		
		return lists;
	}
}
