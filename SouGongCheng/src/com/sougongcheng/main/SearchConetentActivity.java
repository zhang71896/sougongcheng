package com.sougongcheng.main;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sougongcheng.adapter.AdapterSearchProject;
import com.sougongcheng.bean.RecommandInfo;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.server.Server;
import com.sougongcheng.util.CommenTools;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.ThreadPoolManager;
import com.test.finder.R;

public class SearchConetentActivity extends Activity implements OnClickListener, OnItemClickListener{
	
	private TextView tv_back;
	
	private ArrayList<Map<String,Object>> mMapList;
	
    private ListView actualListView;
    
	private PullToRefreshListView mPullRefreshListView;
	
	private GetShareDatas mGetShareDatas;
	
	private ThreadPoolManager mPoolManager;
	
	private RecommandInfo recommandInfo;
	
	private Server mServer;
	
	private String access_token;
	
	private String type;
	
	private String area;
	
	private String trade;
	
	private String hot;
	
	
	private AdapterSearchProject adapterSearchProject;
	
	private Handler mHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1)
			{
				if(recommandInfo.status==0)
				{
				mMapList=recommandInfo.items;
				if(mMapList!=null&&mMapList.size()>0)
				{
				adapterSearchProject=new AdapterSearchProject(SearchConetentActivity.this, mMapList,0);
				actualListView.setAdapter(adapterSearchProject);
				actualListView.setVisibility(View.VISIBLE);
				}else
				{
					Toast.makeText(SearchConetentActivity.this, "没有找到相关数据", Toast.LENGTH_SHORT).show();
				}
				}else
				{
					
				}
			}
		};
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initViews();
		
		initDatas();
		
		initClickListeners();
	}

	private void initClickListeners() {
		
		tv_back.setOnClickListener(this);
	}

	private void initDatas() {
		
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, SearchConetentActivity.this);
		
		access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, "");
		
		Intent intent=getIntent();
	
		type=intent.getStringExtra("type");
		
		type=CommenTools.chToEn(type);
		
		area=intent.getStringExtra("area");
		
		trade=intent.getStringExtra("trade");
		
		hot=intent.getStringExtra("hot");
		
		Log.e("tag", "typeCondition"+type);
		Log.e("tag", "areaCondition"+area);
		Log.e("tag", "tradeConditon"+trade);
		Log.e("tag", "hotCondition"+hot);
		mServer=Server.getInstance();
		
		mPoolManager=ThreadPoolManager.getInstance();
		
		mPoolManager.addTask(new Runnable() {
		@Override
		public void run() {
			recommandInfo=mServer.searchKeyWords(access_token, type, area, trade, hot, "20","0");
			if(recommandInfo!=null)
			{
			Message message=mHandler.obtainMessage();
			message.what=1;
			message.sendToTarget();
			}
		}
	});
	}

	private void initViews() {
		
		setContentView(R.layout.act_search_content);
		
		tv_back=(TextView) findViewById(R.id.tv_back);
		
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.content_list);
		
		actualListView= mPullRefreshListView.getRefreshableView();
		
		actualListView.setFocusable(true);
		
		actualListView.setOnItemClickListener(this);
		
		mPullRefreshListView.setMode(Mode.BOTH);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		 String itemId=mMapList.get(position-1).get(MConstants.RECOMEND_ITEMS_ID).toString();
		 String itemType=mMapList.get(position-1).get(MConstants.RECOMEND_ITEMS_TYPE).toString();
	 	 String url=mServer.getBandsInfoDetail(access_token, itemType, itemId);
		 Intent intent=new Intent(SearchConetentActivity.this,MessageDetail.class);
		 intent.putExtra("url", url);
		 startActivity(intent);
	}
	

}
