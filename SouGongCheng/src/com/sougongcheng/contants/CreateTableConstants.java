package com.sougongcheng.contants;

public class CreateTableConstants {

	
	
	public static final String RECOMMEND_TABLE="CREATE TABLE recommend(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,"+
			"access_token TEXT NOT NULL,"+//access_token
			"recomend_items_tags_nums TEXT NOT NULL," +//��ǩtag������
			"recomend_items_id TEXT NOT NULL," +//��ǩid	
			"recomend_items_title TEXT NOT NULL,"+//��ǩ����
			"recomend_items_start_time TEXT NOT NULL," +//��Ŀ��ʼʱ��
			"recomend_items_end_time TEXT NOT NULL,"+//��Ŀ����ʱ��
			"recomend_items_authors TEXT NOT NULL,"+//��Ŀ������
			"recomend_items_store TEXT NOT NULL," +//��Ŀ�ղ�
			"recomend_items_type TEXT NOT NULL,"+//��Ŀ����
			"recommend_banners_imageurl TEXT NOT NULL," +//ͼƬurl
			"recommend_banners_gotourl TEXT NOT NULL)";//ͼƬָ��url
	
	public static final String COMMENT_TABLE="CREATE TABLE comment(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,"+
			"access_token TEXT NOT NULL,"+//access_token
			"comments_id TEXT NOT NULL," +//��ǩtag������
			"comments_user_id TEXT NOT NULL," +//��ǩid	
			"comments_user_name TEXT NOT NULL,"+//��ǩ����
			"comments_like_nums TEXT NOT NULL," +//��Ŀ��ʼʱ��
			"comments_share_nums TEXT NOT NULL,"+//��Ŀ����ʱ��
			"comments_contents TEXT NOT NULL,"+//��Ŀ������
			"comments_create_time TEXT NOT NULL," +//��Ŀ�ղ�
			"comments_is_like TEXT NOT NULL,"+//��Ŀ����
			"comments_user_sex TEXT NOT NULL," +//ͼƬurl
			"comments_is_store TEXT NOT NULL)";//ͼƬָ��url
	
	public static final String SEARCH_MACHINE_TABLE="CREATE TABLE search_machine_table(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,"+
			"access_token TEXT NOT NULL,"+//access_token
			"search_machine_id TEXT NOT NULL," +//��ǩtag������
			"search_user_id TEXT NOT NULL," +//��ǩid	
			"search_bind_type TEXT NOT NULL,"+//��ǩ����
			"search_bind_type_num TEXT NOT NULL," +//��Ŀ��ʼʱ��
			"search_bind_area TEXT NOT NULL,"+//��Ŀ����ʱ��
			"search_bind_area_num TEXT NOT NULL,"+//��Ŀ������
			"search_bind_indu TEXT NOT NULL," +//��Ŀ�ղ�
			"search_bind_indu_num TEXT NOT NULL,"+//��Ŀ����
			"search_bind_keywords TEXT NOT NULL," +//ͼƬurl
			"search_bind_keywords_num TEXT NOT NULL," +//ͼƬurl
			"search_bind_is_used TEXT NOT NULL)";//ͼƬָ��url
			
}
