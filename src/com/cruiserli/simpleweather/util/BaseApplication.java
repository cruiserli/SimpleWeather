/*
 * �Ա���������ʱ�򱣴�context���Ա�����ȫ���κεط���ȡ��
 * ʹ���г����޸ļ̳й�ϵ�⣬����Ҫ�޸�AndroidManifest.xml�ļ���
 * <applicaton android:name="����Ϊ��������"
 * */
package com.cruiserli.simpleweather.util;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {

	private static Context context;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = getApplicationContext();
	}
	
	public static Context getContext(){
		return context;
	}
}
