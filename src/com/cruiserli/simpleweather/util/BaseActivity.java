// ���л����

package com.cruiserli.simpleweather.util;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Log.d("BaseActivity::onCreate()", this.getClass().toString());
		ActivityCollector.addActivity(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		Log.d("BaseActivity::onDestroy()", this.getClass().toString());
		ActivityCollector.removeActivity(this);
	}
}
