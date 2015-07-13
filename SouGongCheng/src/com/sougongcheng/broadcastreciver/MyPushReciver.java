package com.sougongcheng.broadcastreciver;

import cn.jpush.android.api.JPushInterface;
import com.sougongcheng.main.MessageDetail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MyPushReciver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
        {
		//打开自定义的Activity
    	Intent i = new Intent(context, MessageDetail.class);
    	i.putExtra("url", "www.baidu.com");
    	i.putExtras(bundle);
    	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
    	context.startActivity(i);
        }
    
	}
	
	

}
