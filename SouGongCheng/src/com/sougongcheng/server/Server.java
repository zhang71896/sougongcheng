package com.sougongcheng.server;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.sougongcheng.bean.AccessStatus;
import com.sougongcheng.bean.CommentsInfo;
import com.sougongcheng.bean.HotKeyWordsInfo;
import com.sougongcheng.bean.RecommandInfo;
import com.sougongcheng.bean.SearchMachine;
import com.sougongcheng.bean.Status;
import com.sougongcheng.bean.UserInfo;
import com.sougongcheng.contants.MConstants;

public class Server {
	
	public static Server instance = null; 
	
	private Status status;
	
	private UserInfo userInfo;
	
	private RecommandInfo recommandInfo;
	
	private AccessStatus accessStatus;
	
	private CommentsInfo commentsInfo;
	
	private SearchMachine searchMachine;
	
	private HotKeyWordsInfo hotKeyWordsInfo;
	
	private int TIME_OUT_DELAY=2000;
	
	private Server()
	{
		
		}
	
	public static final Server getInstance() {  
	    if (instance == null)   
	        instance = new Server();  
	    return instance;  
	} 
	
	 public String  getData(String reqStr)
		{		
			 String str = "";
				try {
		            HttpGet request = new HttpGet(reqStr);
		            request.setHeader("Accept", "application/json");
		            request.setHeader("Content-type", "application/json");
		            DefaultHttpClient httpClient = new DefaultHttpClient();
		            httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, TIME_OUT_DELAY);
		            httpClient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, TIME_OUT_DELAY);// 连接超时  
		            HttpResponse response = httpClient.execute(request);
		            HttpEntity responseEntity = response.getEntity();
		            str=retrieveInputStream(responseEntity);
		    		return str;
			    } catch (Exception e) {
		            e.printStackTrace();
		    		return "err";
		        } 
		}
	
	 
	 
	 /** 
	  * 1.1返回status
	  * @param reqStr
	  * @return
	  **/
	 private Status getStatus(String reqStr)
	 {
		 String str = "";
		 str=getData(reqStr);
		 status=new Status();
         if(str.equals("err"))
         {
        	 status.status=-1;
         }else
         {
		 try {
	            
	            JSONObject jsonObj = new JSONObject(str);
	            int result=Integer.parseInt(jsonObj.getString("status"));
	           
	            if(result==0)
	            {
	            	status.status=0;
	            }else
	            {
	            	status.status=result;
	            }
		    } catch (Exception e) {
	            e.printStackTrace();
	        }
         }
		 return status;
	 }
	 
	
	 /** 
	  * 1.2返回带reuslt_status
	  * @param reqStr
	  * @return
	  **/
	 private AccessStatus getAccessStatus(String reqStr)
	 {
		 String str = "";
		 str=getData(reqStr);
         accessStatus=new AccessStatus();
		 if(str.equals("err"))
		 {
			 accessStatus.status=-1;
		 }else
		 {
		 try {
	            JSONObject jsonObj = new JSONObject(str);
	            int result=Integer.parseInt(jsonObj.getString("status"));
	            if(result==0)
	            {
	            	accessStatus.status=0;
	            }else
	            {
	            	accessStatus.status=result;
	            }
		    } catch (Exception e) {
	            e.printStackTrace();
	        }
		 }
		 return accessStatus;
		 
	 }
	 
	 private ArrayList<Map<String,Object>> getItems(JSONArray jsonItems)
	 {
		 ArrayList<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
		 try{
		 for(int i=0;i< jsonItems.length();i++)
         {
         	Map<String,Object> info=new HashMap<String, Object>();
        
         	//第一层
         	JSONArray jsonItemContents=jsonItems.getJSONArray(i);
         	for(int j=0;j<jsonItemContents.length();j++)
         	{  
         		if(j==5)
         		{
         			JSONArray jsonItemContentsTags=jsonItemContents.getJSONArray(5);
     				String str="";
         			for(int z=0;z<jsonItemContentsTags.length();z++)
         			{
	            			info.put("tag"+z, jsonItemContentsTags.get(z));
	            			str+=jsonItemContentsTags.get(z);
         			}
     				info.put(MConstants.RECOMEND_ITEMS_TAGS, str);
         		}else
         		{
	            		
	            		if(j==0)
	            		{
	            			info.put(MConstants.RECOMEND_ITEMS_ID,jsonItemContents.get(j));
	            		}else if(j==1)
	            		{
	            			info.put(MConstants.RECOMEND_ITEMS_title,jsonItemContents.get(j));
	            		}else if(j==2)
	            		{
	            			info.put(MConstants.RECOMEND_ITEMS_START_TIME, jsonItemContents.get(j));
	            		}else if(j==3)
	            		{
	            			info.put(MConstants.RECOMEND_ITEMS_END_TIME,jsonItemContents.get(j));
	            		}else if(j==4)
	            		{
	            			info.put(MConstants.RECOMEND_ITEMS_AUTHORS,jsonItemContents.get(j));
	            		}else if(j==6)
	            		{
	            			info.put(MConstants.RECOMEND_ITEMS_STORE,jsonItemContents.get(j));
	            		}else if(j==7)
	            		{
	            			info.put(MConstants.RECOMEND_ITEMS_TYPE, jsonItemContents.get(j));
	            		}
	            		
         		}
         	}
         	mapList.add(info);
         }
		 }catch(Exception e) {
	            e.printStackTrace();
		 }
		 return mapList;
	 }
	 
	 //2.1 用户注册 
	 
	 /** 
	  * 2.1 注册
	  * @param userName
	  * @param passWord
	  * @param sex
	  * @param telnum
	  * @return
	  **/
	 public AccessStatus regin(String userName,String passWord,String sex,String telnum){
		 String reqStr=MConstants.URL+"register?username="+userName+"&password="+passWord+"&sex="+sex+"&telnum="+telnum;
		 return getAccessStatus(reqStr);
	 }
	 
	 //2.3修改用户信息
	 //修改用户名
	
	 /**
	  * 2.2用户登录
	  * @param userName
	  * @param passWord
	  * @return
	  **/
	 public UserInfo login(String userName,String passWord)
	 {
		 String reqStr=MConstants.URL+"login?username="+userName+"&password="+passWord;
		 String str = "";
		 userInfo=new UserInfo();
		  str=getData(reqStr);
		  if(str.equals("err"))
		  {
			  userInfo.status=-1;
		  }else
		  {
			try {
	            JSONObject jsonObj = new JSONObject(str);
	            int result=Integer.parseInt(jsonObj.getString("status"));
	            if(result==0)
	            {
	            	userInfo.status=0;
	            	userInfo.access_token=jsonObj.getString("access_token");
	            	userInfo.sex=jsonObj.getInt("sex");
	            	userInfo.telnum=jsonObj.getString("telnum");
	            }else
	            {
	            	userInfo.status=result;
	            }
		    } catch (Exception e) {
	            e.printStackTrace();
	        }
		  }
			return userInfo;
	 }
	 
	 /**
	  * 2.3修改用户信息
	  * @param access_token
	  * @param userName
	  * @return
	  */
	 public AccessStatus modifyUserName(String access_token,String userName)
	 {
		 String reqStr=MConstants.URL+"modify_user/username?access_token="+access_token+"&username="+userName;
		 return getAccessStatus(reqStr);
	 }
	 
	 //修改密码
	 public AccessStatus modifyPassWord(String access_token,String passWord,String oldPassWord)
	 {
		 String reqStr=MConstants.URL+"modify_user/password?access_token="+access_token+"&password="+passWord+"&oldpassword="+oldPassWord;
		 return getAccessStatus(reqStr);
	 }
	 
	 //修改用户名
	 public AccessStatus modifySex(String access_token,String sex)
	 {
		 String reqStr=MConstants.URL+"modify_user/sex?access_token="+access_token+"&sex="+sex;
		 return getAccessStatus(reqStr);
	 }
	 
	 //修改电话号码
	 public AccessStatus modifyTelNum(String access_token,String telNum)
	 {
		 String reqStr=MConstants.URL+"modify_user/telnum?access_token="+access_token+"&telnum="+telNum;
		 return getAccessStatus(reqStr);
	 }

	 
	 /** *
	  * 2.4首页推荐
	  * @param access_token
	  * @param num
	  * @return
	  **/
	 public RecommandInfo getRecommandInfo(String access_token,String num)
	 {
		 String reqStr=MConstants.URL+"recommend?access_token="+access_token+"&num="+num;
		// String reqStr="http://120.25.224.229:8888/recommend?access_token=1-gonglijun&num=2";
		 String str = "";
		 recommandInfo=new RecommandInfo();
	      str=getData(reqStr);
	      if(str.equals("err"))
	      {
	    	  recommandInfo.status=-1;
	      }else
	      {
			try {
	            JSONObject jsonObj = new JSONObject(str);
	            recommandInfo.status=Integer.parseInt(jsonObj.getString("status"));
	            JSONArray jsonItems=jsonObj.getJSONArray("items");
	            recommandInfo.items=new ArrayList<Map<String,Object>>();
	            recommandInfo.items=getItems(jsonItems);
	            JSONArray jsonBanners=jsonObj.getJSONArray("banners");
	            recommandInfo.banners=new ArrayList<Map<String,Object>>();
	            for(int i=0;i<jsonBanners.length();i++)
	            {
	            	Map<String,Object> info=new HashMap<String, Object>();
	            	JSONArray jsonBannersContents=jsonBanners.getJSONArray(i);
	            	for(int j=0;j<jsonBannersContents.length();j++)
	            	{
	            		if(j==0)
	            		{
	            			info.put(MConstants.RECOMEND_BANNERS_IMAGEURL+i, jsonBannersContents.get(j));
	            		}else if(j==1)
	            		{
	            			info.put(MConstants.RECOMEND_BANNERS_GOTOURL+i, jsonBannersContents.get(j));
	            		}
	            	}
	            	recommandInfo.banners.add(info);
	            }
		    } catch (Exception e) {
	            e.printStackTrace();
	        }
	      }
			return recommandInfo;
	 }
	 
	 /** 
	  * 2.5 招标信息列表
	  * @param type
	  * @param access_token
	  * @param page_num
	  * @param offset
	  * @return
	  **/
	 public RecommandInfo getBandsInfo(String type,String access_token,String page_num,String offset)
	 {
		  String reqStr=MConstants.URL+"list/"+type+"?access_token="+access_token+"&page_num="+page_num+"&offset="+offset;
		  String str = "";
		  str=getData(reqStr);
		  recommandInfo=new RecommandInfo();
		  if(str.equals("err"))
		  {
			  recommandInfo.status=-1;  
		  }else{
			try {
	           
	            JSONObject jsonObj = new JSONObject(str);
	            recommandInfo.status=Integer.parseInt(jsonObj.getString("status"));
	            recommandInfo.items=new ArrayList<Map<String,Object>>();
	            JSONArray jsonItems=jsonObj.getJSONArray("items");
	            recommandInfo.items=getItems(jsonItems);
			 } catch (Exception e) {
		            e.printStackTrace();
		        }
		  }
		 return recommandInfo;
	 }
	 
	 /** 
	  * 2.6招标信息详情
	  * @param access_token
	  * @param type
	  * @param id
	  * @return
	  **/
	 public String getBandsInfoDetail(String access_token,String type,String id)
	 {
		 String reqStr=MConstants.URL+"item?access_token="+access_token+"&type="+type+"&id="+id;
		 return reqStr;
	 }
 //把传送的实体转换成为字符串,再用jason解析出来
	 
	 /** 
	  * 2.7招标信息收藏
	  * @param state
	  * @param access_token
	  * @param type
	  * @param id
	  * @return
	  **/
	 public Status StoreBandsInfo(String state,String access_token,String type,String id)
	 {
		 String reqStr=MConstants.URL+"favorite/"+state+"?access_token="+access_token+"&type="+type+"&id="+id;
		 return getStatus(reqStr);
	 }
	 /** *
	  * 2.8 获取评论
	  * @param type favorite/all/user
	  * @param access_token
	  * @param page_num
	  * @param offset
	  * @return
	  */
	 public CommentsInfo getCommments(String type,String access_token,String page_num,String offset,String parent_id)
	 {
		 //http://120.25.224.229:8888/get_comment/favorite?access_token=1-gonglijun&page_num=10&offset=0
		 String reqStr=MConstants.URL+"get_comment/"+type+"?access_token="+access_token+"&page_num="+page_num+"&offset="+offset+"&parent_id="+parent_id;
		 String str = "";
		 str=getData(reqStr);
		 commentsInfo=new CommentsInfo();
		 if(str.equals("err"))
		 {
			 commentsInfo.status=-1; 
		 }else
		 {
         try {
			JSONObject jsonObj = new JSONObject(str);
			int result=Integer.parseInt(jsonObj.getString("status"));
			if(result==0)
			{
			commentsInfo.status=result;
            JSONArray jsonComments=jsonObj.getJSONArray("comments");
            commentsInfo.comments=new ArrayList<Map<String,Object>>();
            for(int i=0;i<jsonComments.length();i++)
            {
            	Map<String,Object> info=new HashMap<String, Object>();
            	JSONArray jsonCommentsContents=jsonComments.getJSONArray(i);
            	for(int j=0;j<jsonCommentsContents.length();j++)
            	{
            		if(j==0)
            		{
            			info.put(MConstants.COMMENTS_ID, jsonCommentsContents.get(j));
            		}else if(j==1)
            		{
            			info.put(MConstants.COMMENTS_USER_ID, jsonCommentsContents.get(j));
            		}else if(j==2)
            		{
            			info.put(MConstants.COMMENTS_USER_NAME, jsonCommentsContents.get(j));
            		}else if(j==3)
            		{
            			info.put(MConstants.COMMENTS_LIKE_NUMS, jsonCommentsContents.get(j));
            		}else if(j==4)
            		{
            			info.put(MConstants.COMMENTS_SHARE_NUMS, jsonCommentsContents.get(j));
            		}else if(j==5)
            		{
            			info.put(MConstants.COMMENTS_CONTENTS, jsonCommentsContents.get(j));
            		}else if(j==6)
            		{
            			info.put(MConstants.COMMENTS_CREATE_TIME, jsonCommentsContents.get(j));
            		}else if(j==7)
            		{
            			info.put(MConstants.COMMENTS_IS_LIKE, jsonCommentsContents.get(j));
            		}else if(j==8)
            		{
            			info.put(MConstants.COMMENTS_USER_SEX, jsonCommentsContents.get(j));
            		}else if(j==9)
            		{
            			info.put(MConstants.COMMENTS_IS_STORE, jsonCommentsContents.get(j));
            		}
            	}
            	commentsInfo.comments.add(info);
            }

			}else
			{
			commentsInfo.status=result;	
			}

			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 }
         return commentsInfo;
	 }
	 
	 /** 
	  * 2.9评论管理
	  * @param type add/del
	  * @param access_token
	  * @param content
	  * @param parent_id
	  * @return
	  */
	 public Status manageComment(String type,String access_token,String content,String parent_id)
	 {
		 String reqStr=MConstants.URL+"comment/"+type+"?access_token="+access_token+"&content="+content+"&parent_id="+parent_id;
		 status=getStatus(reqStr);
		 return status;
	 }
	 
	 /** *
	  * 2.10评论点赞
	  * @param type add/del
	  * @param access_token
	  * @param id
	  * @return
	  */
	 public Status likeComment(String type,String access_token,String id)
	 {
		 String reqStr=MConstants.URL+"comment_like_num/"+type+"?access_token="+access_token+"&id="+id;
		 status=getStatus(reqStr);
		 return status;
	 }
	 
	 /** *
	  * 2.11评论分享
	  * @param access_token
	  * @param id
	  * @return
	  */
	 public Status shareComment(String access_token,String id)
	 {
		 String reqStr=MConstants.URL+"comment_forward_num/?access_token="+access_token+"&id="+id;
		 status=getStatus(reqStr);
		 return status;
	 }
	 
	 /** *
	  * 2.12 评论收藏
	  * @param type
	  * @param access_token
	  * @param id
	  * @return
	  */
	 public Status StoreComment(String type,String access_token,String id)
	 {
		 String reqStr=MConstants.URL+"comment_favorite/"+type+"?access_token="+access_token+"&id="+id;
		 status=getStatus(reqStr);
		 return status;
	 }
	 
	 /** 
	  * 2.13关键字搜索
	  * @param type
	  * @param access_token
	  * @param keywords
	  * @param page_num
	  * @param offset
	  * @return
	  */
	 public RecommandInfo searchKeyWords(String access_token,String type,String area,String trade,String hot,String page_num,String offset)
	 {
		 String reqStr=MConstants.URL+"search?access_token="+access_token+"&type="+type+"&area="+area+"&trade="+trade+"&hot="+hot+"&page_num="+page_num+"&offset="+offset;
		 String str = "";
		 str=getData(reqStr);
		 recommandInfo=new RecommandInfo();
		 if(str.equals("err"))
		 {
			 recommandInfo.status=-1; 
		 }else
		 {
		try {
			
			 JSONObject jsonObj = null;
			 jsonObj = new JSONObject(str);
		     int result=Integer.parseInt(jsonObj.getString("status"));
			 if(result==0)
			 {
				  recommandInfo.status=0;
				  JSONArray jsonItems=jsonObj.getJSONArray("items");
		          recommandInfo.items=new ArrayList<Map<String,Object>>();
		          recommandInfo.items=getItems(jsonItems); 
			 }else 
			 {
				 recommandInfo.status=result;
			 }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 }
		return recommandInfo;
	 }
	 
	 /** *
	  * 2.14 1添加自定义搜索器
	  * @param type
	  * @param access_token
	  * @param keywords
	  * @return
	  */
	 public Status addSelfSearch(String access_token,String type,String area,String trade,String hot)
	 {
		 String reqStr="";
		 reqStr=MConstants.URL+"keywords/add?access_token="+access_token+"&type="+type+"&area="+area+"&trade="+trade+"&hot="+hot;
		 status=getStatus(reqStr);
		 return status;
	 }
	 
	 /** *
	  * 2.14 2删除自定义搜索器
	  * @param access_token
	  * @param id
	  * @return
	  */
	 public Status delSelfSearch(String access_token,String id)
	 {
		 String reqStr="";
		 reqStr=MConstants.URL+"keywords/del?access_token="+access_token+"&id="+id;	 
		 status=getStatus(reqStr);
		 return status;
	 }

	 /** *
	  * 2.15 获取自定义搜索器
	  * @param access_token
	  * @param id
	  * @return
	  */
	 public SearchMachine getSearchMachine(String access_token,String id)
	 {
		 String reqStr=MConstants.URL+"get_keywords?access_token="+access_token+"&id="+id;
		 String str = "";
		 str=getData(reqStr);
		 searchMachine=new SearchMachine();
		 if(str.equals("err"))
		 {
			 searchMachine.status=-1;
		 }else
		 {
		 try {
			 JSONObject jsonObj = null;
			 jsonObj = new JSONObject(str);
		     int result=Integer.parseInt(jsonObj.getString("status"));
			 if(result==0)
			 {
				  searchMachine.status=0;
				  JSONArray jsonKeyWords=jsonObj.getJSONArray("keywords");
				  searchMachine.keywords=new ArrayList<Map<String,Object>>();
				  for(int i=0;i<jsonKeyWords.length();i++)
				  {
					  JSONArray jsonKeyWordsContent=(JSONArray) jsonKeyWords.get(i);
					  HashMap<String,Object> info=new HashMap<String, Object>();
					  for(int j=0;j<jsonKeyWordsContent.length();j++)
					  {
						  if(j==0)
						  {
							  info.put(MConstants.SEARCH_MACHINE_ID, jsonKeyWordsContent.get(j));
						  }else if(j==1)
						  {
							  info.put(MConstants.SEARCH_USER_ID, jsonKeyWordsContent.get(j));
						  }else if(j==2)
						  {
							  JSONArray jsonKeyWordsContentArray=(JSONArray) jsonKeyWordsContent.get(j);
							  info.put(MConstants.SEARCH_BIND_TYPE_NUM,jsonKeyWordsContentArray.length());
							  for(int z=0;z<jsonKeyWordsContentArray.length();z++)
							  {
								  info.put(MConstants.SEARCH_BIND_TYPE+z, jsonKeyWordsContentArray.get(z));
							  }
						  }else if(j==3)
						  {
							  JSONArray jsonKeyWordsContentArray=(JSONArray) jsonKeyWordsContent.get(j);
							  info.put(MConstants.SEARCH_BIND_AREA_NUM,jsonKeyWordsContentArray.length());
							  for(int z=0;z<jsonKeyWordsContentArray.length();z++)
							  {
								  info.put(MConstants.SEARCH_BIND_AREA+z, jsonKeyWordsContentArray.get(z));
							  }
						  }else if(j==4)
						  {
							  JSONArray jsonKeyWordsContentArray=(JSONArray) jsonKeyWordsContent.get(j);
							  info.put(MConstants.SEARCH_BIND_INDU_NUM,jsonKeyWordsContentArray.length());
							  for(int z=0;z<jsonKeyWordsContentArray.length();z++)
							  {
								  info.put(MConstants.SEARCH_BIND_INDU+z, jsonKeyWordsContentArray.get(z));
							  }
						  }else if(j==5)
						  {
							  JSONArray jsonKeyWordsContentArray=(JSONArray) jsonKeyWordsContent.get(j);
							  info.put(MConstants.SEARCH_BIND_KEYWORDS_NUM,jsonKeyWordsContentArray.length());
							  for(int z=0;z<jsonKeyWordsContentArray.length();z++)
							  {
								  info.put(MConstants.SEARCH_BIND_KEYWORDS+z, jsonKeyWordsContentArray.get(z));
							  }
						  }else if(j==6)
						  {
							  info.put(MConstants.SEARCH_BIND_IS_USED, jsonKeyWordsContent.get(j));
							  Log.e("tag", "MConstants.SEARCH_BIND_IS_USED"+jsonKeyWordsContent.get(j));
						  }
					  }
					  searchMachine.keywords.add(info);
				  }
				  
			 }else 
			 {
				 searchMachine.status=result;
			 }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 }
		 return searchMachine;
	 }
	 
	 /** *
	  *  2.15.1 开启或者关闭 自定义搜索器
	  * @param operation enable ,disable
	  * @param access_token
	  * @param id
	  * @return
	  */
	 public Status setSearchMachineIsUsed(String operation,String access_token,String id)
	 {
		 String reqStr="";
		 reqStr=MConstants.URL+"keywords/"+operation+"?access_token="+access_token+"&id="+id;	
		 status=getStatus(reqStr);
		 return status;
	 }
	 /** *
	  * 2.16获取热门关键词
	  * @param type
	  * @param access_token
	  * @return
	  */
	 public HotKeyWordsInfo getHotKeyWordsInfo(String type,String access_token)
	 {
		 String reqStr=MConstants.URL+"get_hotwords/"+type+"?access_token="+access_token;
		 String str = "";
		 str=getData(reqStr);
		 hotKeyWordsInfo=new HotKeyWordsInfo();
		 JSONObject jsonObj = null;
		 if(str.equals("err"))
		 {
			 hotKeyWordsInfo.status=-1; 
		 }else
		 {
		 try {
			jsonObj = new JSONObject(str);
			int result=Integer.parseInt(jsonObj.getString("status"));
			 if(result==0)
			 {
				 hotKeyWordsInfo.area=new ArrayList<Map<String,Object>>();
				 JSONArray jsonAreaArray=jsonObj.getJSONArray("area");
				 for(int i=0;i<jsonAreaArray.length();i++)
				 {
					 HashMap<String,Object> info=new HashMap<String, Object>();
					 JSONArray jsonAreaArrayContent=(JSONArray) jsonAreaArray.get(i);
					 for(int j=0;j<jsonAreaArrayContent.length();j++)
					 {
					 if(j==0)
					 {
					 info.put(MConstants.HOT_KEY_WORDS_AREA_NUMBER, jsonAreaArrayContent.get(j));
					 }else if(j==1)
					 {
					  info.put(MConstants.TYPE, MConstants.AREA);
					  info.put(MConstants.VALUE, jsonAreaArrayContent.get(j));
					 }
					 }
					 hotKeyWordsInfo.area.add(info);
				 }
				 
				 hotKeyWordsInfo.hot=new ArrayList<Map<String,Object>>();
				 JSONArray jsonHotArray=jsonObj.getJSONArray("hot");
				 for(int i=0;i<jsonHotArray.length();i++)
				 {
					 HashMap<String,Object> info=new HashMap<String, Object>();
					 JSONArray jsonHotArrayContent=(JSONArray) jsonHotArray.get(i);
					 for(int j=0;j<jsonHotArrayContent.length();j++)
					 {
					 if(j==0)
					 {
					 info.put(MConstants.HOT_KEY_WORDS_HOT_NUMBER, jsonHotArrayContent.get(j));
					 }else if(j==1)
					 {
				      info.put(MConstants.TYPE, MConstants.HOT);
					  info.put(MConstants.VALUE, jsonHotArrayContent.get(j));
					 }
					 }
					 hotKeyWordsInfo.hot.add(info);
				 }
				 
				 hotKeyWordsInfo.trade=new ArrayList<Map<String,Object>>();
				 JSONArray jsonTradeArray=jsonObj.getJSONArray("trade");
				 for(int i=0;i<jsonTradeArray.length();i++)
				 {
					 HashMap<String,Object> info=new HashMap<String, Object>();
					 JSONArray jsonTradeArrayContent=(JSONArray) jsonTradeArray.get(i);
					 for(int j=0;j<jsonTradeArrayContent.length();j++)
					 {
					 if(j==0)
					 {
					 info.put(MConstants.HOT_KEY_WORDS_TRADE_NUMBER, jsonTradeArrayContent.get(j));
					 }else if(j==1)
					 {
					 info.put(MConstants.TYPE, MConstants.TRADE);
					  info.put(MConstants.VALUE, jsonTradeArrayContent.get(j));
					 }
					 }
					 hotKeyWordsInfo.trade.add(info);
				 }
				 hotKeyWordsInfo.type=new  ArrayList<Map<String,Object>>();
				 String [] typeArray={MConstants.TYPE_INVITE,MConstants.TYPE_WIN,MConstants.TYPE_COMPARE,MConstants.TYPE_COMPARE_ORG};
				 String [] typeCodeArray={"invite","win","compare","compare_org"};
				 for(int i=0;i<4;i++)
				 {
					 HashMap<String,Object> info=new HashMap<String, Object>();
					 info.put(MConstants.TYPE, MConstants.TYPE);
					 info.put(MConstants.VALUE, typeArray[i]);
					 info.put(MConstants.CODE_VALUE,typeCodeArray[i]);
					 hotKeyWordsInfo.type.add(info);
				 }
			 }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 }
		 return hotKeyWordsInfo;
	 }
	 
	 protected String retrieveInputStream(HttpEntity httpEntity) {
     int length = (int) httpEntity.getContentLength();
     if (length < 0)
         length = 10000;
     StringBuffer StringBuffer = new StringBuffer(length);
     try {
         InputStreamReader inputStreamReader = new InputStreamReader(
                 httpEntity.getContent(), HTTP.UTF_8);
         char buffer[] = new char[length];
         int count;
         while ((count = inputStreamReader.read(buffer, 0, length - 1)) > 0) {
             StringBuffer.append(buffer, 0, count);
         }
     } catch (UnsupportedEncodingException e) {

     } catch (IllegalStateException e) {

     } catch (IOException e) {

     }
     return StringBuffer.toString();
 }
}
