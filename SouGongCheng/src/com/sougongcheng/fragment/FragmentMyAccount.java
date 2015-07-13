package com.sougongcheng.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.style.BulletSpan;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sougongcheng.contants.MConstants;
import com.sougongcheng.main.ContactUs;
import com.sougongcheng.main.LoginActivity;
import com.sougongcheng.util.GetShareDatas;
import com.test.finder.R;

public class FragmentMyAccount extends Fragment implements OnClickListener{
	
	private RelativeLayout rl_name;
	
	private RelativeLayout rl_account;
	
	private RelativeLayout rl_sex;
	
	private RelativeLayout rl_modify_ps;
	
	private RelativeLayout rl_exit;
	
	private RelativeLayout rl_del;
	
	private RelativeLayout rl_about;
	
	private TextView tv_name;
	
	private TextView tv_sex;
	
	private TextView tv_tel_content;
	
	private ImageView iv_header;
	
	private View myView;
	
	private String mySex;
	
	private String myPhone;
	
	private String myName;
	
	private GetShareDatas mGetShareDatas;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		myView=inflater.inflate(R.layout.fragment_my_account, null);
		
		initViews();
		
		initDatas();
		
		setOnClickListenner();
		
		return myView;
	}

	private void initDatas() {
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, getActivity());
		myName=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, "01-nick");
		myName=myName.subSequence(myName.indexOf("-")+1, myName.length()).toString();
		myPhone=mGetShareDatas.getStringMessage(MConstants.TELNUM, "110");
		if((mGetShareDatas.getIntMessage(MConstants.SEX, 1))==1)
		{
			mySex="男";
			iv_header.setBackgroundResource(R.drawable.male);
		}else
		{
			mySex="女";
			iv_header.setBackgroundResource(R.drawable.female);
		}
		tv_name.setText(myName);
		tv_sex.setText(mySex);
		tv_tel_content.setText(myPhone);
		
	}

	private void setOnClickListenner() {
		
		rl_exit.setOnClickListener(this);
		
		rl_del.setOnClickListener(this);
		
		rl_about.setOnClickListener(this);
	}

	private void initViews() {
		
		rl_name=(RelativeLayout) myView.findViewById(R.id.rl_name);
		
		rl_account=(RelativeLayout) myView.findViewById(R.id.rl_account);

		rl_sex=(RelativeLayout) myView.findViewById(R.id.rl_sex);
		
		rl_modify_ps=(RelativeLayout) myView.findViewById(R.id.rl_modify_ps);
		
		rl_del=(RelativeLayout) myView.findViewById(R.id.rl_del);

		rl_about=(RelativeLayout) myView.findViewById(R.id.rl_about);
		
		rl_exit=(RelativeLayout) myView.findViewById(R.id.rl_exit);
		
		tv_name=(TextView) myView.findViewById(R.id.tv_name);
		
		tv_sex=(TextView) myView.findViewById(R.id.tv_sex);
		
		tv_tel_content=(TextView) myView.findViewById(R.id.tv_tel_content);
		
		iv_header=(ImageView) myView.findViewById(R.id.iv_header);

	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.rl_modify_ps:
			modifyPsw();
			break;

		case R.id.rl_del:
			
			break;
			
		case R.id.rl_about:
			Intent intent=new Intent(getActivity(), ContactUs.class);
			startActivity(intent);
			break;
		case R.id.rl_exit:
			exit();
			break;
		}
	}

	private void exit() {
		
		ContextThemeWrapper contextThemeWrapper=new ContextThemeWrapper(getActivity(), R.style.dialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(contextThemeWrapper);
		builder.setTitle("你真的要退出么？");  
		builder.setPositiveButton("确定",new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.exit(1);
			}
			
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.create().show();
		
	}

	private void modifyPsw() {
		
	}
}
