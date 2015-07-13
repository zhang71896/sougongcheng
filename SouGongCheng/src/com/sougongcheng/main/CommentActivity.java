package com.sougongcheng.main;


import com.example.sougongcheng.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CommentActivity extends Activity implements OnClickListener{
	
	
	private TextView tv_back;
	
	private Button btn_send;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initViews();
		
		initDatas();
		
		setOnClickListenner();
	}

	private void setOnClickListenner() {
	}

	private void initDatas() {
		tv_back.setOnClickListener(this);
		
		btn_send.setOnClickListener(this);
	}

	private void initViews() {
		
		setContentView(R.layout.act_comment);
		
		tv_back=(TextView) findViewById(R.id.tv_back);
		
		btn_send=(Button) findViewById(R.id.btn_send);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			send();
			break;

		case R.id.tv_back:
			finish();
			break;
		}
	}

	private void send() {
		
	}

}
