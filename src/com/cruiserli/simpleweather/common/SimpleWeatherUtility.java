// 该app一些方法，比如初始化省市县数据

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
	 * 分别从中国气象网获取省市区xml，并解析存入数据库省、市、区表中
	 * 由于不知道该网站的省市区信息数据，所以分了很多次，从预报表中获取；
	 * 当然第一次抓取了以后，可以把数据保存下来，以后不要这样抓取了；
	 * simpleWeatherDB:数据库实例
	 * response：通过http请求返回的response数据，里面包含所有省xml数据
	 * url地址：http://flash.weather.com.cn/wmaps/xml/china.xml
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
					// 从中国气象网获取各省信息XML数据
					String provinceResponse = HttpUtil.sendHttpRequestDirectly("http://flash.weather.com.cn/wmaps/xml/china.xml");
					
					// 解析省份XML数据并放入Province对象列表中
					List<Province> provinceList = SimpleWeatherXMLUtil.getProvincesFromResponseXML(provinceResponse);
					
					// 写入Province表
					for (int i = 0; i < provinceList.size(); i++){
						Province province = provinceList.get(i);
						long insertedProvinceId = simpleWeatherDB.inertProvince(province);
						if (insertedProvinceId == -1)
							throw new Exception();
						
						// 依次获取每个省份下所有城市信息			
						String provinceCode = provinceList.get(i).getProvinceCode();
						String cityResponse = HttpUtil.sendHttpRequestDirectly("http://flash.weather.com.cn/wmaps/xml/" 
								+ provinceCode + ".xml");
						List<City> provinceCityList = SimpleWeatherXMLUtil.getCitiesFromResponseXML(cityResponse);
						// 写入City表
						for (int j = 0; j < provinceCityList.size(); j++){
							City city = provinceCityList.get(j);
							city.setProvinceId((int)insertedProvinceId);
							long insertedCityId = simpleWeatherDB.insertCity(city);
							if (insertedCityId == -1)
								throw new Exception();
							
							// 依次获取每个市所有区县信息
							String cityCode = city.getCityCode();
							String countyResponse = HttpUtil.sendHttpRequestDirectly("http://flash.weather.com.cn/wmaps/xml/" 
								+ cityCode + ".xml");
							List<County> countyList = SimpleWeatherXMLUtil.getCountiesFromResponseXML(countyResponse);
							// 写入区县表
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
