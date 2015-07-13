package com.sougongcheng.ui.widget;


import com.example.sougongcheng.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyProgressDialog extends Dialog {
	private Context context = null;
	private static MyProgressDialog progressDialog = null;
	public MyProgressDialog(Context context){
		super(context);
		this.context = context;
	}
	public MyProgressDialog(Context context, int theme) {
		super(context, theme);
	}
	public static MyProgressDialog createDialog(Context context){
		progressDialog = new MyProgressDialog(context,R.style.myprogressDialog);
		progressDialog.setContentView(R.layout.my_progress_dialog);  
		progressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return progressDialog;
	}
	public void onWindowFocusChanged(boolean hasFocus){
		if (progressDialog == null){
			return;
		}
		View progressBar = (ProgressBar) progressDialog.findViewById(R.id.loadbar);
		progressBar.setVisibility(View.VISIBLE);
	}
	/**
	* setTitile ����
	* @param strTitle
	*
	*/
	public MyProgressDialog setTitile(String strTitle){
		return progressDialog;
	}
	/**
	* setMessage ��ʾ����
	* @param strMessage
	*/
	public MyProgressDialog setMessage(String strMessage){
		TextView tvMsg = (TextView)progressDialog.findViewById(R.id.id_tv_loadingmsg);
		if (tvMsg != null){
			tvMsg.setText(strMessage);
		}
			return progressDialog;
	}
}
