// 选择省市县活动
package com.cruiserli.simpleweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.cruiserli.simpleweather.common.SimpleWeatherUtility;
import com.cruiserli.simpleweather.db.SimpleWeatherDB;
import com.cruiserli.simpleweather.model.City;
import com.cruiserli.simpleweather.model.County;
import com.cruiserli.simpleweather.model.Province;
import com.cruiserli.simpleweather.util.BaseActivity;

import com.cruiserli.simpleweather.R;

import android.R.anim;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.DownloadManager.Query;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseAreaActivity extends BaseActivity {

	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	
	private ProgressDialog progressDialog;
	private TextView titleTextView;
	private ListView listView;
	private ArrayAdapter<String> listViewAdapter;
	private SimpleWeatherDB simpleWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	// 省列表
	private List<Province> provinceList;
	// 市列表
	private List<City> cityList;
	// 县列表
	private List<County> countyList;
	
	// 选中的省份
	private Province selectedProvince;
	// 选中的市
	private City selectedCity;
	// 当前选中的级别
	private int currentLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		simpleWeatherDB = SimpleWeatherDB.getInstance(this);
		// 初始化数据库，第一次运行从中国气象站抓取省市县信息以往三张表写入数据
		//SimpleWeatherUtility.initDB();

		listView = (ListView)findViewById(R.id.ListView);
		titleTextView = (TextView)findViewById(R.id.TitleTextView);
		
		listViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				dataList);
		listView.setAdapter(listViewAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (currentLevel == LEVEL_PROVINCE){
					selectedProvince = provinceList.get(position);
					queryCities();
				} else if (currentLevel == LEVEL_CITY){
					selectedCity = cityList.get(position);
					queryCounties();
				}
			}
		});
		
		// 加载省份数据
		queryProvince();
	}
	
	// 查询全国所有的省
	private void queryProvince(){
		provinceList = simpleWeatherDB.queryAllProvinces();
		if (provinceList.size() > 0){
			// 替换当前适配器数据源
			dataList.clear();
			for (Province province : provinceList)
				dataList.add(province.getProvinceName());
			listViewAdapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleTextView.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		}
	}
	
	// 查询选中省下面所有的市
	private void queryCities(){
		cityList = simpleWeatherDB.queryCitiesByProvinceId(selectedProvince.getId());
		if (cityList.size() > 0){
			// 将ListView适配器数据源替换为市数据
			dataList.clear();
			for (City city : cityList)
				dataList.add(city.getCityName());
			listViewAdapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleTextView.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		}
	}
	
	// 查询选中市下面所有县信息
	private void queryCounties(){
		countyList = simpleWeatherDB.queryCountyByCityId(selectedCity.getId());
		if (countyList.size() > 0){
			// 替换适配器数据源为县数据
			dataList.clear();
			for (County county : countyList)
				dataList.add(county.getCountyName());
			listViewAdapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleTextView.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		}
	}
	
	/*
	 * 捕获Back按键，根据当前级别判断，应该返回市列表，省列表，还是直接退出
	 * 
	 */
	@Override
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTY)
			queryCities();
		else if (currentLevel == LEVEL_CITY)
			queryProvince();
		else 
			finish();
	}
}
