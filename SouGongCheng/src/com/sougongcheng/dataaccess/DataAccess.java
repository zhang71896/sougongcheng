package com.sougongcheng.dataaccess;

import java.util.ArrayList;
import java.util.Map;

import com.sougongcheng.contants.CreateTableConstants;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.sqlite.DataBaseManager;

import android.content.ContentValues;
import android.content.Context;


public class DataAccess {
	
	private DataBaseManager dbBaseManager;
	
	private ArrayList<Map<String,Object>> mMapList;

	private long reuslt=0;
	
	public DataAccess(Context context)
	{
		dbBaseManager=DataBaseManager.getInstance(context);
	}
	
	public long insertNews(Map<String,Object> mMaps)
	{
		ContentValues values=new ContentValues();
		values.put(MConstants.END_TIME,mMaps.get(MConstants.END_TIME).toString());
		values.put(MConstants.NEWS_AUTHOR,mMaps.get(MConstants.NEWS_AUTHOR).toString());
		values.put(MConstants.NEWS_TITLE,mMaps.get(MConstants.NEWS_TITLE).toString());
		values.put(MConstants.NEWS_TYPE,mMaps.get(MConstants.NEWS_TYPE).toString());
		values.put(MConstants.END_TIME,mMaps.get(MConstants.START_TIME).toString());
		//reuslt=dbBaseManager.insertData(CreateTableConstants.NEWS_TABLE, values);	
		return reuslt; 
	}
}
