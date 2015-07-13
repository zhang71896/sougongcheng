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
	
	private String mstate="";
	
	private int num_like=0;

	private int mPosition;  
 
	private Handler handler=new Handler(){
		
		public void handleMessage(Message msg) {
			if(msg.what==1)
			{
				if(status.status==0)
				{
				if(msg.obj.toString().equals("add"))
				{
				Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();
	
				}else
				{
				Toast.makeText(mContext, "取消点赞", Toast.LENGTH_SHORT).show();
				}
				}else
				{
					Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT).show();
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
        viewHolder.btn_like.setChecked(isSelected.get(position));
        viewHolder.tv_date.setText(mMapList.get(position).get(MConstants.COMMENTS_CREATE_TIME).toString());
        viewHolder.tv_num_like.setText(mMapList.get(position).get(MConstants.COMMENTS_LIKE_NUMS).toString());
        viewHolder.tv_num_share.setText(mMapList.get(position).get(MConstants.COMMENTS_SHARE_NUMS).toString());
        viewHolder.tv_nick_name.setText(mMapList.get(position).get(MConstants.COMMENTS_USER_NAME).toString());
        viewHolder.tv_content.setText(mMapList.get(position).get(MConstants.COMMENTS_CONTENTS).toString());
        likeListener=new LikeListener(position,comments_id,viewHolder.btn_like,viewHolder.tv_num_like);
        commentListenner=new CommentListenner(position);
        shareListenner=new ShareListenner(position);
        viewHolder.btn_comment.setOnClickListener(commentListenner);
        viewHolder.btn_like.setOnClickListener(likeListener);
        viewHolder.btn_share.setOnClickListener(shareListenner);
        return convertView;
	}

	
	 private class LikeListener implements  android.view.View.OnClickListener{
         String mid;
         CheckBox mCheckBox;
     	 TextView mLikeNum;
		public LikeListener(int inPosition,String id,CheckBox checkbox,TextView likeNumTV)
		{
			mPosition=inPosition;
			
			mid=id;
			
			mLikeNum=likeNumTV;
			
			mCheckBox=checkbox;
			
			num_like=Integer.parseInt(mMapList.get(inPosition).get(MConstants.COMMENTS_LIKE_NUMS).toString());
		}
		@Override
		public void onClick(View v) {
			isSelected.put(mPosition, mCheckBox.isChecked());
			if(mCheckBox.isChecked())
			{
				mstate="add";
				mMapList.get(mPosition).put(MConstants.COMMENTS_LIKE_NUMS, num_like+1+"");
				mLikeNum.setText(num_like+1+"");
			}else
			{
				mstate="del";
				if(num_like>=1)
				{
				mMapList.get(mPosition).put(MConstants.COMMENTS_LIKE_NUMS, num_like-1+"");
				mLikeNum.setText(num_like-1+"");
				}else
				{
				mLikeNum.setText(num_like+"");	
				}
			}
			mPoolManager.addTask(new Runnable() {
				@Override
				public void run() {
					status=mServer.likeComment(mstate, access_token, mid);
					if(status!=null)
					{
						Message message=handler.obtainMessage();
						message.what=1;
						message.obj=mstate;
						message.sendToTarget();
					}
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
			Intent intent=new Intent(mContext, CommentActivity.class);
			mContext.startActivity(intent);
		}
	 }
	 private void showShare()
	 {
		// TODO Auto-generated method stub
				 ShareSDK.initSDK(mContext);
				 OnekeyShare oks = new OnekeyShare();
				 //关闭sso授权
				 oks.disableSSOWhenAuthorize(); 
				 
				// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
				 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
				 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
				 oks.setTitle(mContext.getString(R.string.share));
				 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
				 oks.setTitleUrl("http://sharesdk.cn");
				 // text是分享文本，所有平台都需要这个字段
				 oks.setText("我是分享文本");
				 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
				 oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
				 // url仅在微信（包括好友和朋友圈）中使用
				 oks.setUrl("http://sharesdk.cn");
				 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
				 oks.setComment("我是测试评论文本");
				 // site是分享此内容的网站名称，仅在QQ空间使用
				 oks.setSite(mContext.getString(R.string.app_name));
				 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
				 oks.setSiteUrl("http://sharesdk.cn");
				 
				// 启动分享GUI
				 oks.show(mContext);
	 }
	 private class ShareListenner implements  android.view.View.OnClickListener{
         int mPosition;  

		 public ShareListenner(int inPosition)
		 {
			 mPosition=inPosition;
		 }
			@Override
			public void onClick(View v) {
				showShare();
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
