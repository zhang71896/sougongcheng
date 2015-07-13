package com.sougongcheng.broadcastreciver;

import cn.jpush.android.api.JPushInterface;
import com.sougongcheng.main.MessageDetail;
import com.sougongcheng.main.MessageDetailPush;
import com.sougongcheng.util.GetShareDatas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MyPushReciver extends BroadcastReceiver{
	
	

	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		JPushInterface.setDebugMode(true);
		 // 设置开启日志,发布时请关闭日志
        JPushInterface.init(context); 
        JPushInterface.resumePush(context);
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
        {
    	Intent i = new Intent(context, MessageDetailPush.class);
    	i.putExtras(bundle);
	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
    	context.startActivity(i);
        }
    
	}
	
	

}
