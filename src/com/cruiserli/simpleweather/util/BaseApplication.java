/*
 * 以便在启动的时候保存context，以便于在全局任何地方获取；
 * 使用中除了修改继承关系外，还需要修改AndroidManifest.xml文件的
 * <applicaton android:name="必须为完整包名"
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
