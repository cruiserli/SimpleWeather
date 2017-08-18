// ��ʾ���ȶԻ���
package com.cruiserli.simpleweather.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ShowProgressDialog {

	private static ProgressDialog progressDialog;
	
	public static void show(Context context){
		if (progressDialog == null){
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	public static void close(){
		if (progressDialog != null)
			progressDialog.dismiss();
	}
}