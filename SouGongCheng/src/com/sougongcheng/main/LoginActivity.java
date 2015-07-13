package com.sougongcheng.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.sougongcheng.bean.UserInfo;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.server.Server;
import com.sougongcheng.ui.widget.SpotsDialog;
import com.sougongcheng.util.CommenTools;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.NetworkUtils;
import com.sougongcheng.util.ThreadPoolManager;
import com.test.finder.R;

public class LoginActivity extends Activity implements OnClickListener{
	
	
	private Button btn_login;
	
	private EditText et_account;
	
	private EditText et_password;
	
	private Button btn_regin;
	
	private Server mServer;
	
	private UserInfo userInfo;
	
	private ThreadPoolManager mPoolManager;
	
	private GetShareDatas mGetShareDatas;
	
	private SpotsDialog spotsDialog;
	
	private Handler mHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			
			if(msg.what==1)
			{
				if(userInfo.status==0)
				{
					if(spotsDialog.isShowing())
					{
						spotsDialog.dismiss();
					}
				Toast.makeText(LoginActivity.this, CommenTools.getResult(userInfo.status), Toast.LENGTH_SHORT).show();
				mGetShareDatas.insertStringMessage(MConstants.ACCESS_TOKEN, userInfo.access_token);
				mGetShareDatas.insertStringMessage(MConstants.TELNUM, userInfo.telnum);
				mGetShareDatas.insertIntMessage(MConstants.SEX, userInfo.sex);
				mGetShareDatas.insertIntMessage(MConstants.STATUS, userInfo.status);
				Intent intent=new Intent(LoginActivity.this, MainAct.class);
				startActivity(intent);
				finish();
				}else
				{
				Toast.makeText(LoginActivity.this, CommenTools.getResult(userInfo.status), Toast.LENGTH_SHORT).show();
				}
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_login);
		
		initViews();
		
		setOnClickListenner();
	}
	

	private void setOnClickListenner() {
		
		btn_login.setOnClickListener(this);
		
		btn_regin.setOnClickListener(this);
	}

	private void initViews() {
		
		 JPushInterface.setDebugMode(true);
		 // 设置开启日志,发布时请关闭日志
         JPushInterface.init(this); 
		
         JPushInterface.resumePush(getApplicationContext());
		
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, LoginActivity.this);
		
		btn_login=(Button) findViewById(R.id.submit);
		
		et_account=(EditText) findViewById(R.id.et_account);
		
		et_password=(EditText) findViewById(R.id.et_password);
		
		btn_regin=(Button) findViewById(R.id.btn_regin);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.submit:
			if(inputCheck())
			{
				if(NetworkUtils.isNetworkAvailable(LoginActivity.this))
				{
			
			mServer=Server.getInstance();
			
			mPoolManager=ThreadPoolManager.getInstance();
			
			spotsDialog=new SpotsDialog(this, "登录中...");
			
			spotsDialog.show();
			
			mPoolManager.addTask(new Runnable() {
				@Override
				public void run() {
					userInfo=mServer.login(et_account.getText().toString(), et_password.getText().toString());
					try {
						Thread.sleep(1000);
						if(userInfo!=null)
						{
							Message message=mHandler.obtainMessage();
							message.what=1;
							message.sendToTarget();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					}
				});
				}else
				{
					Toast.makeText(LoginActivity.this, "当前网络不可用", Toast.LENGTH_SHORT).show();
				}
			}
			break;

		case R.id.btn_regin:
			Intent intent1=new Intent(LoginActivity.this, ReginActivity2.class);
			startActivity(intent1);
			break;
		}
	}


	private boolean inputCheck() {
		if(!CommenTools.inputCheck(et_account.getText().toString()))
		{
			et_account.requestFocus();
			Toast.makeText(LoginActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!CommenTools.isPassword(et_password.getText().toString()))
		{
			et_password.requestFocus();
			Toast.makeText(LoginActivity.this, "请输入6-20位的正确密码", Toast.LENGTH_SHORT).show();

			return false;
		}
		return true;
	}

}
