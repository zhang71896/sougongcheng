package com.sougongcheng.fragment;

import java.util.ArrayList;
import java.util.Map;

import com.example.sougongcheng.R;
import com.sougongcheng.adapter.AdapterMyProject;
import com.sougongcheng.adapter.AdapterSearchProject;
import com.sougongcheng.bean.RecommandInfo;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.main.LoginActivity;
import com.sougongcheng.main.MessageDetail;
import com.sougongcheng.server.Server;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.ThreadPoolManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragmentMyProject extends Fragment implements OnItemClickListener{
	
    private ListView actualListView;
    
    private ThreadPoolManager mPoolManager;
	
	private Server mServer;

	private RecommandInfo recommandInfo;
	
	private View myView;
	
	private String type;
	
	private AdapterMyProject adapterMyProject;
	
	private ArrayList<Map<String,Object>> mMapList;
	
	private GetShareDatas mGetShareDatas;
	
	private String access_token;
	
	private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
				initDatas();
		}
	};
	
	private Handler mHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1)
			{
				changeDataSource();
 			}
			
		};
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 IntentFilter dynamic_filter = new IntentFilter();
	     dynamic_filter.addAction("changeLike");            //添加动态广播的Action
	     getActivity().registerReceiver(mBroadcastReceiver, dynamic_filter); 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		myView=inflater.inflate(R.layout.fragment_my_project, null);
		
		initViews();
		
		initDatas();
		
		return myView;
	}

	private void initViews() {
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, getActivity());

		access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, "");
		
		actualListView=(ListView) myView.findViewById(R.id.content_list);
	}

	private void initDatas() {
		type="favorite";
		
		if(mServer==null)
		{
		mServer=Server.getInstance();
		}
		if(mPoolManager==null)
		{
		mPoolManager=ThreadPoolManager.getInstance();
		}
		
		mPoolManager.addTask(new Runnable() {
			public void run() {
				recommandInfo=mServer.getBandsInfo(type, access_token, "10", "0");
				if(recommandInfo!=null)
				{
					Message message=mHandler.obtainMessage();
					message.what=1;
					message.sendToTarget();
				}
			}
		});
	}
	
	private void changeDataSource() {
		// TODO Auto-generated method stub
	
		mMapList=recommandInfo.items;

		adapterMyProject=new AdapterMyProject(getActivity(), mMapList, 0);
		
		actualListView.setAdapter(adapterMyProject);
		
		actualListView.setOnItemClickListener(this);
		

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		 String itemId=mMapList.get(position).get(MConstants.RECOMEND_ITEMS_ID).toString();
		 Log.e("tag", "itemId:"+itemId);
		 String itemType=mMapList.get(position).get(MConstants.RECOMEND_ITEMS_TYPE).toString();
	 	 String url=mServer.getBandsInfoDetail(access_token, itemType, itemId);
		 Intent intent=new Intent(getActivity(),MessageDetail.class);
		 intent.putExtra("url", url);
		 this.startActivity(intent);
		
	}

}

