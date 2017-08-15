/*
 * Http请求完成回调函数
 * 
 * 注意：由于onFinish()和onError()方法是在创建的子线程中运行的，所以不能
 * 在这里执行任何UI操作，依然需要用Message-Handler异步消息处理机制来处理UI更新；
 */
package com.cruiserli.simpleweather.util;

public interface HttpCallbackListener {
	
	// Http请求完成回调函数
	void onFinish(String response);
	
	// Http请求出错回调函数
	void onError(Exception e);
}
