package com.sougongcheng.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sougongcheng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sougongcheng.adapter.AdapterSearchProject;
import com.sougongcheng.bean.RecommandInfo;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.main.MessageDetail;
import com.sougongcheng.server.Server;
import com.sougongcheng.ui.widget.HeartProgressBar;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.NetworkUtils;
import com.sougongcheng.util.ThreadPoolManager;

public class FragmentSearchProject extends Fragment implements OnClickListener, OnItemClickListener{
	
	private View myView;

	ViewPager pager = null;
	
    PagerTabStrip tabStrip = null;
    
    ArrayList<View> viewContainter = new ArrayList<View>();
    
    ArrayList<String> titleContainer = new ArrayList<String>();
    
	private PullToRefreshListView mPullRefreshListView;
	
	private LinkedList<String> mListItems;
	
	private ArrayAdapter<String> mAdapter;
	
    //声明rl
    
    private RelativeLayout tab_suggest;
    
    private RelativeLayout tab_agent_compare;
    
    private RelativeLayout tab_project_compare;
    
    private RelativeLayout tab_invite_bids;
    
    private RelativeLayout tab_win_bid ;
    
    //声明words
    private TextView tv_suggest;
    
    private TextView tv_agent_compare;
    
    private TextView tv_project_compare;
    
    private TextView tv_invite_bids;
    
    private TextView tv_win_bid;
    
    
    // 声明line
    
    private TextView line_suggest;
    
    private TextView line_agent_compare;
    
    private TextView line_project_compare;
    
    private TextView line_invite_bids;
    
    private TextView line_win_bid;
    
    private String TAG="tag";
    
    private ListView actualListView;
    
    private int position=0;
    
    private boolean isRefreshing=false;
    
	private ViewPager mPager;
	
	private List<ImageView> imageViews; // 滑动的图片集合

	private String[] titles; // 图片标题
	
	private int[] imageResId; // 图片ID
	
	private List<View> dots; // 图片标题正文的那些点

	private TextView tv_title;
	
	private int currentItem = 0; // 当前图片的索引号
	
	private ScheduledExecutorService scheduledExecutorService;

	private AdapterSearchProject adapterSearchProject;
	
	private ArrayList<Map<String,Object>> mMapList;
	
	private View top_menu;
	
	private View top_tab;
	
	private GetShareDatas mGetShareDatas;
	
	private ThreadPoolManager mPoolManager;
	
	private Server mServer;

	private RecommandInfo recommandInfo;
	
	private String access_token;
	
	DisplayImageOptions options;

	private ImageLoader imageLoader;
	
    HeartProgressBar heartProgressBar;
    
	// 切换当前显示的图片
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mPager.setCurrentItem(currentItem);// 切换当前显示的图片
		};
	};
	private Handler mHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1)
			{
				if(heartProgressBar.isShown())
				{
				heartProgressBar.dismiss();
				}
				changeDataSource();
			}
			
		};
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		imageLoader=ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_person)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_secure)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		myView=inflater.inflate(R.layout.fragment_search_project, null);
		
		initViews();
		
		initdatas();
		
		setOnClickListenner();
		
		return myView;
	}

	private void initdatas() {
		
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, getActivity());
		
		actualListView= mPullRefreshListView.getRefreshableView();
		
		actualListView.setFocusable(true);

		titles = new String[3];
		
		titles[0] = "测试1";
		
		titles[1] = "测试2";
		
		titles[2] = "测试3";
		
		imageViews = new ArrayList<ImageView>();

		// 初始化图片资源
		for (int i = 0; i < 3; i++) {
			ImageView imageView = new ImageView(getActivity());
			imageLoader.displayImage("http://120.25.224.229:8080/pics/cm2_daily_banner22x.jpg", imageView, options);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}
		
		dots = new ArrayList<View>();
		dots.add(myView.findViewById(R.id.v_dot0));
		dots.add(myView.findViewById(R.id.v_dot1));
		dots.add(myView.findViewById(R.id.v_dot2));
		
		tv_title.setText(titles[0]);//
		
		mPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		mPager.setOnPageChangeListener(new MyPageChangeListener());
		
		access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, "");
		
	}
	
	

	private void changeDataSource() {
		// TODO Auto-generated method stub
	
		mMapList=recommandInfo.items;

		adapterSearchProject=new AdapterSearchProject(getActivity(), mMapList, 0);
		
		actualListView.setAdapter(adapterSearchProject);
		
		actualListView.setVisibility(View.VISIBLE);
		

	}

	private void setOnClickListenner() {
		
		tab_suggest.setOnClickListener(this);
		
		tab_agent_compare.setOnClickListener(this);
		
		tab_project_compare.setOnClickListener(this);
		
		tab_invite_bids.setOnClickListener(this);
		
		tab_win_bid.setOnClickListener(this);
		
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
		
		
		actualListView.setOnItemClickListener(this);
		
		mPullRefreshListView.setMode(Mode.BOTH);

	}
	
	private void initViews() {
		
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, getActivity());
		//tab
		tab_suggest=(RelativeLayout) myView.findViewById(R.id.tab_suggest);
		
		tab_agent_compare=(RelativeLayout) myView.findViewById(R.id.tab_agent_compare);

		tab_project_compare=(RelativeLayout) myView.findViewById(R.id.tab_project_compare);
		
		tab_invite_bids=(RelativeLayout) myView.findViewById(R.id.tab_invite_bids);
		
		tab_win_bid=(RelativeLayout) myView.findViewById(R.id.tab_win_bid);
		
		//tv_words
		tv_suggest=(TextView) myView.findViewById(R.id.tv_suggest);
		
		tv_agent_compare=(TextView) myView.findViewById(R.id.tv_agent_compare);
		
		tv_project_compare=(TextView) myView.findViewById(R.id.tv_project_compare);
		
		tv_invite_bids=(TextView) myView.findViewById(R.id.tv_invite_bids);
		
		tv_win_bid=(TextView) myView.findViewById(R.id.tv_win_bid);
		
		
		//line
		line_suggest=(TextView) myView.findViewById(R.id.line_suggest);
		
		line_agent_compare=(TextView) myView.findViewById(R.id.line_agent_compare);
		
		line_project_compare=(TextView) myView.findViewById(R.id.line_project_compare);
		
		line_invite_bids=(TextView) myView.findViewById(R.id.line_invite_bids);
		
		line_win_bid=(TextView) myView.findViewById(R.id.line_win_bid);
		
		//contest_list
		
		mPager = (ViewPager)myView.findViewById(R.id.pager);
		
		tv_title = (TextView)myView.findViewById(R.id.tv_title);
	

		
		mPullRefreshListView = (PullToRefreshListView) myView.findViewById(R.id.content_list);
		
		mPullRefreshListView.setFocusable(true);
		
		//顶部
		top_menu=myView.findViewById(R.id.top_menu);
		
		top_tab=myView.findViewById(R.id.tab_menu);
		
		heartProgressBar=(HeartProgressBar) myView.findViewById(R.id.progressBar);
		
	}
	
	

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.tab_suggest:
			switchTab(0);
			break;

		case R.id.tab_agent_compare:
			switchTab(1);
			break;
		case R.id.tab_project_compare:
			switchTab(2);
			break;
		case R.id.tab_invite_bids:
			switchTab(3);
			break;
		case R.id.tab_win_bid:
			switchTab(4);
			break;
		}
	}

	private void switchTab(int i) {
		
		top_tab.setVisibility(View.GONE);
		
		tabStatus(tv_suggest,line_suggest,0);
		
		tabStatus(tv_agent_compare,line_agent_compare,0);
		
		tabStatus(tv_project_compare,line_project_compare,0);
		
		tabStatus(tv_invite_bids,line_invite_bids,0);
		
		tabStatus(tv_win_bid,line_win_bid,0);
		
		if(i==0)
		{
			top_tab.setVisibility(View.VISIBLE);
			tabStatus(tv_suggest,line_suggest,1);
			getDataSource(0);
			return;
		}else if(i==1)
		{
			tabStatus(tv_agent_compare,line_agent_compare,1);
			getDataSource(1);
			return;
		}else if(i==2)
		{
			tabStatus(tv_project_compare,line_project_compare,1);
			getDataSource(2);
			return;
		}else if(i==3)
		{
			tabStatus(tv_invite_bids,line_invite_bids,1);
			getDataSource(3);
			return;
		}else
		{
			tabStatus(tv_win_bid,line_win_bid,1);
			getDataSource(4);
			return;
		}
		
	}
	
	private void getDataSource(final int i) {
		if(NetworkUtils.isNetworkAvailable(getActivity()))
		{
		if(mServer==null)
		{
		mServer=Server.getInstance();
		}
		if(mPoolManager==null)
		{
		mPoolManager=ThreadPoolManager.getInstance();
		}
		if(heartProgressBar.isStopped())
		{
		heartProgressBar.start();
		Log.e("tag", "heartProgressBar"+heartProgressBar.isShown()+"");
		}
		actualListView.setVisibility(View.INVISIBLE);
		mPoolManager.addTask(new Runnable() {
			public void run() {
					if(i==0)
					{
					recommandInfo=mServer.getRecommandInfo(access_token, "10");
					}else
					{
						String type="";
						if(i==1)
						{
						type="compare_org";
						}else if(i==2)
						{
						type="compare";	
						}else if(i==3)
						{
						type="invite";
						}else if(i==4)
						{
						type="win";
						}
						recommandInfo=mServer.getBandsInfo(type, access_token, "10", "10");
					}
					if(recommandInfo!=null)
					{
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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

	private void tabStatus(TextView words,TextView line, int type)
	{
		
		//未选中
		if(type==0)
		{
			words.setTextSize(14);
			line.setVisibility(View.INVISIBLE);
			words.setTextColor(getResources().getColor(R.color.my_gray));
		}else
		{
			//选中状态
			words.setTextSize(18);
			line.setVisibility(View.VISIBLE);
			words.setTextColor(getResources().getColor(R.color.tab_strip));
		}
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
	
	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}
	
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			tv_title.setText(titles[position]);
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}
	
	@Override
	public void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2, TimeUnit.SECONDS);
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		scheduledExecutorService.shutdown();

		super.onStop();
	}
	
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (mPager) {
				System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
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
		 String itemId=mMapList.get(position-1).get(MConstants.RECOMEND_ITEMS_ID).toString();
		 String itemType=mMapList.get(position-1).get(MConstants.RECOMEND_ITEMS_TYPE).toString();
	 	 String url=mServer.getBandsInfoDetail(access_token, itemType, itemId);
		 Intent intent=new Intent(getActivity(),MessageDetail.class);
		 intent.putExtra("url", url);
		 this.startActivity(intent);
	}
}
