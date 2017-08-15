package com.cruiserli.simpleweather.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SimpleWeatherOpenHelper extends SQLiteOpenHelper {
	
	// Province 各省信息表建表语句
	public static final String CRREATE_PROVINCE = "Create Table Province("
			+ "id integer primary key autoincrement, "
			+ "province_name text, "
			+ "province_code text)";
	
	// City 城市表建表语句
	public static final String CREATE_CITY = "Create Table City("
			+ "id integer primary key autoincrement,"
			+ "city_name text,"
			+ "city_code text,"
			+ "province_id integer)";
	
	// County 各县信息表建表语句
	public static final String CREATE_COUNTY = "Create Table County("
			+ "id integer primary key autoincrement, "
			+ "county_name text,"
			+ "county_code text,"
			+ "city_id integer)";
	
	// 构造函数
	public SimpleWeatherOpenHelper(Context context, String name, CursorFactory factory, 
			int version){
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CRREATE_PROVINCE);	// 创建省Province表
		db.execSQL(CREATE_CITY);		// 创建城市City表
		db.execSQL(CREATE_COUNTY);		// 创建县County表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
