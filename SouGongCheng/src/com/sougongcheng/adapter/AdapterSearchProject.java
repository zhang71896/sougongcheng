package com.sougongcheng.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sougongcheng.R;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.dataaccess.DataAccess;
import com.sougongcheng.util.CommenTools;

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
	


	
	public AdapterSearchProject(Context context,ArrayList<Map<String,Object>> mapList,int type)
	{
		
		mContext=context;
		
		mType=type;
		
		mMapList=mapList;
		
		mInflater=LayoutInflater.from(mContext);
		
		mResources=mContext.getResources();
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
            convertView=mInflater.inflate(R.layout.suggest_item, null);
            viewHolder.news_title=(TextView) convertView.findViewById(R.id.news_title);
            viewHolder.news_author=(TextView) convertView.findViewById(R.id.news_author);
            viewHolder.news_type=(TextView) convertView.findViewById(R.id.news_type);
            viewHolder.start_time=(TextView) convertView.findViewById(R.id.start_time);
            viewHolder.end_time=(TextView) convertView.findViewById(R.id.end_time);
            viewHolder.label=(TextView) convertView.findViewById(R.id.label);
            viewHolder.heart=(ImageView) convertView.findViewById(R.id.heart);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        myListener=new MyListener(position);  
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
        
        if(Integer.parseInt(mMapList.get(position).get(MConstants.RECOMEND_ITEMS_STORE).toString())==1)
        {
        	viewHolder.heart.setBackgroundResource(R.drawable.heart_red);
        }else
        {
        	viewHolder.heart.setBackgroundResource(R.drawable.heart_gray);
        }
        
        viewHolder.heart.setOnClickListener(myListener);
        return convertView;
	}
	
	 private class MyListener implements  android.view.View.OnClickListener{  
         int mPosition;  
         public MyListener(int inPosition){  
             mPosition= inPosition;  
         }  
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			   new DataAccess(mContext).insertNews(mMapList.get(mPosition));
		}  
           
     }  
	
	class ViewHolder
	{
		public TextView news_title;
		
		public TextView news_author;
		
		public TextView news_type;
		
		public TextView start_time;
		
		public TextView end_time;
		
		public ImageView heart;
		
		public TextView label;
		
	}
}
