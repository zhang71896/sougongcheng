package com.sougongcheng.main;

import cn.jpush.android.api.JPushInterface;

import com.sougongcheng.contants.MConstants;
import com.sougongcheng.util.CommenTools;
import com.sougongcheng.util.GetShareDatas;
import com.test.finder.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.TransactionTooLargeException;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.RelativeLayout;

public class LoadingAnimationActivity extends Activity{
	
	private Animation mAnimation;
	
	private RelativeLayout animLayout;
	
	private String access_token;
	
	private GetShareDatas mGetShareDatas;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_animation);
		
		 JPushInterface.setDebugMode(true);
		 // 设置开启日志,发布时请关闭日志
         JPushInterface.init(this); 
		
         JPushInterface.resumePush(getApplicationContext());
		
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, LoadingAnimationActivity.this);
		
		access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, "");
		
		animLayout=(RelativeLayout) findViewById(R.id.rl_anim);
		
		mAnimation=new AlphaAnimation(0, 1);
		
		mAnimation.setDuration(1000);
		
		mAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				if(!CommenTools.inputCheck(access_token))
				{
					Intent intent=new Intent(LoadingAnimationActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}else
				{
					Intent intent=new Intent(LoadingAnimationActivity.this, MainAct.class);
					startActivity(intent);
					finish();
				}
			}
		});
		animLayout.setAnimation(mAnimation);
		animLayout.startAnimation(mAnimation);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(getApplicationContext());
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(getApplicationContext());
	}

}
