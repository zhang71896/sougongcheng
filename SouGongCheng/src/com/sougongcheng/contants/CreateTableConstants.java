package com.sougongcheng.contants;

public class CreateTableConstants {

	
	public static final String NEWS_TABLE="CREATE TABLE news(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,"//�������
			+"news_title TEXT NOT NULL,"//���ű���
			+ "news_author TEXT NOT NULL,"//��������
			+ "start_time TEXT NOT NULL,"//��ʼʱ��
			+ "end_time TEXT NOT NULL,"//����ʱ��
			+ "news_type TEXT NOT NULL,"//����
			+ "heart INTEGER NOT NULL,"//ϲ����
			+"label Text NOT NULL)";//��ǩ
	
}
