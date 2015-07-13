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
		values.put(MConstants.RECOMEND_ITEMS_TAGS,mMaps.get(MConstants.RECOMEND_ITEMS_TAGS).toString());
		values.put(MConstants.RECOMEND_ITEMS_ID,mMaps.get(MConstants.RECOMEND_ITEMS_ID).toString());
		values.put(MConstants.RECOMEND_ITEMS_title,mMaps.get(MConstants.RECOMEND_ITEMS_title).toString());
		values.put(MConstants.RECOMEND_ITEMS_START_TIME,mMaps.get(MConstants.RECOMEND_ITEMS_START_TIME).toString());
		values.put(MConstants.RECOMEND_ITEMS_END_TIME,mMaps.get(MConstants.RECOMEND_ITEMS_END_TIME).toString());
		values.put(MConstants.RECOMEND_ITEMS_AUTHORS,mMaps.get(MConstants.RECOMEND_ITEMS_AUTHORS).toString());
		values.put(MConstants.RECOMEND_ITEMS_STORE,mMaps.get(MConstants.RECOMEND_ITEMS_STORE).toString());
		values.put(MConstants.RECOMEND_ITEMS_TYPE,mMaps.get(MConstants.RECOMEND_ITEMS_TYPE).toString());
		reuslt=dbBaseManager.insertData(CreateTableConstants.RECOMMEND_TABLE_NAME, values);	
		return reuslt; 
	}
}
