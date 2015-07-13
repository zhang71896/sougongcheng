package com.sougongcheng.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.sougongcheng.contants.MConstants;
import com.sougongcheng.fragment.FragmentMyAccount;
import com.sougongcheng.fragment.FragmentMyCircle;
import com.sougongcheng.fragment.FragmentMyProject;
import com.sougongcheng.fragment.FragmentSearchProject;
import com.sougongcheng.util.CommenTools;
import com.sougongcheng.util.GetShareDatas;
import com.test.finder.R;


@SuppressLint("ResourceAsColor") public class MainAct extends FragmentActivity implements OnClickListener{
	
	private FragmentMyAccount myAccountFragment;
	
	private FragmentMyCircle myCircleFragment;
	
	private FragmentMyProject myProjectFragment;
	
	private FragmentSearchProject searchProjectFragment;
	
	private ViewPager mViewPager;
	
	private FragmentPagerAdapter mAdapter;
	
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	
	private RelativeLayout rl_search_project;
	
	private RelativeLayout rl_my_project;
	
	private RelativeLayout rl_my_circle;
	
	private RelativeLayout rl_my_account;
	
	private EditText search_title;
	
	private ImageButton search_btn;
	
	private TextView tv_send;
	
	private GetShareDatas mGetShareDatas;
	
	private String access_token;

	private String access_token_alias;
	
	private long firstTime;
	
	private long secondTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initViews();
		
		initViewPager();
		
		initClickListenner();

	}

	private void initClickListenner() {
		// TODO Auto-generated method stub
		rl_search_project.setOnClickListener(this);
		
		rl_my_project.setOnClickListener(this);

		rl_my_circle.setOnClickListener(this);

		rl_my_account.setOnClickListener(this);
		
		search_btn.setOnClickListener(this);
		
		tv_send.setOnClickListener(this);
		
		search_title.setOnClickListener(this);
		
	}

	private void initViewPager() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager) findViewById(R.id.container);

		searchProjectFragment=new FragmentSearchProject();
		
		myProjectFragment=new FragmentMyProject();
		
		myCircleFragment=new FragmentMyCircle();
		
		myAccountFragment=new FragmentMyAccount();
		
		mFragments.add(searchProjectFragment);
		
		mFragments.add(myProjectFragment);

		mFragments.add(myCircleFragment);

		mFragments.add(myAccountFragment);
		
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override
			public int getCount()
			{
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int position)
			{
			
				return mFragments.get(position);
			
			}
		};
		
		mViewPager.setAdapter(mAdapter);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				switchTabColor(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		mViewPager.setCurrentItem(0);

	}

	 private void switchTabColor(int position) {
		// TODO Auto-generated method stub
		rl_search_project.setBackgroundColor(getResources().getColor(R.color.my_gray));
		
		rl_my_project.setBackgroundColor(getResources().getColor(R.color.my_gray));

		rl_my_circle.setBackgroundColor(getResources().getColor(R.color.my_gray));

		rl_my_account.setBackgroundColor(getResources().getColor(R.color.my_gray));

		if(position==0)
		{
			rl_search_project.setBackgroundColor(getResources().getColor(R.color.my_red));
			return;
		}else if(position==1)
		{
			rl_my_project.setBackgroundColor(getResources().getColor(R.color.my_red));
			return;
			
		}else if(position==2)
		{
			rl_my_circle.setBackgroundColor(getResources().getColor(R.color.my_red));
			return;

		}else
		{
			rl_my_account.setBackgroundColor(getResources().getColor(R.color.my_red));
			return;
		}
	}
	
	private void initViews() {
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, MainAct.this);
		
		access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, " ");
		
		if(CommenTools.inputCheck(access_token))
		{
		access_token_alias=(String) access_token.subSequence(0, access_token.indexOf("-"));
		}
		
		 JPushInterface.setDebugMode(true);
		 // 设置开启日志,发布时请关闭日志
         JPushInterface.init(this); 
		
         JPushInterface.resumePush(getApplicationContext());
         
         
         JPushInterface.setAlias(MainAct.this, access_token_alias, new TagAliasCallback() {
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				
				Log.e("tag","result: "+arg0);
				
				if(arg0==1)
				{
					Toast.makeText(MainAct.this, "推送设置成功", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		setContentView(R.layout.act_main);
		
		rl_search_project=(RelativeLayout) findViewById(R.id.rl_search_project);

		rl_my_project=(RelativeLayout) findViewById(R.id.rl_my_project);

		rl_my_circle=(RelativeLayout) findViewById(R.id.rl_my_circle);

		rl_my_account=(RelativeLayout) findViewById(R.id.rl_my_account);
		
		search_title=(EditText) findViewById(R.id.search_title);
		
		search_btn=(ImageButton) findViewById(R.id.search_btn);
		
		tv_send=(TextView) findViewById(R.id.tv_send);
		
		rl_search_project.setBackgroundColor(getResources().getColor(R.color.my_red));
		
		rl_my_project.setBackgroundColor(getResources().getColor(R.color.my_gray));

		rl_my_circle.setBackgroundColor(getResources().getColor(R.color.my_gray));

		rl_my_account.setBackgroundColor(getResources().getColor(R.color.my_gray));
		

	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.rl_search_project:
			mViewPager.setCurrentItem(0);
			switchTabColor(0);
			break;

		case R.id.rl_my_project:
			mViewPager.setCurrentItem(1);
			switchTabColor(1);
			break;
		case R.id.rl_my_circle:
			mViewPager.setCurrentItem(2);
			tv_send.setVisibility(View.VISIBLE);
			switchTabColor(2);
			break;

		case R.id.rl_my_account:
			mViewPager.setCurrentItem(3);
			switchTabColor(3);
			break;
			
		case R.id.tv_send:
			Intent intent=new Intent(MainAct.this, SendMessageActivity.class);
			startActivity(intent);
			break;
			
		case R.id.search_btn:
			Intent intent1=new Intent(MainAct.this, SearchActivity.class);
			startActivity(intent1);
			break;
		case R.id.search_title:
			Intent intent2=new Intent(MainAct.this, SearchActivity.class);
			startActivity(intent2);
			break;
		}
		
	}
	
	@Override 
    public boolean onKeyUp(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK) { 
            long secondTime = System.currentTimeMillis(); 
            if (secondTime - firstTime > 800) {//如果两次按键时间间隔大于800毫秒，则不退出 
                Toast.makeText(MainAct.this, "再按一次退出程序...", 
                        Toast.LENGTH_SHORT).show(); 
                firstTime = secondTime;//更新firstTime 
                return true; 
            } else { 
            	android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);//否则退出程序 
            } 
        } 
        return super.onKeyUp(keyCode, event); 
    } 

}
