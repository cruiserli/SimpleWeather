// 封装常用的数据库操作

package com.cruiserli.simpleweather.db;

import java.util.ArrayList;
import java.util.List;

import com.cruiserli.simpleweather.model.City;
import com.cruiserli.simpleweather.model.County;
import com.cruiserli.simpleweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SimpleWeatherDB {

	// 数据库名
	public static final String DB_NAME = "simple_weather";
	
	// 数据库版本
	public static final int DB_VERSION = 1;
	
	private static SimpleWeatherDB simpleWeatherDB;
	
	private SQLiteDatabase db;
	
	// 私有化构造函数，以便实现单实例
	private SimpleWeatherDB(Context context){
		SimpleWeatherOpenHelper dbHelper = new SimpleWeatherOpenHelper(context, DB_NAME, 
						null, DB_VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	// 静态方法获取SimpleWeatherDB实例
	public synchronized static SimpleWeatherDB getInstance(Context context){
		if (simpleWeatherDB == null){
			simpleWeatherDB = new SimpleWeatherDB(context);
		}
		return simpleWeatherDB;
	}
	
	// 开始事务
	public void beginTransaction(){
		db.beginTransaction();
	}
	
	// 提交事务
	public void commitTransaction(){
		db.setTransactionSuccessful();
	}
	
	// 结束事务
	public void endTransaction(){
		db.endTransaction();
	}
	
	// 将Province实例添加到数据库
	public long inertProvince(Province province){
		long insertedId = -1;
		if (province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			insertedId = db.insert("Province", null, values);
		}
		return insertedId;
	}
	
	// 从数据库中读取全国所有身份信息
	public List<Province> queryAllProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		// 检查下会不会漏掉一条，是否需要先moveToFirst()
		while (cursor.moveToNext()) {
			Province province = new Province();
			province.setId(cursor.getInt(cursor.getColumnIndex("id")));
			province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
			province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
			list.add(province);
		}
		return list;
	}
	
	// 将City实例保存到数据库中
	public long insertCity(City city){
		long insertedId = -1;
		if (city != null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			insertedId = db.insert("City", null, values);
		}
		return insertedId;
	}
	
	// 读取某个省下所有的城市信息
	public List<City> queryCitiesByProvinceId(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id=?", new String[]{String.valueOf(provinceId)}, 
				null, null, null);
		while (cursor.moveToNext()) {
			City city = new City();
			city.setId(cursor.getInt(cursor.getColumnIndex("id")));
			city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
			city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
			city.setProvinceId(provinceId);
			list.add(city);
		}
		return list;
	}
	
	// 将County县/区实例存到数据库中
	public long insertCounty(County county){
		long insertedId = -1;
		if (county != null){
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			insertedId = db.insertOrThrow("County", null, values);
		}
		return insertedId;
	}
	
	// 从数据库读取城市id所有下的县区信息
	public List<County> queryCountyByCityId(int cityId){
		List<County> list = new ArrayList<County>();
		// 这次直接使用sql语句查询
		Cursor cursor = db.rawQuery("Select * from County Where city_id=?", 
				new String[]{String.valueOf(cityId)});
		while (cursor.moveToNext()) {
			County county = new County();
			county.setId(cursor.getInt(cursor.getColumnIndex("id")));
			county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
			county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
			county.setCityId(cityId);
			list.add(county);
		}
		return list;
	}
}
