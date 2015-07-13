package com.sougongcheng.main;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.sougongcheng.R;
import com.sougongcheng.bean.Status;
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
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SendMessageActivity extends FragmentActivity
implements OnClickListener{
	
	private TextView tv_back;
	
	private TextView tv_send;
	
	private EditText et_send_content;
	
	private CheckBox cb_input_method;
	
	private ThreadPoolManager mPoolManager;
	
	private GetShareDatas mGetShareDatas;

	private Server mServer;
	
	private Status status;
	
	private String access_token;
	

	private Handler mHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			
			if(msg.what==1)
			{
				if(status.status==0)
				{
				Toast.makeText(SendMessageActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
				finish();
				}else
				{
			    Toast.makeText(SendMessageActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
			    finish();
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
			
			tv_back.setOnClickListener(this);
			
			tv_send.setOnClickListener(this);
			
		}


		private void initViews() {
			
			mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, SendMessageActivity.this);
			
			access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, "");
			
			setContentView(R.layout.act_send_message);
			
			tv_back=(TextView) findViewById(R.id.tv_back);
			
			tv_send=(TextView) findViewById(R.id.tv_send);
			
			et_send_content=(EditText) findViewById(R.id.et_send_content);


		
		}


		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			
			case R.id.tv_back:
				finish();
				break;

			case R.id.tv_send:
				send();
				break;
				
			}
			
		}

		private void send() {
			mServer=Server.getInstance();
			
			mPoolManager=ThreadPoolManager.getInstance();

			mPoolManager.addTask(new Runnable() {
				@Override
				public void run() {
					status=mServer.manageComment("add", access_token, et_send_content.getText().toString(), "0");
					if(status!=null)
					{
					Message message=mHandler.obtainMessage();
					message.what=1;
					message.sendToTarget();
					}else 
					{
					Toast.makeText(SendMessageActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
					}
					}
				});
			}
			
}
