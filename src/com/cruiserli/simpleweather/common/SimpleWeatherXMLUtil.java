// ���й����������ص�ʡ���С���/����Ϣxml����������
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
	
	// ��XML�ַ����н�����ʡ����Ϣ�����������list��
	public static List<Province> getProvincesFromResponseXML(String responseXml){
		List<Province> list = new ArrayList<Province>();
		
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new StringReader(responseXml));
			int eventType = xmlPullParser.getEventType();
			
			Province province = null;
			while (eventType != xmlPullParser.END_DOCUMENT){
				// ��ȡ��ǰ�ڵ�����
				String nodeName = xmlPullParser.getName();
				switch (eventType) {
				// ��ʼ��жĳ���ڵ�
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
	
	// ��XML�ַ����н�����������Ϣ�����������list��
		public static List<City> getCitiesFromResponseXML(String responseXml){
			List<City> list = new ArrayList<City>();
			
			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(responseXml));
				int eventType = xmlPullParser.getEventType();
				
				City city = null;
				while (eventType != xmlPullParser.END_DOCUMENT){
					// ��ȡ��ǰ�ڵ�����
					String nodeName = xmlPullParser.getName();
					switch (eventType) {
					// ��ʼ��жĳ���ڵ�
					case XmlPullParser.START_TAG:
						if ("city".equals(nodeName))
						{
							city = new City();
							city.setCityName(xmlPullParser.getAttributeValue(2));
							city.setCityCode(xmlPullParser.getAttributeValue(5));
							// ����ֻ�������ʡ��¼֮��Ż����ʡId�������ݲ�����
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
		
		// ��XML�ַ����н�����������Ϣ�����������list��
		public static List<County> getCountiesFromResponseXML(String responseXml){
			List<County> list = new ArrayList<County>();
			
			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(responseXml));
				int eventType = xmlPullParser.getEventType();
				
				County county = null;
				while (eventType != xmlPullParser.END_DOCUMENT){
					// ��ȡ��ǰ�ڵ�����
					String nodeName = xmlPullParser.getName();
					switch (eventType) {
					// ��ʼ��жĳ���ڵ�
					case XmlPullParser.START_TAG:
						if ("city".equals(nodeName))
						{
							county = new County();
							county.setCountyName(xmlPullParser.getAttributeValue(2));
							county.setCountyCode(xmlPullParser.getAttributeValue(5));
							// ����ֻ������˳��м�¼֮��Ż������Id�������ݲ�����
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
