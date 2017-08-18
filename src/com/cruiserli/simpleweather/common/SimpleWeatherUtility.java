// ��appһЩ�����������ʼ��ʡ��������

package com.cruiserli.simpleweather.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.R.integer;
import android.content.Context;
import android.text.TextUtils;

import com.cruiserli.simpleweather.db.SimpleWeatherDB;
import com.cruiserli.simpleweather.model.City;
import com.cruiserli.simpleweather.model.County;
import com.cruiserli.simpleweather.model.Province;
import com.cruiserli.simpleweather.util.BaseApplication;
import com.cruiserli.simpleweather.util.HttpUtil;

public class SimpleWeatherUtility {

	/* 
	 * �ֱ���й���������ȡʡ����xml���������������ݿ�ʡ���С�������
	 * ���ڲ�֪������վ��ʡ������Ϣ���ݣ����Է��˺ܶ�Σ���Ԥ�����л�ȡ��
	 * ��Ȼ��һ��ץȡ���Ժ󣬿��԰����ݱ����������Ժ�Ҫ����ץȡ�ˣ�
	 * simpleWeatherDB:���ݿ�ʵ��
	 * response��ͨ��http���󷵻ص�response���ݣ������������ʡxml����
	 * url��ַ��http://flash.weather.com.cn/wmaps/xml/china.xml
	 */
	
	public synchronized static void initDB(){
		
		//ShowProgressDialog.show(BaseApplication.getContext());
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Context context = BaseApplication.getContext();
				SimpleWeatherDB simpleWeatherDB = SimpleWeatherDB.getInstance(context);
				simpleWeatherDB.beginTransaction();
				try {
					// ���й���������ȡ��ʡ��ϢXML����
					String provinceResponse = HttpUtil.sendHttpRequestDirectly("http://flash.weather.com.cn/wmaps/xml/china.xml");
					
					// ����ʡ��XML���ݲ�����Province�����б���
					List<Province> provinceList = SimpleWeatherXMLUtil.getProvincesFromResponseXML(provinceResponse);
					
					// д��Province��
					for (int i = 0; i < provinceList.size(); i++){
						Province province = provinceList.get(i);
						long insertedProvinceId = simpleWeatherDB.inertProvince(province);
						if (insertedProvinceId == -1)
							throw new Exception();
						
						// ���λ�ȡÿ��ʡ�������г�����Ϣ			
						String provinceCode = provinceList.get(i).getProvinceCode();
						String cityResponse = HttpUtil.sendHttpRequestDirectly("http://flash.weather.com.cn/wmaps/xml/" 
								+ provinceCode + ".xml");
						List<City> provinceCityList = SimpleWeatherXMLUtil.getCitiesFromResponseXML(cityResponse);
						// д��City��
						for (int j = 0; j < provinceCityList.size(); j++){
							City city = provinceCityList.get(j);
							city.setProvinceId((int)insertedProvinceId);
							long insertedCityId = simpleWeatherDB.insertCity(city);
							if (insertedCityId == -1)
								throw new Exception();
							
							// ���λ�ȡÿ��������������Ϣ
							String cityCode = city.getCityCode();
							String countyResponse = HttpUtil.sendHttpRequestDirectly("http://flash.weather.com.cn/wmaps/xml/" 
								+ cityCode + ".xml");
							List<County> countyList = SimpleWeatherXMLUtil.getCountiesFromResponseXML(countyResponse);
							// д�����ر�
							for (int k = 0; k < countyList.size(); k++) {
								County county = countyList.get(k);
								county.setCityId((int)insertedCityId);
								long insertedCountyId = simpleWeatherDB.insertCounty(county);
								if (insertedCountyId == -1)
									throw new Exception();
							}
						}
					}
					
					simpleWeatherDB.commitTransaction();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					simpleWeatherDB.endTransaction();
					//ShowProgressDialog.close();
				}
			}
		}).start();
		
	}
}
