package com.sougongcheng.fragment;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sougongcheng.adapter.AdapterMyCircle;
import com.sougongcheng.bean.CommentsInfo;
import com.sougongcheng.bean.RecommandInfo;
import com.sougongcheng.bean.UserInfo;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.main.CommentActivity;
import com.sougongcheng.server.Server;
import com.sougongcheng.ui.widget.HeartProgressBar;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.NetworkUtils;
import com.sougongcheng.util.ThreadPoolManager;
import com.test.finder.R;

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
    
    HeartProgressBar heartProgressBar;
    
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
				actualListView.setVisibility(View.VISIBLE);
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
		
		initClickListenner();
		
		initDatas();
		
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
		if(NetworkUtils.isNetworkAvailable(getActivity()))
		{
			actualListView.setVisibility(View.INVISIBLE);
			mServer=Server.getInstance();
			mPoolManager=ThreadPoolManager.getInstance();
			mPoolManager.addTask(new Runnable() {
			@Override
			public void run() {
				commentsInfo=mServer.getCommments("all", access_token, "10", "0","0");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(commentsInfo!=null)
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
				Thread.sleep(2000);
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
		
		Intent intent=new Intent(getActivity(),CommentActivity.class);
		intent.putExtra(MConstants.COMMENTS_ID, mMapList.get(position-1).get(MConstants.COMMENTS_ID).toString());
		intent.putExtra(MConstants.COMMENTS_USER_ID, mMapList.get(position-1).get(MConstants.COMMENTS_USER_ID).toString());
		intent.putExtra(MConstants.COMMENTS_USER_NAME, mMapList.get(position-1).get(MConstants.COMMENTS_USER_NAME).toString());
		intent.putExtra(MConstants.COMMENTS_LIKE_NUMS, mMapList.get(position-1).get(MConstants.COMMENTS_LIKE_NUMS).toString());
		intent.putExtra(MConstants.COMMENTS_SHARE_NUMS, mMapList.get(position-1).get(MConstants.COMMENTS_SHARE_NUMS).toString());
		intent.putExtra(MConstants.COMMENTS_CONTENTS, mMapList.get(position-1).get(MConstants.COMMENTS_CONTENTS).toString());
		intent.putExtra(MConstants.COMMENTS_CREATE_TIME, mMapList.get(position-1).get(MConstants.COMMENTS_CREATE_TIME).toString());
		intent.putExtra(MConstants.COMMENTS_IS_LIKE, mMapList.get(position-1).get(MConstants.COMMENTS_IS_LIKE).toString());
		intent.putExtra(MConstants.COMMENTS_USER_SEX, mMapList.get(position-1).get(MConstants.COMMENTS_USER_SEX).toString());
		intent.putExtra(MConstants.COMMENTS_IS_STORE, mMapList.get(position-1).get(MConstants.COMMENTS_IS_STORE).toString());
		startActivity(intent);
	}


}
