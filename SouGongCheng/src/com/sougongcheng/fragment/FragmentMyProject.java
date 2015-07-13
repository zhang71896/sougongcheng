package com.sougongcheng.fragment;

import java.util.ArrayList;
import java.util.Map;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sougongcheng.adapter.AdapterMyProject;
import com.sougongcheng.bean.RecommandInfo;
import com.sougongcheng.bean.SearchMachine;
import com.sougongcheng.bean.Status;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.main.MessageDetail;
import com.sougongcheng.main.SearchMachineActivity;
import com.sougongcheng.server.Server;
import com.sougongcheng.ui.widget.ProgressWheel;
import com.sougongcheng.util.CommenTools;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.NetworkUtils;
import com.sougongcheng.util.ThreadPoolManager;
import com.test.finder.R;

public class FragmentMyProject extends Fragment implements OnItemClickListener, OnClickListener{
	
    private ListView actualListView;
    
	private PullToRefreshListView mPullRefreshListView;
    
    private ThreadPoolManager mPoolManager;
	
	private Server mServer;

	private RecommandInfo recommandInfo;
	
	private View myView;
	
	private String type;
	
	private AdapterMyProject adapterMyProject;
	
	private ArrayList<Map<String,Object>> mMapList;
	
	private ArrayList<Map<String,Object>> mSearchMachineMapList;
	
	private GetShareDatas mGetShareDatas;
	
	private String access_token;
	
    private boolean isRefreshing=false;

    private ProgressWheel progressWheel;
    
    private ImageView iv_add;
    
	private ArrayAdapter<String> adapter ;
	
	private ListView follower_list;
	
	private SearchMachine searchMachine;
	
	private Status status;
	
	private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("changeLike"))
			{
				initDatas();
			}else if(intent.getAction().equals("changeSearchMachine"))
			{
				initDatas();
			}
		}
	};
	
	private Handler mHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1)
			{
				if(recommandInfo.status==0)
				{
				changeDataSource();
				}
 			}else if(msg.what==2)
 			{
 				if(searchMachine.status==0)
				{
				changeDataMachine();
				}
 			}else if(msg.what==3)
 			{
 				if(status.status==0)
 				{
 					Toast.makeText(getActivity(), "跟踪器删除成功", Toast.LENGTH_SHORT).show();
 					Intent intent = new Intent();
 	                intent.setAction("changeSearchMachine");        //设置Action
 	                getActivity().sendBroadcast(intent);   
 				}else
 				{
 					Toast.makeText(getActivity(), "跟踪器删除失败", Toast.LENGTH_SHORT).show();
 				}
 			}
			
		};
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 IntentFilter dynamic_filter = new IntentFilter();
	     dynamic_filter.addAction("changeLike");            //添加动态广播的Action
	     dynamic_filter.addAction("changeSearchMachine");
	     getActivity().registerReceiver(mBroadcastReceiver, dynamic_filter); 
	}
	
	protected void changeDataMachine() {
		mSearchMachineMapList=searchMachine.keywords;
		initSearchMachine(mSearchMachineMapList);
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
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	private void initClickListenner() {
		
		iv_add.setOnClickListener(this);
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
		
		follower_list=(ListView) myView.findViewById(R.id.follower_list);

		access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, "");
		
		mPullRefreshListView = (PullToRefreshListView) myView.findViewById(R.id.content_list);
		
		mPullRefreshListView.setFocusable(true);
		
		actualListView= mPullRefreshListView.getRefreshableView();
		
		actualListView.setFocusable(true);
		
		actualListView.setOnItemClickListener(this);
		
		iv_add=(ImageView) myView.findViewById(R.id.iv_add);
		
		mPullRefreshListView.setMode(Mode.BOTH);
		
		
	}

	private void initSearchMachine(final ArrayList<Map<String,Object>> result)
	{
		String[] mStrings=new String[result.size()];
		//获取共有多少组追踪器
		for(int i=0;i<mStrings.length;i++)
		{
			//最后的标签值
			String values="";
			
			//标签个数统计
			int count=0;
			//类型标签
			
			//获取类型标签的个数
			int type_num=result.get(i).get(MConstants.SEARCH_BIND_TYPE_NUM)==null?0:Integer.parseInt(result.get(i).get(MConstants.SEARCH_BIND_TYPE_NUM).toString());
;
			
			//获取区域标签的个数
			int area_num=result.get(i).get(MConstants.SEARCH_BIND_AREA_NUM)==null?0:Integer.parseInt(result.get(i).get(MConstants.SEARCH_BIND_AREA_NUM).toString());
			
			//获取行业标签的个数
			int indu_num=result.get(i).get(MConstants.SEARCH_BIND_INDU_NUM)==null?0:Integer.parseInt(result.get(i).get(MConstants.SEARCH_BIND_INDU_NUM).toString());
			
			//获取关键字的个数
			int key_words_num=result.get(i).get(MConstants.SEARCH_BIND_KEYWORDS_NUM)==null?0:Integer.parseInt(result.get(i).get(MConstants.SEARCH_BIND_KEYWORDS_NUM).toString());
			
			int total_num=type_num+area_num+indu_num+key_words_num;
			
			for(int j=0;j<type_num;j++)
			{
				count++;
				if(count==total_num)
				{
				values+=result.get(i).get(MConstants.SEARCH_BIND_TYPE+j).toString();
				}else
				{
				values+=result.get(i).get(MConstants.SEARCH_BIND_TYPE+j).toString()+",";
				}
			}
			for(int j=0;j<area_num;j++)
			{
				count++;
				if(count==total_num)
				{
				values+=result.get(i).get(MConstants.SEARCH_BIND_AREA+j).toString();
				}else
				{
				values+=result.get(i).get(MConstants.SEARCH_BIND_AREA+j).toString()+",";
				}
			}
			for(int j=0;j<indu_num;j++)
			{
				count++;
				if(count==total_num)
				{
				values+=result.get(i).get(MConstants.SEARCH_BIND_INDU+j).toString();
				}else
				{
				values+=result.get(i).get(MConstants.SEARCH_BIND_INDU+j).toString()+",";
				}
			}
			for(int j=0;j<key_words_num;j++)
			{
				count++;
				if(count==total_num)
				{
				values+=result.get(i).get(MConstants.SEARCH_BIND_KEYWORDS+j).toString();
				}else
				{
				values+=result.get(i).get(MConstants.SEARCH_BIND_KEYWORDS+j).toString()+",";
				}
			}
			values=CommenTools.enToCh(values);
			mStrings[i]=values;
		}
		adapter = new ArrayAdapter<String>(  
                getActivity(),  
                R.layout.item_search,//只能有一个定义了id的TextView  
                mStrings);//data既可以是数组，也可
		
		follower_list.setAdapter(adapter);
		
		follower_list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final String search_machine_id=result.get(position).get(MConstants.SEARCH_MACHINE_ID).toString();
				ContextThemeWrapper contextThemeWrapper=new ContextThemeWrapper(getActivity(), R.style.dialog);
				AlertDialog.Builder builder = new AlertDialog.Builder(contextThemeWrapper);
				builder.setTitle("你确定要删除该跟踪器么？");  
				builder.setPositiveButton("确定",new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mPoolManager.addTask(new Runnable() {
							public void run() {
								status=mServer.delSelfSearch(access_token,search_machine_id);
								if(status!=null)
								{
									Message message=mHandler.obtainMessage();
									message.what=3;
									message.sendToTarget();
								}
							}
						});
					}
					
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}
				);
				builder.create().show();
				return false;
			}
		});
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
				recommandInfo=mServer.getBandsInfo(type, access_token, "10", "0");
				searchMachine=mServer.getSearchMachine(access_token, "0");
				if(recommandInfo!=null)
				{
					Message message=mHandler.obtainMessage();
					message.what=1;
					message.sendToTarget();
				}
				if(searchMachine!=null)
				{
					Message message=mHandler.obtainMessage();
					message.what=2;
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
	
	private class GetDataTask extends AsyncTask<Void, Void, ArrayList<Map<String,Object>>> {

	/*	@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return mStrings;
		}*/

		@Override
		protected void onPostExecute(ArrayList<Map<String,Object>> result) {
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

		@Override
		protected ArrayList<Map<String, Object>> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
	}


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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_add:
			Intent intent=new Intent(getActivity(), SearchMachineActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}

}

