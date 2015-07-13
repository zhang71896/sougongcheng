package com.sougongcheng.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sougongcheng.bean.AccessStatus;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.main.ContactUs;
import com.sougongcheng.server.Server;
import com.sougongcheng.util.CommenTools;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.ThreadPoolManager;
import com.test.finder.R;

public class FragmentMyAccount extends Fragment implements OnClickListener{
	
	private RelativeLayout rl_name;
	
	private RelativeLayout rl_account;
	
	private RelativeLayout rl_sex;
	
	private RelativeLayout rl_modify_ps;
	
	private RelativeLayout rl_exit;
	
	private RelativeLayout rl_del;
	
	private RelativeLayout rl_about;
	
	private RelativeLayout rl_modify_message;
	
	private TextView tv_name;
	
	private TextView tv_sex;
	
	private TextView tv_tel_content;
	
	private ImageView iv_header;
	
	private View myView;
	
	private String mySex;
	
	private String myPhone;
	
	private String myName;
	
	private String myPsw;
	
	private GetShareDatas mGetShareDatas;
	
	private Server mServer;
	
	private ThreadPoolManager mThreadPoolManager;
	
	private String access_token;
	
	private AccessStatus accessStatus;
	
	private Handler mHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1)
			{
				if(accessStatus.status==0)
				{
					Toast.makeText(getActivity(), "密码修改成功", Toast.LENGTH_SHORT).show();
				}else
				{
					Toast.makeText(getActivity(), CommenTools.getResult(accessStatus.status), Toast.LENGTH_SHORT).show();
				}
			}
			if(msg.what==2)
			{
				if(accessStatus.status==0)
				{
					Toast.makeText(getActivity(), "个人信息修改成功", Toast.LENGTH_SHORT).show();
					
					
				}else
				{
					Toast.makeText(getActivity(), CommenTools.getResult(accessStatus.status), Toast.LENGTH_SHORT).show();
				}
			}
		};
	};
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
		access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, "01-nick");
		myPsw=mGetShareDatas.getStringMessage(MConstants.PSW, "123456");
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
		
		rl_modify_ps.setOnClickListener(this);
		
		rl_modify_message.setOnClickListener(this);
	}

	private void initViews() {
		
		rl_modify_message=(RelativeLayout) myView.findViewById(R.id.rl_modify_message);
		
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
		
		mServer=Server.getInstance();
		
		mThreadPoolManager=ThreadPoolManager.getInstance();

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
		case R.id.rl_modify_message:
			modify_message();
			break;
		}
	}

	private void modify_message() {
		
		final AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).create();
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_modify_message, null);
		alertDialog.setView(view);
		alertDialog.show();
		Window window=alertDialog.getWindow();
		window.setContentView(R.layout.dialog_modify_message);
		final EditText et_account=(EditText) window.findViewById(R.id.et_account);
		et_account.setText(myPhone);
		final EditText et_nick_name=(EditText) window.findViewById(R.id.et_nick_name);
		et_nick_name.setText(myName);
		final RadioButton sex_male=(RadioButton) window.findViewById(R.id.sex_male);
		final RadioButton sex_female=(RadioButton) window.findViewById(R.id.sex_female);
		if(mySex.equals("男"))
		{
			sex_male.setChecked(true);
		}else
		{
			sex_female.setChecked(true);
		}
	    Button btn_done=(Button) window.findViewById(R.id.btn_done);
		TextView tv_back=(TextView) window.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		btn_done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(inputCheckMessage())
				{
					mThreadPoolManager.addTask(new Runnable() {
						
						@Override
						public void run() {
							if(!et_account.getText().toString().equals(myPhone))
							{
								Log.e("tag", "myPhone: "+myPhone);
								myPhone=et_account.getText().toString();
								accessStatus=mServer.modifyTelNum(access_token, et_account.getText().toString());
							}
							if(!et_nick_name.getText().toString().equals(myName))
							{
								myName=et_nick_name.getText().toString();
								Log.e("tag", "myName: "+myName);
								accessStatus=mServer.modifyUserName(access_token, et_nick_name.getText().toString());
							}
							if(sex_male.isChecked()&&mySex.equals("男"))
							{
								mySex="男";
								Log.e("tag", "mySex: "+"男");
							}else
							{
								mySex="女";
								Log.e("tag", "mySex: "+"女");
								accessStatus=mServer.modifySex(access_token, sex_male.isChecked()?"1":"0");
							}
							if(accessStatus!=null)
							{
								if(accessStatus.status==1)
								{
									Message message=mHandler.obtainMessage();
									message.what=2;
									message.sendToTarget();
								}
							}
						}
					});
					alertDialog.dismiss();
				}
				
			}

			private boolean inputCheckMessage() {
				if(!CommenTools.isMobileNO(et_account.getText().toString()))
				{
					et_account.requestFocus();
					Toast.makeText(getActivity(), "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
					return false;
				}
				if(!CommenTools.inputCheck(et_nick_name.getText().toString()))
				{
					et_nick_name.requestFocus();
					Toast.makeText(getActivity(), "请填写姓名", Toast.LENGTH_SHORT).show();
					return false;
				}
				return true;
			}
		});
	}

	private void exit() {
		
		ContextThemeWrapper contextThemeWrapper=new ContextThemeWrapper(getActivity(), R.style.dialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(contextThemeWrapper);
		builder.setTitle("你真的要退出么？");  
		builder.setPositiveButton("确定",new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mGetShareDatas.insertStringMessage(MConstants.ACCESS_TOKEN,"");
				mGetShareDatas.insertStringMessage(MConstants.TELNUM, "");
				mGetShareDatas.insertIntMessage(MConstants.SEX, 0);
				mGetShareDatas.insertStringMessage(MConstants.PSW, "");
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
		final AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).create();
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_modify_psw, null);
		alertDialog.setView(view);
		alertDialog.show();
		Window window=alertDialog.getWindow();
		window.setContentView(R.layout.dialog_modify_psw);
		final EditText et_init_psw=(EditText) window.findViewById(R.id.et_init_psw);
		final EditText et_new_psw=(EditText) window.findViewById(R.id.et_new_psw);
		final EditText et_new_psw2=(EditText) window.findViewById(R.id.et_new_psw2);
		Button btn_done=(Button) window.findViewById(R.id.btn_done);
		TextView tv_back=(TextView) window.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		btn_done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(inputCheckPsw())
				{
					mThreadPoolManager.addTask(new Runnable() {
						@Override
						public void run() {
							accessStatus=mServer.modifyPassWord(access_token, et_new_psw2.getText().toString(), et_init_psw.getText().toString());
							if(accessStatus!=null&&accessStatus.status==1)
							{
								myPsw=et_new_psw2.getText().toString();
								mGetShareDatas.insertStringMessage(MConstants.PSW, myPsw);
								Message message=mHandler.obtainMessage();
								message.what=1;
								message.sendToTarget();
							}
						}
					});
					alertDialog.dismiss();
				}
			}

			private boolean inputCheckPsw() {
				if(!CommenTools.inputCheck(et_init_psw.getText().toString()))
				{
					Toast.makeText(getActivity(), "初始密码不能为空", Toast.LENGTH_SHORT).show();
					et_init_psw.requestFocus();
					return false;
				}
				if(!CommenTools.inputCheck(et_new_psw.getText().toString()))
				{
					Toast.makeText(getActivity(), "新密码不能为空", Toast.LENGTH_SHORT).show();
					et_new_psw.requestFocus();
					return false;
				}
				if(!CommenTools.inputCheck(et_new_psw2.getText().toString()))
				{
					Toast.makeText(getActivity(), "第二次密码不能为空", Toast.LENGTH_SHORT).show();
					et_new_psw2.requestFocus();
					return false;
				}
				return true;
			}
		});
		
	}

	
}
