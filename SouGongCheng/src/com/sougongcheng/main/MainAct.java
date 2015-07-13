package com.sougongcheng.main;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.sougongcheng.R;
import com.sougongcheng.fragment.FragmentMyAccount;
import com.sougongcheng.fragment.FragmentMyCircle;
import com.sougongcheng.fragment.FragmentMyProject;
import com.sougongcheng.fragment.FragmentSearchProject;


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
		
		setContentView(R.layout.main);
		
		rl_search_project=(RelativeLayout) findViewById(R.id.rl_search_project);

		rl_my_project=(RelativeLayout) findViewById(R.id.rl_my_project);

		rl_my_circle=(RelativeLayout) findViewById(R.id.rl_my_circle);

		rl_my_account=(RelativeLayout) findViewById(R.id.rl_my_account);
		
		search_title=(EditText) findViewById(R.id.search_title);
		
		search_btn=(ImageButton) findViewById(R.id.search_btn);
		
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
			switchTabColor(2);
			break;

		case R.id.rl_my_account:
			mViewPager.setCurrentItem(3);
			switchTabColor(3);
			break;
		}
		
	}

}
