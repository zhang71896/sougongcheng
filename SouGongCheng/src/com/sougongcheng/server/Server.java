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
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sougongcheng.bean.AccessStatus;
import com.sougongcheng.bean.CommentsInfo;
import com.sougongcheng.bean.RecommandInfo;
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
		            HttpResponse response = httpClient.execute(request);
		            HttpEntity responseEntity = response.getEntity();
		            str=retrieveInputStream(responseEntity);
			    } catch (Exception e) {
		            e.printStackTrace();
		        }
				return str;
		}
	
	 /** 
	  * 1.1����status
	  * @param reqStr
	  * @return
	  **/
	 private Status getStatus(String reqStr)
	 {
		 String str = "";
		 try {
	            str=getData(reqStr);
	            GsonBuilder gsonb = new GsonBuilder(); 
		        Gson gson = gsonb.create();
		        status= gson.fromJson(str,Status.class);
		    } catch (Exception e) {
	            e.printStackTrace();
	        }
		 return status;
	 }
	 
	
	 /** 
	  * 1.2���ش�reuslt_status
	  * @param reqStr
	  * @return
	  **/
	 private AccessStatus getAccessStatus(String reqStr)
	 {
		 String str = "";
		 try {
	            str=getData(reqStr);
	            GsonBuilder gsonb = new GsonBuilder(); 
		        Gson gson = gsonb.create();
		        accessStatus= gson.fromJson(str,AccessStatus.class);
		    } catch (Exception e) {
	            e.printStackTrace();
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
        
         	//��һ��
         	JSONArray jsonItemContents=jsonItems.getJSONArray(i);
         	for(int j=0;j<jsonItemContents.length();j++)
         	{  
         		if(j==5)
         		{
         			JSONArray jsonItemContentsTags=jsonItemContents.getJSONArray(5);
     				info.put(MConstants.RECOMEND_ITEMS_TAGS_NUMS, jsonItemContentsTags.length());
                 	Log.e("tag","jsonItemContentsTags"+jsonItemContentsTags.length());
         			for(int z=0;z<jsonItemContentsTags.length();z++)
         			{
	            			info.put("tag"+z, jsonItemContentsTags.get(z));
	                     	Log.e("tag","jsonItemContentsTags.get(z)"+jsonItemContentsTags.get(z));
         			}
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
	 
	 //2.1 �û�ע�� 
	 
	 /** 
	  * 2.1 ע��
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
	 
	 //2.3�޸��û���Ϣ
	 //�޸��û���
	
	 /**
	  * 2.2�û���¼
	  * @param userName
	  * @param passWord
	  * @return
	  **/
	 public UserInfo login(String userName,String passWord)
	 {
		 String reqStr=MConstants.URL+"login?username="+userName+"&password="+passWord;
		 String str = "";
			try {
	            str=getData(reqStr);
	            GsonBuilder gsonb = new GsonBuilder(); 
		        Gson gson = gsonb.create();
		        userInfo= gson.fromJson(str,UserInfo.class);
		    } catch (Exception e) {
	            e.printStackTrace();
	        }
			return userInfo;
	 }
	 
	 /**
	  * 2.3�޸��û���Ϣ
	  * @param access_token
	  * @param userName
	  * @return
	  */
	 public AccessStatus modifyUserName(String access_token,String userName)
	 {
		 String reqStr=MConstants.URL+"modify_user/username?access_token="+access_token+"&username="+userName;
		 return getAccessStatus(reqStr);
	 }
	 
	 //�޸�����
	 public AccessStatus modifyPassWord(String access_token,String passWord,String oldPassWord)
	 {
		 String reqStr=MConstants.URL+"modify_user/password?access_token="+access_token+"&password="+passWord+"&oldpassword="+oldPassWord;
		 return getAccessStatus(reqStr);
	 }
	 
	 //�޸��û���
	 public AccessStatus modifySex(String access_token,String sex)
	 {
		 String reqStr=MConstants.URL+"modify_user/sex?access_token="+access_token+"&sex="+sex;
		 return getAccessStatus(reqStr);
	 }
	 
	 //�޸ĵ绰����
	 public AccessStatus modifyTelNum(String access_token,String telNum)
	 {
		 String reqStr=MConstants.URL+"modify_user/telnum?access_token="+access_token+"&telnum="+telNum;
		 return getAccessStatus(reqStr);
	 }

	 
	 /** *
	  * 2.4��ҳ�Ƽ�
	  * @param access_token
	  * @param num
	  * @return
	  **/
	 public RecommandInfo getRecommandInfo(String access_token,String num)
	 {
		 String reqStr=MConstants.URL+"recommend?access_token="+access_token+"&num="+num;
		// String reqStr="http://120.25.224.229:8888/recommend?access_token=1-gonglijun&num=2";
		 String str = "";
			try {
	            str=getData(reqStr);
	            recommandInfo=new RecommandInfo();
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
	            	JSONArray jsonBannersContents=jsonItems.getJSONArray(i);
	            	for(int j=0;j<jsonBannersContents.length();j++)
	            	{
	            		if(j==0)
	            		{
	            			info.put(MConstants.RECOMEND_BANNERS_IMAGEURL, jsonBannersContents.get(j));
	            			break;
	            		}else if(j==1)
	            		{
	            			info.put(MConstants.RECOMEND_BANNERS_GOTOURL, jsonBannersContents.get(j));
	            			break;
	            		}
	            	}
	            	recommandInfo.banners.add(info);
	            }
		    } catch (Exception e) {
	            e.printStackTrace();
	        }
			return recommandInfo;
	 }
	 
	 /** 
	  * 2.5 �б���Ϣ�б�
	  * @param type
	  * @param access_token
	  * @param page_num
	  * @param offset
	  * @return
	  **/
	 public RecommandInfo getBandsInfo(String type,String access_token,String page_num,String offset)
	 {
		  String reqStr=MConstants.URL+"list/"+type+"?access_token="+access_token+"&page_num="+page_num+"&offset="+offset;
		// String reqStr="http://120.25.224.229:8888/list/win?access_token=1-gonglijun&page_num=10&offset=20";
		 String str = "";
			try {
	            str=getData(reqStr);
	            recommandInfo=new RecommandInfo();
	            JSONObject jsonObj = new JSONObject(str);
	            recommandInfo.status=Integer.parseInt(jsonObj.getString("status"));
	            recommandInfo.items=new ArrayList<Map<String,Object>>();
	            JSONArray jsonItems=jsonObj.getJSONArray("items");
	            recommandInfo.items=getItems(jsonItems);
			 } catch (Exception e) {
		            e.printStackTrace();
		        }
		 return recommandInfo;
	 }
	 
	 /** 
	  * 2.6�б���Ϣ����
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
 //�Ѵ��͵�ʵ��ת����Ϊ�ַ���,����jason��������
	 
	 /** 
	  * 2.7�б���Ϣ�ղ�
	  * @param state
	  * @param access_token
	  * @param type
	  * @param id
	  * @return
	  **/
	 public Status StoreBandsInfo(String state,String access_token,String type,String id)
	 {
		 String reqStr=MConstants.URL+"favorite/"+state+"?access_token="+access_token+"&type="+type+"&id="+id;
		 String str = "";
		 str=getData(reqStr);
		 return getStatus(str);
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
