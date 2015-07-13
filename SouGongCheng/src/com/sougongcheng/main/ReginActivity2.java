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
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.sougongcheng.R;
import com.sougongcheng.bean.AccessStatus;
import com.sougongcheng.server.Server;
import com.sougongcheng.util.CommenTools;
import com.sougongcheng.util.ThreadPoolManager;

public class ReginActivity2 extends Activity implements OnClickListener{

	
	private EditText et_nick_name;
	
	private EditText et_password;
	
	private Button btn_done;
	
	private RadioButton sex_male;
	
	private RadioButton sex_female;
	
	private ThreadPoolManager mPoolManager;
	
	private Server mServer;
	
	private String phoneNums;
	
	private String sex;
	
	private AccessStatus accessStatus;
	
	private Handler mHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			
			if(msg.what==1)
			{
				if(accessStatus.status==0)
				{
				Toast.makeText(ReginActivity2.this, CommenTools.getResult(accessStatus.status), Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(ReginActivity2.this, LoginActivity.class);
				startActivity(intent);
				}else
				{
				Toast.makeText(ReginActivity2.this, CommenTools.getResult(accessStatus.status), Toast.LENGTH_SHORT).show();
				}
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		initClickListenner();
	}

	private void initClickListenner() {
		btn_done.setOnClickListener(this);
	}

	private void initViews() {
		setContentView(R.layout.act_regin2);
		
		et_nick_name=(EditText) findViewById(R.id.et_nick_name);
		
		et_password=(EditText) findViewById(R.id.et_password);
		
		btn_done=(Button) findViewById(R.id.btn_done);
		
		sex_male=(RadioButton) findViewById(R.id.sex_male);
		
		sex_female=(RadioButton) findViewById(R.id.sex_female);
		
		phoneNums=getIntent().getCharSequenceExtra("phoneNum").toString();
		
		if(sex_male.isChecked())
		{
			sex="1";
		}else
		{
			sex="0";
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_done)
		{
			if(inputCheck())
			{
			if(sex_male.isChecked())
			{
				sex="1";
			}else
			{
				sex="0";
			}
			
		mServer=Server.getInstance();
		
		mPoolManager=ThreadPoolManager.getInstance();
		
		mPoolManager.addTask(new Runnable() {
			
			@Override
			public void run() {
				
				accessStatus=mServer.regin(et_nick_name.getText().toString(), et_password.getText().toString(), sex, phoneNums);
				Message message=mHandler.obtainMessage();
				message.what=1;
				message.sendToTarget();
				}
			});
			}
		}
	}

	private boolean inputCheck() {
		if(et_nick_name.getText().toString().equals(phoneNums))
		{
			Toast.makeText(ReginActivity2.this, "昵称不能与手机号一样", Toast.LENGTH_SHORT).show();
			et_nick_name.requestFocus();
			return false;
		}
		if(!CommenTools.inputCheck(et_nick_name.getText().toString()))
		{
			Toast.makeText(ReginActivity2.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
			et_nick_name.requestFocus();
			return false;
		}
		if(!CommenTools.isPassword(et_password.getText().toString()))
		{
			Toast.makeText(ReginActivity2.this, "密码为6-20位之间的字字符或者数字", Toast.LENGTH_SHORT).show();
			et_password.requestFocus();
			return false;
		}
		return true;
	}
}