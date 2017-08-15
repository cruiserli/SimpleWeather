// 用于管理所有的活动，所有活动创建都将添加到这个容器中
// 由于是一个工具类，所有成员和方法都是static类型的
package com.cruiserli.simpleweather.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.widget.ListView;

public class ActivityCollector {
	public static List<Activity> activities = new ArrayList<Activity>();
	
	public static void addActivity(Activity activity){
		if (!activities.contains(activity))
			activities.add(activity);
	}
	
	public static void removeActivity(Activity activity){
		activities.remove(activity);
	}
	
	public static void finishAll(){
		for (Activity activity : activities){
			if (!activity.isFinishing()){
				activity.finish();
			}
		}
	}
}
