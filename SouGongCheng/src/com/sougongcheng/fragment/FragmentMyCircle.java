package com.sougongcheng.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sougongcheng.R;
import com.sougongcheng.bean.RecommandInfo;
import com.sougongcheng.bean.UserInfo;
import com.sougongcheng.server.Server;
import com.sougongcheng.util.ThreadPoolManager;

public class FragmentMyCircle extends Fragment{
	
	private View myView;
	
	private Server mServer;
	
	private String test_str;
	
	private ThreadPoolManager mPoolManager;
	
	private UserInfo userInfo;
	
	private RecommandInfo recommandInfo;
	
	private ArrayList<Map<String,Object>> mMapList;
	
    private ListView actualListView;

    private boolean isRefreshing=false;
    
	private Handler mHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1)
			{
				
			}
		};
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		myView=inflater.inflate(R.layout.fragment_my_circle, null);	
		initViews();
		return myView;
	}

	private void initViews() {
		mServer=Server.getInstance();
		mPoolManager=ThreadPoolManager.getInstance();
		mPoolManager.addTask(new Runnable() {
			public void run() {
				String strUTF8;
					recommandInfo=mServer.getBandsInfo("win", "1-gonglijun", "10", "20");
					if(recommandInfo!=null)
					{
						Message message=mHandler.obtainMessage();
						message.what=1;
						message.sendToTarget();
					}
					
			}
			});
		
	}

}
