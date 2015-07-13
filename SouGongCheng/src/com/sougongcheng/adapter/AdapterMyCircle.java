package com.sougongcheng.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.sougongcheng.R;
import com.sougongcheng.adapter.AdapterMyProject.ViewHolder;
import com.sougongcheng.bean.Status;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.server.Server;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.ThreadPoolManager;

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
	
	private String comments_id;
	
	private Status status;
	
	private Handler handler=new Handler(){
		
		public void handleMessage(Message msg) {
			if(msg.what==1)
			{
				if(msg.obj.toString().equals("add"))
				{
				Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();
				}else
				{
				Toast.makeText(mContext, "取消点赞", Toast.LENGTH_SHORT).show();
				}
			}
		};
	};
		
	public AdapterMyCircle(Context context,ArrayList<Map<String,Object>> mapList)
	{
		mContext=context;
		
		mMapList=mapList;
		
		mPoolManager=ThreadPoolManager.getInstance();
		
		mServer=Server.getInstance();
		
		mInflater=LayoutInflater.from(mContext);
		
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, (Activity) context);
		
		access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, "");
		
		initDatas();
	}
	private void initDatas() {
		
		isSelected=new HashMap<Integer,Boolean>();
		for(int i=0;i<mMapList.size();i++)
		{
			if(Integer.parseInt(mMapList.get(i).get(MConstants.COMMENTS_IS_LIKE).toString())==1)
			{
				isSelected.put(i, true);
			}else
			{
				isSelected.put(i, false);
			}
		}
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
             convertView=mInflater.inflate(R.layout.item_circle, null);
             viewHolder.btn_comment=(Button) convertView.findViewById(R.id.btn_comment);
             viewHolder.btn_like=(CheckBox) convertView.findViewById(R.id.btn_like);
             viewHolder.btn_share=(Button) convertView.findViewById(R.id.btn_share);
             viewHolder.iv_header=(ImageView) convertView.findViewById(R.id.iv_header);
             viewHolder.tv_nick_name=(TextView) convertView.findViewById(R.id.tv_nick_name);
             viewHolder.tv_content=(TextView) convertView.findViewById(R.id.tv_content);
             viewHolder.tv_date=(TextView) convertView.findViewById(R.id.tv_date);
             viewHolder.tv_num_like=(TextView) convertView.findViewById(R.id.tv_num_like);
             viewHolder.tv_num_share=(TextView) convertView.findViewById(R.id.tv_num_share);
             convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        comments_id=mMapList.get(position).get(MConstants.COMMENTS_ID).toString();
        likeListener=new LikeListener(position, comments_id,viewHolder.btn_like);
        viewHolder.btn_like.setChecked(isSelected.get(position));
        viewHolder.tv_date.setText(mMapList.get(position).get(MConstants.COMMENTS_CREATE_TIME).toString());
        viewHolder.tv_num_like.setText(mMapList.get(position).get(MConstants.COMMENTS_LIKE_NUMS).toString());
        viewHolder.tv_num_share.setText(mMapList.get(position).get(MConstants.COMMENTS_SHARE_NUMS).toString());
        viewHolder.tv_nick_name.setText(mMapList.get(position).get(MConstants.COMMENTS_USER_NAME).toString());
        viewHolder.tv_content.setText(mMapList.get(position).get(MConstants.COMMENTS_CONTENTS).toString());
        viewHolder.btn_comment.setOnClickListener(commentListenner);
        viewHolder.btn_like.setOnClickListener(likeListener);
        viewHolder.btn_share.setOnClickListener(shareListenner);
        return convertView;
	}

	
	 private class LikeListener implements  android.view.View.OnClickListener{
         int mPosition;  
         String mid;
         CheckBox mCheckBox;
         String mstate="";
		public LikeListener(int inPosition,String id,CheckBox checkbox)
		{
			mPosition=inPosition;
			
			mid=id;
			
			mCheckBox=checkbox;
		}
		@Override
		public void onClick(View v) {
			isSelected.put(mPosition, mCheckBox.isChecked());
			if(mCheckBox.isChecked())
			{
				mstate="add";
			}else
			{
				mstate="del";
			}
			mPoolManager.addTask(new Runnable() {
				@Override
				public void run() {
					status=mServer.likeComment(mstate, access_token, mid);
					Message message=handler.obtainMessage();
					message.what=1;
					message.obj=mstate;
					message.sendToTarget();
				}
			});
		}
	 }
	 
	 private class CommentListenner implements  android.view.View.OnClickListener{
         int mPosition;  

		public CommentListenner(int inPosition)
		{
			
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	 }
	 
	 private class ShareListenner implements  android.view.View.OnClickListener{
         int mPosition;  

		 public ShareListenner(int inPosition)
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
		 
		 public TextView tv_nick_name;
		 
		 public TextView tv_num_like;
		 
		 public TextView tv_num_share;
		 
		 public TextView tv_content;
		 
	 }

}
