package com.sougongcheng.fragment;

import java.util.ArrayList;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.sougongcheng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.sougongcheng.adapter.AdapterMyCircle;
import com.sougongcheng.bean.CommentsInfo;
import com.sougongcheng.bean.RecommandInfo;
import com.sougongcheng.bean.UserInfo;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.server.Server;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.ThreadPoolManager;

public class FragmentMyCircle extends Fragment implements OnItemClickListener{
	
	private View myView;
	
	private Server mServer;
	
	private String test_str;
	
	private ThreadPoolManager mPoolManager;
	
	private UserInfo userInfo;
	
	private RecommandInfo recommandInfo;
	
	private ArrayList<Map<String,Object>> mMapList;
	
    private ListView actualListView;
    
	private PullToRefreshListView mPullRefreshListView;

    private boolean isRefreshing=false;
    
	private GetShareDatas mGetShareDatas;
    
    private String access_token;
    
    private CommentsInfo commentsInfo;
    
    private AdapterMyCircle adapterMyCircle;
    
	private Handler mHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1)
			{
				if(commentsInfo.status==0)
				{
				mMapList=commentsInfo.comments;
				adapterMyCircle=new AdapterMyCircle(getActivity(), mMapList);
				actualListView.setAdapter(adapterMyCircle);
				}else
				{
					
				}
			}
		};
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		myView=inflater.inflate(R.layout.fragment_my_circle, null);	
		
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

	private void initDatas() {
		mServer=Server.getInstance();
		mPoolManager=ThreadPoolManager.getInstance();
		mPoolManager.addTask(new Runnable() {
			@Override
			public void run() {
				commentsInfo=mServer.getCommments("all", access_token, "10", "0","0");
				if(commentsInfo!=null)
				{
				Message message=mHandler.obtainMessage();
				message.what=1;
				message.sendToTarget();
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
		Toast.makeText(getActivity(), "当前"+position+"被点击", Toast.LENGTH_SHORT).show();
		
	}


}
