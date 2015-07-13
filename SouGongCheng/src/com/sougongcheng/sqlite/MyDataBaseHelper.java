package com.sougongcheng.sqlite;



import com.sougongcheng.contants.CreateTableConstants;
import com.sougongcheng.contants.MConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBaseHelper extends SQLiteOpenHelper{

	public MyDataBaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	@SuppressWarnings("static-access")
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	    String RECOMMEND_TABLE=CreateTableConstants.RECOMMEND_TABLE;
	    String COMMENT_TABLE=CreateTableConstants.COMMENT_TABLE;
	    String SEARCH_MACHINE_TABLE=CreateTableConstants.SEARCH_MACHINE_TABLE;
	    db.execSQL(RECOMMEND_TABLE);
	    db.execSQL(COMMENT_TABLE);
	    db.execSQL(SEARCH_MACHINE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
