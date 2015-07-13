package com.sougongcheng.main;

import com.example.sougongcheng.R;
import com.sougongcheng.bean.UserInfo;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.server.Server;
import com.sougongcheng.util.CommenTools;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.ThreadPoolManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	
	
	private Button btn_login;
	
	private EditText et_account;
	
	private EditText et_password;
	
	private TextView tv_regin;
	
	private Server mServer;
	
	private UserInfo userInfo;
	
	private ThreadPoolManager mPoolManager;
	
	private GetShareDatas mGetShareDatas;
	
	private Handler mHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			
			if(msg.what==1)
			{
				if(userInfo.status==0)
				{
				Toast.makeText(LoginActivity.this, CommenTools.getResult(userInfo.status), Toast.LENGTH_SHORT).show();
				mGetShareDatas.insertStringMessage(MConstants.ACCESS_TOKEN, userInfo.access_token);
				mGetShareDatas.insertStringMessage(MConstants.TELNUM, userInfo.telnum);
				mGetShareDatas.insertIntMessage(MConstants.SEX, userInfo.sex);
				mGetShareDatas.insertIntMessage(MConstants.STATUS, userInfo.status);
				Intent intent=new Intent(LoginActivity.this, MainAct.class);
				startActivity(intent);
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
		
		tv_regin.setOnClickListener(this);
	}

	private void initViews() {
		
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, LoginActivity.this);
		
		btn_login=(Button) findViewById(R.id.submit);
		
		et_account=(EditText) findViewById(R.id.et_account);
		
		et_password=(EditText) findViewById(R.id.et_password);
		
		tv_regin=(TextView) findViewById(R.id.tv_regin);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.submit:
			if(inputCheck())
			{
			mServer=Server.getInstance();
			
			mPoolManager=ThreadPoolManager.getInstance();
			
			mPoolManager.addTask(new Runnable() {
				@Override
				public void run() {
					userInfo=mServer.login(et_account.getText().toString(), et_password.getText().toString());
					Message message=mHandler.obtainMessage();
					message.what=1;
					message.sendToTarget();
					}
				});
			}
			break;

		case R.id.tv_regin:
			Intent intent1=new Intent(LoginActivity.this, ReginActivity.class);
			startActivity(intent1);
			break;
		}
	}


	private boolean inputCheck() {
		if(!CommenTools.inputCheck(et_account.getText().toString()))
		{
			et_account.requestFocus();
			Toast.makeText(LoginActivity.this, "�������ǳ�", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!CommenTools.isPassword(et_password.getText().toString()))
		{
			et_password.requestFocus();
			Toast.makeText(LoginActivity.this, "������6-20λ����ȷ����", Toast.LENGTH_SHORT).show();

			return false;
		}
		return true;
	}

}
