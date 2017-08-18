// 从中国天气网返回的省、市、县/区信息xml解析工具类
package com.cruiserli.simpleweather.common;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.R.integer;

import com.cruiserli.simpleweather.model.City;
import com.cruiserli.simpleweather.model.County;
import com.cruiserli.simpleweather.model.Province;
import com.cruiserli.simpleweather.util.LogUtil;

public class SimpleWeatherXMLUtil {
	
	// 从XML字符串中解析出省份信息，并存入对象list中
	public static List<Province> getProvincesFromResponseXML(String responseXml){
		List<Province> list = new ArrayList<Province>();
		
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new StringReader(responseXml));
			int eventType = xmlPullParser.getEventType();
			
			Province province = null;
			while (eventType != xmlPullParser.END_DOCUMENT){
				// 获取当前节点名称
				String nodeName = xmlPullParser.getName();
				switch (eventType) {
				// 开始接卸某个节点
				case XmlPullParser.START_TAG:
					if ("city".equals(nodeName))
					{
						province = new Province();
						province.setProvinceName(xmlPullParser.getAttributeValue(0));
						province.setProvinceCode(xmlPullParser.getAttributeValue(1));
					}
					break;
				case XmlPullParser.END_TAG:
					if ("city".equals(nodeName))
						list.add(province);
					break;
				default:
					break;
				}
				eventType = xmlPullParser.next();
			}
		} catch (Exception e) {
			LogUtil.e("SimpleWeatherXMLUtil", e.getMessage());
		}
		
		return list;
	}
	
	// 从XML字符串中解析出城市信息，并存入对象list中
		public static List<City> getCitiesFromResponseXML(String responseXml){
			List<City> list = new ArrayList<City>();
			
			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(responseXml));
				int eventType = xmlPullParser.getEventType();
				
				City city = null;
				while (eventType != xmlPullParser.END_DOCUMENT){
					// 获取当前节点名称
					String nodeName = xmlPullParser.getName();
					switch (eventType) {
					// 开始接卸某个节点
					case XmlPullParser.START_TAG:
						if ("city".equals(nodeName))
						{
							city = new City();
							city.setCityName(xmlPullParser.getAttributeValue(2));
							city.setCityCode(xmlPullParser.getAttributeValue(5));
							// 由于只有添加了省记录之后才会产生省Id，这里暂不设置
						}
						break;
					case XmlPullParser.END_TAG:
						if ("city".equals(nodeName))
							list.add(city);
						break;
					default:
						break;
					}
					eventType = xmlPullParser.next();
				}
			} catch (Exception e) {
				LogUtil.e("SimpleWeatherXMLUtil", e.getMessage());
			}
			
			return list;
		}
		
		// 从XML字符串中解析出区县信息，并存入对象list中
		public static List<County> getCountiesFromResponseXML(String responseXml){
			List<County> list = new ArrayList<County>();
			
			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(responseXml));
				int eventType = xmlPullParser.getEventType();
				
				County county = null;
				while (eventType != xmlPullParser.END_DOCUMENT){
					// 获取当前节点名称
					String nodeName = xmlPullParser.getName();
					switch (eventType) {
					// 开始接卸某个节点
					case XmlPullParser.START_TAG:
						if ("city".equals(nodeName))
						{
							county = new County();
							county.setCountyName(xmlPullParser.getAttributeValue(2));
							county.setCountyCode(xmlPullParser.getAttributeValue(5));
							// 由于只有添加了城市记录之后才会产生市Id，这里暂不设置
						}
						break;
					case XmlPullParser.END_TAG:
						if ("city".equals(nodeName))
							list.add(county);
						break;
					default:
						break;
					}
					eventType = xmlPullParser.next();
				}
			} catch (Exception e) {
				LogUtil.e("SimpleWeatherXMLUtil", e.getMessage());
			}
			
			return list;
		}
}
