package com.sougongcheng.util;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
public class GetShareDatas{
	  public static GetShareDatas instance = null;  
	private SharedPreferences sharedPreferences; 
	private Editor editor;
		private GetShareDatas (String shareName,Activity mActivity)
		{
		
			sharedPreferences = mActivity.getSharedPreferences(shareName, Context.MODE_PRIVATE);
			editor=sharedPreferences.edit();
			
		}
		public static final GetShareDatas getInstance(String shareName,Activity mActivity)
		{
			if(instance==null)
			{
				instance=new GetShareDatas(shareName,mActivity);
			}
			return instance;
		}
		public void insertStringMessage(String key,String value)
		{
			editor.putString(key,value);
			editor.commit();
		}
		public void insertIntMessage(String key,int value)
		{
			editor.putInt(key,value);
			editor.commit();
		}
		public void insertBooleanMessage(String key,boolean value)
		{
			editor.putBoolean(key, value);
			editor.commit();
		}
		public boolean getBooleanMessage(String key,boolean value)
		{
			return sharedPreferences.getBoolean(key, value);
		}
		public String getStringMessage(String key,String value)
		{
			return sharedPreferences.getString(key, value);
		}
		public int getIntMessage(String key,int value)
		{
			return sharedPreferences.getInt(key, value);
		}
		public void clearData()
		{
			editor.clear();
			editor.commit();
		}
}
