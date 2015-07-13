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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sougongcheng.bean.Status;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.server.Server;
import com.sougongcheng.ui.widget.SwitchButton;
import com.sougongcheng.util.CommenTools;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.ThreadPoolManager;
import com.test.finder.R;

public class AdapterSearchMachine extends BaseAdapter{
	
	public ArrayList<Map<String,Object>> mMapList;
	
	private Context mContext;
	
	private LayoutInflater mInflater;
	
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
	
	private String[] mStringsArray;
	
	public static Map<Integer,Boolean> isSelected;
	
	private Handler handler=new Handler(){
		
		public void handleMessage(Message msg) {
			if(msg.what==1)
			{
				if(msg.obj.toString().equals("enable"))
				{
				Toast.makeText(mContext, "跟踪器开启成功", Toast.LENGTH_SHORT).show();
				}else
				{
				Toast.makeText(mContext, "跟踪器关闭成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				}
			}
		};
	};
	
	public AdapterSearchMachine(Context context,ArrayList<Map<String,Object>> mapList,String[] stringArray)
	{
		
		mContext=context;
		
		mMapList=mapList;
		
		mInflater=LayoutInflater.from(mContext);
		
		mResources=mContext.getResources();
		
		mPoolManager=ThreadPoolManager.getInstance();
		
		mServer=Server.getInstance();
		
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, (Activity) context);
		
		access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, "");
		
		mStringsArray=stringArray;
		
		initDatas();

	}
	

	private void initDatas() {
		isSelected=new HashMap<Integer,Boolean>();
		for(int i=0;i<mMapList.size();i++)
		{
			if(mMapList.get(i).get(MConstants.SEARCH_BIND_IS_USED) != null)
			{ 
				if((Boolean) mMapList.get(i).get(MConstants.SEARCH_BIND_IS_USED))
				{
				isSelected.put(i, true);
				}else
				{
				isSelected.put(i, false);	
				}
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
        viewHolder = new ViewHolder();
        convertView=mInflater.inflate(R.layout.item_search_machine, null);
        viewHolder.tv_content=(TextView) convertView.findViewById(R.id.tv_content);
        viewHolder.btn_switch=(SwitchButton) convertView.findViewById(R.id.btn_switch);
        convertView.setTag(viewHolder);
        viewHolder.tv_content.setText(mStringsArray[position]);
        viewHolder.btn_switch.setChecked(isSelected.get(position));
        String mid=mMapList.get(position).get(MConstants.SEARCH_MACHINE_ID).toString();
        myListener=new MyListener(position,mid,viewHolder.btn_switch);
        viewHolder.btn_switch.setOnCheckedChangeWidgetListener(myListener);
        return convertView;
	}
	
	 private class MyListener implements OnCheckedChangeListener{  
         int mPosition;  
         String mstate;
         String mid;
         SwitchButton mSwitchButton;
         public MyListener(int inPosition,String id,SwitchButton Button){  
             mPosition= inPosition;  
             mid=id;
             mSwitchButton=Button;
         }  
	
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			isSelected.put(mPosition, mSwitchButton.isChecked());
			if(isChecked)
			{
				mstate="enable";
			}else
			{
				mstate="disable";
			}
			mPoolManager.addTask(new Runnable() {
				@Override
				public void run() {
					status=mServer.setSearchMachineIsUsed(mstate, access_token, mid);
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
		public TextView tv_content;
		
		public SwitchButton btn_switch;
		
	}
}
