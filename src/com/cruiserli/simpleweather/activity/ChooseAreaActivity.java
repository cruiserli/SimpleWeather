// ѡ��ʡ���ػ
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
	// ʡ�б�
	private List<Province> provinceList;
	// ���б�
	private List<City> cityList;
	// ���б�
	private List<County> countyList;
	
	// ѡ�е�ʡ��
	private Province selectedProvince;
	// ѡ�е���
	private City selectedCity;
	// ��ǰѡ�еļ���
	private int currentLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		simpleWeatherDB = SimpleWeatherDB.getInstance(this);
		// ��ʼ�����ݿ⣬��һ�����д��й�����վץȡʡ������Ϣ�������ű�д������
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
		
		// ����ʡ������
		queryProvince();
	}
	
	// ��ѯȫ�����е�ʡ
	private void queryProvince(){
		provinceList = simpleWeatherDB.queryAllProvinces();
		if (provinceList.size() > 0){
			// �滻��ǰ����������Դ
			dataList.clear();
			for (Province province : provinceList)
				dataList.add(province.getProvinceName());
			listViewAdapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleTextView.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
		}
	}
	
	// ��ѯѡ��ʡ�������е���
	private void queryCities(){
		cityList = simpleWeatherDB.queryCitiesByProvinceId(selectedProvince.getId());
		if (cityList.size() > 0){
			// ��ListView����������Դ�滻Ϊ������
			dataList.clear();
			for (City city : cityList)
				dataList.add(city.getCityName());
			listViewAdapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleTextView.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		}
	}
	
	// ��ѯѡ����������������Ϣ
	private void queryCounties(){
		countyList = simpleWeatherDB.queryCountyByCityId(selectedCity.getId());
		if (countyList.size() > 0){
			// �滻����������ԴΪ������
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
	 * ����Back���������ݵ�ǰ�����жϣ�Ӧ�÷������б�ʡ�б�����ֱ���˳�
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
