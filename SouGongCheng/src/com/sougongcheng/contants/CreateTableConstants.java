package com.sougongcheng.contants;

public class CreateTableConstants {

	
	public static final String NEWS_TABLE="CREATE TABLE news(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,"//自增序号
			+"news_title TEXT NOT NULL,"//新闻标题
			+ "news_author TEXT NOT NULL,"//新闻作者
			+ "start_time TEXT NOT NULL,"//起始时间
			+ "end_time TEXT NOT NULL,"//结束时间
			+ "news_type TEXT NOT NULL,"//类型
			+ "heart INTEGER NOT NULL,"//喜欢否
			+"label Text NOT NULL)";//标签
	
}
