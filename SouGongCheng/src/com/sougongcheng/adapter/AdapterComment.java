package com.sougongcheng.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.sougongcheng.R;
import com.sougongcheng.adapter.AdapterMyProject.ViewHolder;
import com.sougongcheng.bean.Status;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.main.CommentActivity;
import com.sougongcheng.server.Server;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.ThreadPoolManager;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterComment extends BaseAdapter{
	
	private ArrayList<Map<String,Object>> mMapList;
	
	private Context mContext;
	
	private LayoutInflater mInflater;
	
	private ThreadPoolManager mPoolManager;
	
	private Server mServer;
	
	private GetShareDatas mGetShareDatas;
	
	private String access_token;
	
	private Resources mResources;
	
	public static Map<Integer,Boolean> isSelected;
	
	private String comments_id;
	
	private Status status;
	
	private String mstate="";
	
	private int num_like=0;

	private int mPosition;  
 
	public AdapterComment(Context context,ArrayList<Map<String,Object>> mapList)
	{
		mContext=context;
		
		mMapList=mapList;
		
		mPoolManager=ThreadPoolManager.getInstance();
		
		mServer=Server.getInstance();
		
		mInflater=LayoutInflater.from(mContext);
		
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, (Activity) context);
		
		access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, "");
	}
	
	@Override
	public int getCount() {
		return mMapList.size();
	}

	@Override
	public Object getItem(int position) {
		return mMapList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
        if (null == convertView)
        {	
        	 viewHolder = new ViewHolder();
             convertView=mInflater.inflate(R.layout.item_comment, null);
             viewHolder.iv_header=(ImageView) convertView.findViewById(R.id.iv_header);
             viewHolder.tv_nick_name=(TextView) convertView.findViewById(R.id.tv_nick_name);
             viewHolder.tv_content=(TextView) convertView.findViewById(R.id.tv_content);
             convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        comments_id=mMapList.get(position).get(MConstants.COMMENTS_ID).toString();
        viewHolder.tv_nick_name.setText(mMapList.get(position).get(MConstants.COMMENTS_USER_NAME).toString());
        viewHolder.tv_content.setText(mMapList.get(position).get(MConstants.COMMENTS_CONTENTS).toString());
        return convertView;
	}
	
	 class ViewHolder
	 {
		 
		 public ImageView iv_header;
		 
		 public TextView tv_nick_name;
		 
		 public TextView tv_content;
		 
	 }

}
