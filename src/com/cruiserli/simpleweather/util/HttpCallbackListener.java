/*
 * Http������ɻص�����
 * 
 * ע�⣺����onFinish()��onError()�������ڴ��������߳������еģ����Բ���
 * ������ִ���κ�UI��������Ȼ��Ҫ��Message-Handler�첽��Ϣ�������������UI���£�
 */
package com.cruiserli.simpleweather.util;

public interface HttpCallbackListener {
	
	// Http������ɻص�����
	void onFinish(String response);
	
	// Http�������ص�����
	void onError(Exception e);
}
