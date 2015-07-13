package com.sougongcheng.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.test.finder.R;

public class FragmentMyAccount extends Fragment implements OnClickListener{
	
	private RelativeLayout rl_exit;
	
	private RelativeLayout rl_store;
	
	private RelativeLayout rl_suggest;
	
	private RelativeLayout rl_latest_view;
	
	private RelativeLayout rl_del;
	
	private RelativeLayout rl_about;
	
	private View myView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		myView=inflater.inflate(R.layout.fragment_my_account, null);
		
		initViews();
		
		setOnClickListenner();
		
		return myView;
	}

	private void setOnClickListenner() {
		
		rl_exit.setOnClickListener(this);
		
		rl_store.setOnClickListener(this);
		
		rl_suggest.setOnClickListener(this);
		
		rl_latest_view.setOnClickListener(this);
		
		rl_del.setOnClickListener(this);
		
		rl_about.setOnClickListener(this);
	}

	private void initViews() {
		
		rl_exit=(RelativeLayout) myView.findViewById(R.id.rl_exit);
		
		rl_store=(RelativeLayout) myView.findViewById(R.id.rl_store);

		rl_suggest=(RelativeLayout) myView.findViewById(R.id.rl_suggest);
		
		rl_latest_view=(RelativeLayout) myView.findViewById(R.id.rl_latest_view);
		
		rl_del=(RelativeLayout) myView.findViewById(R.id.rl_del);

		rl_about=(RelativeLayout) myView.findViewById(R.id.rl_about);

	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.rl_exit:
			
			break;

		case R.id.rl_store:
			
			break;
			
		case R.id.rl_suggest:
			
			break;
		case R.id.rl_latest_view:
			
			break;

		case R.id.rl_del:
			
			break;
			
		case R.id.rl_about:
			
			break;
		}
	}
}
