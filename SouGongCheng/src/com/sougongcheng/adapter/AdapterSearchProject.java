package com.sougongcheng.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sougongcheng.R;
import com.sougongcheng.bean.Status;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.server.Server;
import com.sougongcheng.util.CommenTools;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.ThreadPoolManager;

public class AdapterSearchProject extends BaseAdapter{
	
	private ArrayList<Map<String,Object>> mMapList;
	
	private Context mContext;
	
	private LayoutInflater mInflater;
	
	private int mType;
	
	private List<ImageView> imageViews; // 滑动的图片集合

	private String[] titles; // 图片标题
	
	private int[] imageResId; // 图片ID
	
	private List<View> dots; // 图片标题正文的那些点
	
	private int currentItem = 0; // 当前图片的索引号
	
	private Resources mResources;
	
	private ThreadPoolManager mPoolManager;
	
	private Server mServer;

	private Status status;
	
	private GetShareDatas mGetShareDatas;
	
	private String access_token;
	
	
	public static Map<Integer,Boolean> isSelected;
	
	private Handler handler=new Handler(){
		
		public void handleMessage(Message msg) {
			if(msg.what==1)
			{
				if(msg.obj.toString().equals("add"))
				{
				Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
                intent.setAction("changeLike");        //设置Action
                mContext.sendBroadcast(intent);             
				}else
				{
				Toast.makeText(mContext, "取消收藏", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
                intent.setAction("changeLike");        //设置Action
                mContext.sendBroadcast(intent); 
				}
			}
		};
	};
	
	public AdapterSearchProject(Context context,ArrayList<Map<String,Object>> mapList,int type)
	{
		
		mContext=context;
		
		mType=type;
		
		mMapList=mapList;
		
		mInflater=LayoutInflater.from(mContext);
		
		mResources=mContext.getResources();
		
		mPoolManager=ThreadPoolManager.getInstance();
		
		mServer=Server.getInstance();
		
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, (Activity) context);
		
		access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, "");
		
		initDatas();

	}
	

	private void initDatas() {
		isSelected=new HashMap<Integer,Boolean>();
		for(int i=0;i<mMapList.size();i++)
		{
			if(Integer.parseInt(mMapList.get(i).get(MConstants.RECOMEND_ITEMS_STORE).toString())==1)
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
		// TODO Auto-generated method stub
		return mMapList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mMapList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		MyListener myListener=null;  
        if (null == convertView)
        {
            viewHolder = new ViewHolder();
            convertView=mInflater.inflate(R.layout.item_suggest, null);
            viewHolder.news_title=(TextView) convertView.findViewById(R.id.news_title);
            viewHolder.news_author=(TextView) convertView.findViewById(R.id.news_author);
            viewHolder.news_type=(TextView) convertView.findViewById(R.id.news_type);
            viewHolder.start_time=(TextView) convertView.findViewById(R.id.start_time);
            viewHolder.end_time=(TextView) convertView.findViewById(R.id.end_time);
            viewHolder.label=(TextView) convertView.findViewById(R.id.label);
            viewHolder.heart=(CheckBox) convertView.findViewById(R.id.btn_like);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.news_title.setText(CommenTools.getItemType(mMapList.get(position).get(MConstants.RECOMEND_ITEMS_title).toString()));
        viewHolder.news_author.setText(CommenTools.getItemType(mMapList.get(position).get(MConstants.RECOMEND_ITEMS_AUTHORS).toString()));
        viewHolder.news_type.setText(CommenTools.getItemType(mMapList.get(position).get(MConstants.RECOMEND_ITEMS_TYPE).toString()));
        viewHolder.start_time.setText(CommenTools.getItemType(mMapList.get(position).get(MConstants.RECOMEND_ITEMS_START_TIME).toString()));
        viewHolder.end_time.setText(CommenTools.getItemType(mMapList.get(position).get(MConstants.RECOMEND_ITEMS_END_TIME).toString()));
        String tags="";
        for(int i=0;i<Integer.parseInt(mMapList.get(position).get(MConstants.RECOMEND_ITEMS_TAGS_NUMS).toString());i++)
        {
        	tags+=mMapList.get(position).get("tag"+i).toString();
        }
        viewHolder.label.setText(tags);
        viewHolder.heart.setChecked(isSelected.get(position));
        String mtype=mMapList.get(position).get(MConstants.RECOMEND_ITEMS_TYPE).toString();
        String mid=mMapList.get(position).get(MConstants.RECOMEND_ITEMS_ID).toString();
        myListener=new MyListener(position,  viewHolder.heart,mtype,mid);
        viewHolder.heart.setOnClickListener(myListener);
        return convertView;
	}
	
	 private class MyListener implements  android.view.View.OnClickListener{  
		 CheckBox checkBox;
         int mPosition;  
         String mstate;
         String maccess_token;
         String mtype;
         String mid;
         public MyListener(int inPosition,CheckBox checkbox,String type,String id){  
        	 checkBox=checkbox;
             mPosition= inPosition;  
             mtype=type;
             mid=id;
         }  
		@Override
		public void onClick(View v) {
			isSelected.put(mPosition, checkBox.isChecked());
			if(checkBox.isChecked())
			{
				mstate="add";
			}else
			{
				mstate="del";
			}
			mPoolManager.addTask(new Runnable() {
				@Override
				public void run() {
					status=mServer.StoreBandsInfo(mstate,access_token,mtype,mid);
					Message message=handler.obtainMessage();
					message.what=1;
					message.obj=mstate;
					message.sendToTarget();
				}
			
			});
			  
		}  
           
     }  
	
	class ViewHolder
	{
		public TextView news_title;
		
		public TextView news_author;
		
		public TextView news_type;
		
		public TextView start_time;
		
		public TextView end_time;
		
		public CheckBox heart;
		
		public TextView label;
		
	}
}
