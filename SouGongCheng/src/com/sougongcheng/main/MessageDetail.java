package com.sougongcheng.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.sougongcheng.R;

public class MessageDetail extends Activity implements OnClickListener{
	
	
	private TextView tv_back;
	
	private WebView wv_content;
	
	private String url;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		initClickListenner();
	}

	private void initClickListenner() {
		
		tv_back.setOnClickListener(this);
	}

	private void initViews() {
		
		setContentView(R.layout.message_detail);
		
		tv_back=(TextView) findViewById(R.id.tv_back);
		
		wv_content=(WebView) findViewById(R.id.wv_content);
		
	    WebSettings webSettings = wv_content.getSettings();  
		
        webSettings.setJavaScriptEnabled(true);    
        
        webSettings.setAllowFileAccess(true);  
        
        webSettings.setBuiltInZoomControls(true);  
        
        Intent intent=getIntent();

        url=intent.getStringExtra("url");
        
        wv_content.loadUrl(url);    
        
        wv_content.setWebViewClient(new webViewClient ());    
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			
			finish();
			
			break;

		default:
			break;
		}
		
	}

	 //Web视图    
    private class webViewClient extends WebViewClient {    
        public boolean shouldOverrideUrlLoading(WebView view, String url) {    
            view.loadUrl(url);    
            return true;    
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
