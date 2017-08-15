/*
 * Http请求公共类
 * 
 */

package com.cruiserli.simpleweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.LinearGradient;

public class HttpUtil {
	
	// 发送Http请求
	public static void sendHttpRequest(final String urlAddress, 
			final HttpCallbackListener listener)
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try 
				{
					URL url = new URL(urlAddress);
					connection = (HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					connection.setDoInput(true);
					connection.setDoOutput(true);
					
					InputStream inputStream = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					
					if (listener != null){
						// 回调函数的onFinish()方法
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener != null){
						// 回调onError()方法
						listener.onError(e);
					}
				} finally {
					if (connection != null)
						connection.disconnect();
				}
			}
		}).start();
	}
}
