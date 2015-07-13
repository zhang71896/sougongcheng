package com.sougongcheng.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.example.sougongcheng.R;
import com.sougongcheng.adapter.AdapterMyProject.ViewHolder;
import com.sougongcheng.server.Server;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.ThreadPoolManager;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterMyCircle extends BaseAdapter{
	
	private ArrayList<Map<String,Object>> mMapList;
	
	private Context mContext;
	
	private LayoutInflater mInflater;
	
	private ThreadPoolManager mPoolManager;
	
	private Server mServer;
	
	private GetShareDatas mGetShareDatas;
	
	private String access_token;
	
	private Resources mResources;
	
	public static Map<Integer,Boolean> isSelected;
		
	public AdapterMyCircle(Context context,ArrayList<Map<String,Object>> mapList)
	{
		mContext=context;
		
		mMapList=mapList;
		
		initDatas();
	}
	private void initDatas() {
		
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
		LikeListener likeListener=null;  
		CommentListenner commentListenner=null;
		ShareListenner shareListenner=null;
        if (null == convertView)
        {	
        	 viewHolder = new ViewHolder();
             convertView=mInflater.inflate(R.layout.suggest_item, null);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        return convertView;
	}

	
	 private class LikeListener implements  android.view.View.OnClickListener{

		public LikeListener()
		{
			
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}  
	 }
	 
	 private class CommentListenner implements  android.view.View.OnClickListener{

		public CommentListenner()
		{
			
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	 }
	 
	 private class ShareListenner implements  android.view.View.OnClickListener{
		 
		 public ShareListenner()
		 {
			 
		 }
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		 }
	 
	 class ViewHolder
	 {
		 public TextView tv_date;
		 
		 public CheckBox btn_like;
		 
		 public Button btn_comment;
		 
		 public Button btn_share;
		 
		 public ImageView iv_header;
		 
		 public TextView tv_num_comment;
		 
		 public TextView tv_num_like;
		 
		 public TextView tv_num_share;
		 
	 }

}
