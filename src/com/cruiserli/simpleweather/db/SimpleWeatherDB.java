// ��װ���õ����ݿ����

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

	// ���ݿ���
	public static final String DB_NAME = "simple_weather";
	
	// ���ݿ�汾
	public static final int DB_VERSION = 1;
	
	private static SimpleWeatherDB simpleWeatherDB;
	
	private SQLiteDatabase db;
	
	// ˽�л����캯�����Ա�ʵ�ֵ�ʵ��
	private SimpleWeatherDB(Context context){
		SimpleWeatherOpenHelper dbHelper = new SimpleWeatherOpenHelper(context, DB_NAME, 
						null, DB_VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	// ��̬������ȡSimpleWeatherDBʵ��
	public synchronized static SimpleWeatherDB getInstance(Context context){
		if (simpleWeatherDB == null){
			simpleWeatherDB = new SimpleWeatherDB(context);
		}
		return simpleWeatherDB;
	}
	
	// ��ʼ����
	public void beginTransaction(){
		db.beginTransaction();
	}
	
	// �ύ����
	public void commitTransaction(){
		db.setTransactionSuccessful();
	}
	
	// ��������
	public void endTransaction(){
		db.endTransaction();
	}
	
	// ��Provinceʵ����ӵ����ݿ�
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
	
	// �����ݿ��ж�ȡȫ�����������Ϣ
	public List<Province> queryAllProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		// ����»᲻��©��һ�����Ƿ���Ҫ��moveToFirst()
		while (cursor.moveToNext()) {
			Province province = new Province();
			province.setId(cursor.getInt(cursor.getColumnIndex("id")));
			province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
			province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
			list.add(province);
		}
		return list;
	}
	
	// ��Cityʵ�����浽���ݿ���
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
	
	// ��ȡĳ��ʡ�����еĳ�����Ϣ
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
	
	// ��County��/��ʵ���浽���ݿ���
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
	
	// �����ݿ��ȡ����id�����µ�������Ϣ
	public List<County> queryCountyByCityId(int cityId){
		List<County> list = new ArrayList<County>();
		// ���ֱ��ʹ��sql����ѯ
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
