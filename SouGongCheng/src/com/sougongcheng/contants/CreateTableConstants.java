package com.sougongcheng.contants;

public class CreateTableConstants {

	
	
	public static final String RECOMMEND_TABLE="CREATE TABLE recommend(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,"+
			"access_token TEXT NOT NULL,"+//access_token
			"recomend_items_tags_nums TEXT NOT NULL," +//标签tag的数量
			"recomend_items_id TEXT NOT NULL," +//标签id	
			"recomend_items_title TEXT NOT NULL,"+//标签标题
			"recomend_items_start_time TEXT NOT NULL," +//项目开始时间
			"recomend_items_end_time TEXT NOT NULL,"+//项目结束时间
			"recomend_items_authors TEXT NOT NULL,"+//项目发布者
			"recomend_items_store TEXT NOT NULL," +//项目收藏
			"recomend_items_type TEXT NOT NULL,"+//项目类型
			"recommend_banners_imageurl TEXT NOT NULL," +//图片url
			"recommend_banners_gotourl TEXT NOT NULL)";//图片指向url
	
	public static final String COMMENT_TABLE="CREATE TABLE comment(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,"+
			"access_token TEXT NOT NULL,"+//access_token
			"comments_id TEXT NOT NULL," +//标签tag的数量
			"comments_user_id TEXT NOT NULL," +//标签id	
			"comments_user_name TEXT NOT NULL,"+//标签标题
			"comments_like_nums TEXT NOT NULL," +//项目开始时间
			"comments_share_nums TEXT NOT NULL,"+//项目结束时间
			"comments_contents TEXT NOT NULL,"+//项目发布者
			"comments_create_time TEXT NOT NULL," +//项目收藏
			"comments_is_like TEXT NOT NULL,"+//项目类型
			"comments_user_sex TEXT NOT NULL," +//图片url
			"comments_is_store TEXT NOT NULL)";//图片指向url
	
	public static final String SEARCH_MACHINE_TABLE="CREATE TABLE search_machine_table(id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,"+
			"access_token TEXT NOT NULL,"+//access_token
			"search_machine_id TEXT NOT NULL," +//标签tag的数量
			"search_user_id TEXT NOT NULL," +//标签id	
			"search_bind_type TEXT NOT NULL,"+//标签标题
			"search_bind_type_num TEXT NOT NULL," +//项目开始时间
			"search_bind_area TEXT NOT NULL,"+//项目结束时间
			"search_bind_area_num TEXT NOT NULL,"+//项目发布者
			"search_bind_indu TEXT NOT NULL," +//项目收藏
			"search_bind_indu_num TEXT NOT NULL,"+//项目类型
			"search_bind_keywords TEXT NOT NULL," +//图片url
			"search_bind_keywords_num TEXT NOT NULL," +//图片url
			"search_bind_is_used TEXT NOT NULL)";//图片指向url
			
}
