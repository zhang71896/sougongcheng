package com.sougongcheng.fragment;

import java.lang.reflect.Field;

import com.example.sougongcheng.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class FragmentViewPager extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = null;
		try {
			view = inflater.inflate(R.layout.pager_item, container, false);
			ImageView mImageView = (ImageView) view.findViewById(R.id.imageView1);
			Bundle args = getArguments();
			final int resId = args.getInt("arg");
			Field field = R.drawable.class.getField("home"+ resId);
		    int resourseId = Integer.parseInt(field.get(null).toString());
			mImageView.setImageResource(resourseId);
			mImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "你点击了第"+ resId+"张图片", Toast.LENGTH_SHORT).show();
				}
			});
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}
}