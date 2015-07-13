package com.sougongcheng.util;

import android.text.TextUtils;

public class CommenTools {
	/** 
	 * ��֤�ֻ���ʽ 
	 */  
	public static boolean isMobileNO(String mobiles) {  
	    /* 
	    �ƶ���134��135��136��137��138��139��150��151��157(TD)��158��159��187��188 
	    ��ͨ��130��131��132��152��155��156��185��186 
	    ���ţ�133��153��180��189����1349��ͨ�� 
	    �ܽ��������ǵ�һλ�ض�Ϊ1���ڶ�λ�ض�Ϊ3��5��8������λ�õĿ���Ϊ0-9 
	    */  
	    String telRegex = "[1][358]\\d{9}";//"[1]"�����1λΪ����1��"[358]"����ڶ�λ����Ϊ3��5��8�е�һ����"\\d{9}"��������ǿ�����0��9�����֣���9λ��  
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
		//6��20λ
	    String telRegex = "^[a-zA-Z0-9]{6,20}+$";  
	    if (TextUtils.isEmpty(psw)) return false;  
	    else return psw.matches(telRegex);  
	   }  
	
	public static String chToEn(String content)
	{
		content=content.replaceAll("�б�", "invite");
		
		content=content.replaceAll("�б�", "win");
		
		content=content.replaceAll("�����ѡ", "compare_org");
		
		content=content.replaceAll("��Ŀ��ѡ", "compare");
		
		
		
		return content;
	}
	
	public static String enToCh(String content)
	{
		content=content.replaceAll("invite", "�б�");
		
		content=content.replaceAll("win", "�б�");
		
		content=content.replaceAll("compare_org", "�����ѡ");
		
		content=content.replaceAll("compare", "��Ŀ��ѡ");
		
		
		
		return content;
		
	}
	public static String getResult(int result)
	{
		String resultStr="";
		switch(result)
		{
		case -1:
			resultStr= "�ڲ�����";
			break;
		case -2:
			resultStr= "�û����Ѿ���ע��";
			break;
		case -3:
			resultStr= "�ֻ����Ѿ���ע��";
			break;
		case -4:
			resultStr= "�绰�����ʽ���Ϸ�";
			break;
		case -5:
			resultStr= "���볤�Ȳ���6λ";
			break;
		case -6:
			resultStr= "��Ч���û���������";
			break;
		case -7:
			resultStr= "�û������ڻ��ѱ�����";
			break;
		case -8:
			resultStr= "�����벻��ȷ";
			break;
		case -9:
			resultStr= "������û�о�����";
			break;
		case -10:
			resultStr= "��������";
			break;
		case -11:
			resultStr= "û���û�����";
			break;
		case -12:
			resultStr= "�û��Ѿ��ղع�����Ϣ";
			break;
		case -13:
			resultStr= "�û�δ�ղظ���Ϣ";
			break;
		case -14:
			resultStr= "���۲����ڻ��ѱ�ɾ��";
			break;
		case -15:
			resultStr= "�û��Ѿ��޹�������";
			break;
		case -16:
			resultStr= "�û�û���޹�������";
			break;
		case -17:
			resultStr= "�û��Ѿ��ղع�������";
			break;
		case 0:
			resultStr= "����ɹ�";
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
			str= "�б���Ϣ";
		}else if (type.equals("invite"))
		{
			str=  "�б���Ϣ";
		}else if(type.equals("compare"))
		{
			str=  "������Ŀ��ѡ";
		}else if(type.equals("compare_org"))
		{
			str=  "���������ѡ";
		}else if(type.equals("favorite"))
		{
			str=  "�ղص��б���Ϣ";
		}else
		{
			str=type;
		}
		return str;
	}
}
