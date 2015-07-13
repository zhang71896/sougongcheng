package com.sougongcheng.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

import com.example.sougongcheng.R;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.ui.widget.MyProgressDialog;
import com.sougongcheng.util.CommenTools;

public class ReginActivity extends Activity implements OnClickListener{
	
	private EditText et_account;
	
	private EditText et_centify_code;
	
	private TextView tv_getcode;
	
	private Button btn_next_step;
	
	private TimeCount time;
	
	private MyProgressDialog myProgressDialog;

	private Handler mHandler=new Handler()
	{
		public void handleMessage(Message msg) {
			if(msg.what==1)
			{
			if(myProgressDialog.isShowing())
			{
				myProgressDialog.cancel();
			}
			}
		};
		
	};
	EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
        	if(result==-1)
        	{
        		if(event==2)
        		{
        			//Intent intent=new Intent(ReginActivity.this, ReginActivity2.class);
        			Toast.makeText(ReginActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
        			Message message=mHandler.obtainMessage();
        			message.what=1;
        			message.sendToTarget();
        		}
        	}
        } 
	}; 
  OnSendMessageHandler er=new OnSendMessageHandler() {
	
	@Override
	public boolean onSendMessage(String arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.e("onsendMessage", "arg0:"+arg0+"arg1:"+arg1);
		Toast.makeText(ReginActivity.this, "获得验证码成功", Toast.LENGTH_SHORT).show();
		return false;
	}
};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initViews();
		iniClickListenner();
	}
	private void iniClickListenner() {
		
		btn_next_step.setOnClickListener(this);
		
		tv_getcode.setOnClickListener(this);
	}
	private void initViews() {
		
		setContentView(R.layout.act_regin1);
		
		SMSSDK.initSDK(ReginActivity.this,MConstants.SMS_APP_KEY,MConstants.SMS_APP_CODE);
	
		SMSSDK.registerEventHandler(eh);
		
		et_account=(EditText) findViewById(R.id.et_account);
		
		et_centify_code=(EditText) findViewById(R.id.et_centify_code);

		tv_getcode=(TextView) findViewById(R.id.tv_getcode);

		btn_next_step=(Button) findViewById(R.id.btn_next_step);
		
		time = new TimeCount(60000, 1000);

	}
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_next_step:
			if(CommenTools.inputCheck(et_centify_code.getText().toString()))
			{
			myProgressDialog=new MyProgressDialog(ReginActivity.this).createDialog(ReginActivity.this);
		    myProgressDialog.setMessage("正在验证中..");
			myProgressDialog.show();
			Intent intent=new Intent(ReginActivity.this,ReginActivity2.class);
			intent.putExtra("phoneNum", et_account.getText().toString());
			startActivity(intent);
			SMSSDK.submitVerificationCode("86",et_account.getText().toString(),et_centify_code.getText().toString());
			}else
			{
				Toast.makeText(ReginActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();	
				et_centify_code.requestFocus();
			}
			break;

		case R.id.tv_getcode:
			if(CommenTools.isMobileNO(et_account.getText().toString()))
			{
			SMSSDK.getVerificationCode("86",et_account.getText().toString(),er);
			time.start();
			
			}else
			{
			Toast.makeText(ReginActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();	
			et_account.requestFocus();
			}
			break;
		}
		
	}
	
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() {//计时完毕时触发
		tv_getcode.setText("重新验证");
		tv_getcode.setClickable(true);
		}
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示
		tv_getcode.setClickable(false);
		tv_getcode.setText(millisUntilFinished /1000+"秒");
		}
	}

}
