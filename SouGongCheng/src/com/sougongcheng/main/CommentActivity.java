package com.sougongcheng.main;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.sougongcheng.R;
import com.sougongcheng.adapter.AdapterComment;
import com.sougongcheng.bean.CommentsInfo;
import com.sougongcheng.bean.Status;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.server.Server;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.NetworkUtils;
import com.sougongcheng.util.ThreadPoolManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommentActivity extends Activity implements OnClickListener{
	
	private TextView tv_back;
	
	private Button btn_send;
	
	private String comments_id_str;
	
	private String comments_user_id_str;
	
	private String comments_user_name_str;
	
	private String comments_like_nums_str;
	
	private String comments_share_nums_str;
	
	private String comments_contents_str;
	
	private String comments_create_time_str;
	
	private String comments_is_like_str;
	
	private String comments_user_sex_str;
	
	private String comments_is_store_str;
	
	private TextView tv_nick_name;
	
	private TextView tv_date;

	private TextView tv_content;
	
	private TextView tv_num_like;
	
	private TextView tv_num_share;
	
	private CheckBox btn_like;
	
	private Button btn_share;
	
	private ListView comment_list;
	
	private EditText et_comment_content;
	
	private GetShareDatas mGetShareDatas;
	
	private ThreadPoolManager mPoolManager;
	
	private String access_token;
	
	private Server mServer;
	
	private Status status;
	
	private AdapterComment adapterComment;
	
	private FrameLayout bottom_menu;
	
	private CommentsInfo commentsInfo;
	
	ArrayList<Map<String, Object>> mapList;
	
	HashMap<String,Object> commentInfo;
	
	private Handler mHandler=new Handler()
	{
		public void handleMessage(Message msg) {
			if(msg.what==1)
			{
			mapList=commentsInfo.getComments();
			Collections.reverse(mapList);
			freashList();
			}else if(msg.what==2)
			{
				String name=(String) access_token.subSequence(access_token.indexOf("-")+1,access_token.length());
				commentInfo=new HashMap<String, Object>();
				commentInfo.put(MConstants.COMMENTS_ID, comments_id_str);
				commentInfo.put(MConstants.COMMENTS_USER_NAME, name);
				commentInfo.put(MConstants.COMMENTS_CONTENTS, et_comment_content.getText().toString());
				et_comment_content.setText("");
				mapList.add(mapList.size(), commentInfo);
				freashList();
			}
		}

		
	};
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initViews();
		
		initDatas();
		
		setOnClickListenner();
	}

	private void setOnClickListenner() {
		
		tv_back.setOnClickListener(this);
		
		btn_send.setOnClickListener(this);
		
		btn_share.setOnClickListener(this);
	}

	private void freashList() {
		
		adapterComment=new AdapterComment(CommentActivity.this, mapList);
		
		comment_list.setAdapter(adapterComment);
		
	};
	
	private void initDatas() {
		
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, CommentActivity.this);
		
		access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, "");
		
		mPoolManager=ThreadPoolManager.getInstance();
		
		mServer=Server.getInstance();
		
		Intent intent=getIntent();
		
		comments_id_str=intent.getStringExtra(MConstants.COMMENTS_ID);
		
		comments_user_id_str=intent.getStringExtra(MConstants.COMMENTS_USER_ID);
		
		comments_user_name_str=intent.getStringExtra(MConstants.COMMENTS_USER_NAME);
		
		comments_like_nums_str=intent.getStringExtra(MConstants.COMMENTS_LIKE_NUMS);
		
		comments_share_nums_str=intent.getStringExtra(MConstants.COMMENTS_SHARE_NUMS);
		
		comments_contents_str=intent.getStringExtra(MConstants.COMMENTS_CONTENTS);
		
		comments_create_time_str=intent.getStringExtra(MConstants.COMMENTS_CREATE_TIME);
		
		comments_is_like_str=intent.getStringExtra(MConstants.COMMENTS_IS_LIKE);
		
		comments_user_sex_str=intent.getStringExtra(MConstants.COMMENTS_USER_SEX);
		
		comments_is_store_str=intent.getStringExtra(MConstants.COMMENTS_IS_STORE);
		
		tv_nick_name.setText(comments_user_name_str);
		
		tv_date.setText(comments_create_time_str);
		
		tv_content.setText(comments_contents_str);
		
		tv_num_like.setText(comments_like_nums_str);
		
		tv_num_share.setText(comments_share_nums_str);
		if(comments_is_like_str!=null)
		{
		if(Integer.parseInt(comments_is_like_str)==1)
		{
			btn_like.setChecked(true);
		}else
		{
			btn_like.setChecked(false);
		}
		}
		
		getCommentList();
	}

	private void getCommentList() {
		if(NetworkUtils.isNetworkAvailable(CommentActivity.this))
		{
		mPoolManager.addTask(new Runnable() {
			@Override
			public void run() {
				commentsInfo=mServer.getCommments("all", access_token, "10", "0", comments_id_str);
				if(commentsInfo!=null)
				{
					Message message=mHandler.obtainMessage();
					message.what=1;
					message.sendToTarget();
				}else
				{
					Toast.makeText(CommentActivity.this, "��ȡ�����б�ʧ��", Toast.LENGTH_SHORT).show();
				}
			}
		});
		}else
		{
			Toast.makeText(CommentActivity.this, "��ǰ���粻����", Toast.LENGTH_SHORT).show();
		}
	}

	private void initViews() {
		
		setContentView(R.layout.act_comment);
		
		tv_back=(TextView) findViewById(R.id.tv_back);
		
		btn_send=(Button) findViewById(R.id.btn_send);
		
		tv_nick_name=(TextView) findViewById(R.id.tv_nick_name);
		
		tv_date=(TextView) findViewById(R.id.tv_date);
		
		tv_content=(TextView) findViewById(R.id.tv_content);
		
		tv_num_like=(TextView) findViewById(R.id.tv_num_like);
		
		tv_num_share=(TextView) findViewById(R.id.tv_num_share);
		
		btn_like=(CheckBox) findViewById(R.id.btn_like);
		
		btn_share=(Button) findViewById(R.id.btn_share);
		
		comment_list=(ListView) findViewById(R.id.comment_list);
		
		et_comment_content=(EditText) findViewById(R.id.et_comment_content);
		
		bottom_menu=(FrameLayout) findViewById(R.id.bottom_menu);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			if(et_comment_content!=null&&!TextUtils.isEmpty(et_comment_content.getText().toString()))
			{
			send();
			}else
			{
				Toast.makeText(CommentActivity.this, "˵��ʲô��", Toast.LENGTH_SHORT).show();
				et_comment_content.requestFocus();
			}
			break;
		case R.id.tv_back:
			finish();
			break;
		case R.id.btn_share:
			share();
			break;
		}
	}

	private void send() {
		mPoolManager.addTask(new Runnable() {
			@Override
			public void run() {
				status=mServer.manageComment("add", access_token, et_comment_content.getText().toString(), comments_id_str);
				if(status!=null)
				{
					Message message=mHandler.obtainMessage();
					message.what=2;
					message.sendToTarget();
				}else
				{
					Toast.makeText(CommentActivity.this, "���۷���ʧ��", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private void share()
	{
		// TODO Auto-generated method stub
		 ShareSDK.initSDK(CommentActivity.this);
		 OnekeyShare oks = new OnekeyShare();
		 //�ر�sso��Ȩ
		 oks.disableSSOWhenAuthorize(); 
		 
		// ����ʱNotification��ͼ�������  2.5.9�Ժ�İ汾�����ô˷���
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		 oks.setTitle(getString(R.string.share));
		 // titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		 oks.setText("���Ƿ����ı�");
		 // imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		 oks.setImagePath("/sdcard/test.jpg");//ȷ��SDcard������ڴ���ͼƬ
		 // url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		 oks.setUrl("http://sharesdk.cn");
		 // comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		 oks.setComment("���ǲ��������ı�");
		 // site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		 oks.setSiteUrl("http://sharesdk.cn");
		// ��������GUI
		 oks.show(CommentActivity.this);
	}

}
