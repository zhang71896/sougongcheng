package com.sougongcheng.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.sougongcheng.contants.MConstants;
import com.sougongcheng.server.Server;
import com.sougongcheng.ui.widget.ProgressWebView;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.NetworkUtils;
import com.test.finder.R;

public class MessageDetailPush extends Activity implements OnClickListener{
	
	
	private TextView tv_back;
	
	private ProgressWebView wv_content;
	
	private String url;
	
	private Server mServer;
	
	private String access_token;
	
	private GetShareDatas mGetShareDatas;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		initClickListenner();
	}

	private void initClickListenner() {
		
		tv_back.setOnClickListener(this);
	}

	private void initViews() {
		
	

		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.act_message_detail);
		
		mServer=Server.getInstance();
		
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, MessageDetailPush.this);
		
		access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, " ");
		
		tv_back=(TextView) findViewById(R.id.tv_back);
		
		wv_content=(ProgressWebView) findViewById(R.id.wv_content);
		
	    WebSettings webSettings = wv_content.getSettings();  
		
        webSettings.setJavaScriptEnabled(true);    
        
        webSettings.setAllowFileAccess(true);  
        
        webSettings.setBuiltInZoomControls(true); 
        if(NetworkUtils.isNetworkAvailable(MessageDetailPush.this))
        {
        
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extras=bundle.getString(JPushInterface.EXTRA_EXTRA);
        extras=extras.substring(1, extras.length()-1);
        String type=extras.substring(0,extras.indexOf(":"));
        type=type.substring(1, type.length()-1);
        String id=extras.substring(extras.indexOf(":")+1,extras.length());
        id=id.substring(1, id.length()-1);
        url=mServer.getBandsInfoDetail(access_token, type, id);
        wv_content.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        wv_content.loadUrl(url);   
        }else
        {
        	Toast.makeText(MessageDetailPush.this, "当前网络不可用", Toast.LENGTH_SHORT).show();
        }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			Intent intent=new Intent(MessageDetailPush.this, MainAct.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}

    
    @Override   
    //设置回退    
    public boolean onKeyDown(int keyCode, KeyEvent event) {    
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv_content.canGoBack()) {    
        	wv_content.goBack(); //goBack()表示返回WebView的上一页面    
            return true;    
        }    
        finish();//结束退出程序  
        return false;    
    }    
        
	

}
