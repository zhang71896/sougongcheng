package com.sougongcheng.util;

import android.text.TextUtils;

public class CommenTools {
	/** 
	 * 验证手机格式 
	 */  
	public static boolean isMobileNO(String mobiles) {  
	    /* 
	    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 
	    联通：130、131、132、152、155、156、185、186 
	    电信：133、153、180、189、（1349卫通） 
	    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9 
	    */  
	    String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
	    if (TextUtils.isEmpty(mobiles)) return false;  
	    else return mobiles.matches(telRegex);  
	   }  
	
	public static boolean inputCheck(Object str)
	{
		if(str==null||TextUtils.isEmpty(str.toString()))
		{
			return false;
		}else
		{
			return true;
		}
	}
	
	public static boolean isPassword(String psw) {  
		//6到20位
	    String telRegex = "^[a-zA-Z0-9]{6,20}+$";  
	    if (TextUtils.isEmpty(psw)) return false;  
	    else return psw.matches(telRegex);  
	   }  
	
	public static String chToEn(String content)
	{
		content=content.replaceAll("招标", "invite");
		
		content=content.replaceAll("中标", "win");
		
		content=content.replaceAll("代理比选", "compare_org");
		
		content=content.replaceAll("项目比选", "compare");
		
		
		
		return content;
	}
	
	public static String enToCh(String content)
	{
		content=content.replaceAll("invite", "招标");
		
		content=content.replaceAll("win", "中标");
		
		content=content.replaceAll("compare_org", "代理比选");
		
		content=content.replaceAll("compare", "项目比选");
		
		
		
		return content;
		
	}
	public static String getResult(int result)
	{
		String resultStr="";
		switch(result)
		{
		case -1:
			resultStr= "内部错误";
			break;
		case -2:
			resultStr= "用户名已经被注册";
			break;
		case -3:
			resultStr= "手机号已经被注册";
			break;
		case -4:
			resultStr= "电话号码格式不合法";
			break;
		case -5:
			resultStr= "密码长度不足6位";
			break;
		case -6:
			resultStr= "无效的用户名或密码";
			break;
		case -7:
			resultStr= "用户不存在或已被禁用";
			break;
		case -8:
			resultStr= "旧密码不正确";
			break;
		case -9:
			resultStr= "参数中没有旧密码";
			break;
		case -10:
			resultStr= "参数错误";
			break;
		case -11:
			resultStr= "没有用户令牌";
			break;
		case -12:
			resultStr= "用户已经收藏过该信息";
			break;
		case -13:
			resultStr= "用户未收藏该信息";
			break;
		case -14:
			resultStr= "评论不存在或已被删除";
			break;
		case -15:
			resultStr= "用户已经赞过该评论";
			break;
		case -16:
			resultStr= "用户没有赞过该评论";
			break;
		case -17:
			resultStr= "用户已经收藏过该评论";
			break;
		case 0:
			resultStr= "请求成功";
			break;
		}
		return resultStr;
	}

	public static String getItemType(String type)
	{
		String str="";
		if(type==null||type.equals(" ")||type.equals("None"))
		{
			str="   ";
		}
		else if(type.equals("win"))
		{
			str= "中标信息";
		}else if (type.equals("invite"))
		{
			str=  "招标信息";
		}else if(type.equals("compare"))
		{
			str=  "工程项目比选";
		}else if(type.equals("compare_org"))
		{
			str=  "代理机构比选";
		}else if(type.equals("favorite"))
		{
			str=  "收藏的招标信息";
		}else
		{
			str=type;
		}
		return str;
	}
}
