package com.sougongcheng.main;

import com.test.finder.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ContactUs extends Activity implements OnClickListener{
	
	private TextView tv_back;
	private Button btn_attention;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_contact_us );
		initViews(); 
		initClickListenner();
	}

	private void initClickListenner() {
		tv_back.setOnClickListener(this);
		btn_attention.setOnClickListener(this);
	}

	private void initViews() {
		tv_back=(TextView) findViewById(R.id.tv_back);
		btn_attention=(Button) findViewById(R.id.btn_attention);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;

		case R.id.btn_attention:
			attention();
			break;
		}
	}

	private void attention() {
		ContextThemeWrapper contextThemeWrapper=new ContextThemeWrapper(ContactUs.this, R.style.dialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(contextThemeWrapper);
		builder.setTitle("注意事项");  
		builder.setMessage(R.string.text_attention_please);
		builder.setPositiveButton("确定",new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
			
		});
		builder.create().show();
	}

}
