package com.sougongcheng.fragment;

import java.util.ArrayList;
import java.util.Map;

import com.example.sougongcheng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.sougongcheng.adapter.AdapterMyProject;
import com.sougongcheng.adapter.AdapterSearchProject;
import com.sougongcheng.bean.RecommandInfo;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.main.LoginActivity;
import com.sougongcheng.main.MessageDetail;
import com.sougongcheng.server.Server;
import com.sougongcheng.ui.widget.ProgressWheel;
import com.sougongcheng.ui.widget.SpotsDialog;
import com.sougongcheng.ui.widget.ProgressWheel.ProgressCallback;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.NetworkUtils;
import com.sougongcheng.util.ThreadPoolManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragmentMyProject extends Fragment implements OnItemClickListener{
	
    private ListView actualListView;
    
	private PullToRefreshListView mPullRefreshListView;
    
    private ThreadPoolManager mPoolManager;
	
	private Server mServer;

	private RecommandInfo recommandInfo;
	
	private View myView;
	
	private String type;
	
	private AdapterMyProject adapterMyProject;
	
	private ArrayList<Map<String,Object>> mMapList;
	
	private GetShareDatas mGetShareDatas;
	
	private String access_token;
	
    private boolean isRefreshing=false;

    private ProgressWheel progressWheel;
    
	private SpotsDialog spotsDialog;
	
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
		
		initClickListenner();
		
		return myView;
	}

	private void initClickListenner() {
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if(!isRefreshing)
				{
				isRefreshing=true;
				
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.
				new GetDataTask().execute();
				
				}else
				{
				mPullRefreshListView.onRefreshComplete();
				}
			}
		});
	}

	private void initViews() {
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, getActivity());

		access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, "");
		
		mPullRefreshListView = (PullToRefreshListView) myView.findViewById(R.id.content_list);
		
		mPullRefreshListView.setFocusable(true);
		
		actualListView= mPullRefreshListView.getRefreshableView();
		
		actualListView.setFocusable(true);
		
		actualListView.setOnItemClickListener(this);
		
		mPullRefreshListView.setMode(Mode.BOTH);
		
        progressWheel = (ProgressWheel)myView.findViewById(R.id.progress_wheel);

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
		if(NetworkUtils.isNetworkAvailable(getActivity()))
		{
		mPoolManager.addTask(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				recommandInfo=mServer.getBandsInfo(type, access_token, "10", "0");
				if(recommandInfo!=null)
				{
					Message message=mHandler.obtainMessage();
					message.what=1;
					message.sendToTarget();
				}
			}
		});
		}else
		{
			Toast.makeText(getActivity(), "当前网络不可用", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void changeDataSource() {
		// TODO Auto-generated method stub
	
		mMapList=recommandInfo.items;

		adapterMyProject=new AdapterMyProject(getActivity(), mMapList, 0);
		
		actualListView.setAdapter(adapterMyProject);
		
		actualListView.setOnItemClickListener(this);
		

	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return mStrings;
		}

		@Override
		protected void onPostExecute(String[] result) {
			if(mPullRefreshListView.isHeaderShown())
			{
		/*	mListItems.addFirst("Added after refresh...");*/
			}else if(mPullRefreshListView.isFooterShown())
			{
				/*mListItems.addFirst("Added after refresh...");	*/
			}
			/*mAdapter.notifyDataSetChanged();*/
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
	private String[] mStrings = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler" };


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		 String itemId=mMapList.get(position-1).get(MConstants.RECOMEND_ITEMS_ID).toString();
		 String itemType=mMapList.get(position-1).get(MConstants.RECOMEND_ITEMS_TYPE).toString();
	 	 String url=mServer.getBandsInfoDetail(access_token, itemType, itemId);
		 Intent intent=new Intent(getActivity(),MessageDetail.class);
		 intent.putExtra("url", url);
		 this.startActivity(intent);
		
	}

}

