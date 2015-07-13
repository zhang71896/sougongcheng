package com.sougongcheng.main;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.sougongcheng.bean.HotKeyWordsInfo;
import com.sougongcheng.contants.MConstants;
import com.sougongcheng.server.Server;
import com.sougongcheng.util.CommenTools;
import com.sougongcheng.util.GetShareDatas;
import com.sougongcheng.util.NetworkUtils;
import com.sougongcheng.util.ThreadPoolManager;
import com.test.finder.R;

public class SearchActivity extends Activity implements OnClickListener, OnItemLongClickListener{
	
	private Button btn_area;
	
	private Button btn_industry;
	
	private Button btn_hot;
	
	private Button btn_type;
	
	private ImageView iv_add;
	
	private TextView tv_back;
	
	private TextView tv_search;
	
	private EditText et_add_key_words;
	
	private HashMap<Integer,Integer>MultiChoiceID =new HashMap<Integer, Integer>(); 
	
	private ArrayAdapter<String> adapter ;
	
    String[] myItems ;
    
    ArrayList<Map<String, Object>> initConditionList;
    
	public ArrayList<Map<String,Object>> areaArray;
	
	public ArrayList<Map<String,Object>> tradeArray;
	
	public ArrayList<Map<String,Object>> hotArray;
	
	public ArrayList<Map<String,Object>> typeArray;
    
    private HotKeyWordsInfo hotKeyWordsInfo;
	
	private ListView lv_key_words;
	
	private GetShareDatas mGetShareDatas;
	
    private ThreadPoolManager mPoolManager;
    
	private Server mServer;
    
	private String access_token;
	
	private String typeCondition;
	
	private String areaCondition;
	
	private String tradeConditon;
	
	private String hotCondition;

	
	private Handler mHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1)
			{
				if(hotKeyWordsInfo.status==0)
				{
					tradeArray=hotKeyWordsInfo.trade;
					hotArray=hotKeyWordsInfo.hot;
					areaArray=hotKeyWordsInfo.area;
					typeArray=hotKeyWordsInfo.type;
				}else
				{
					
				}
			}
		};
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initViews();
		
		initChoieceDatas();
		
		initClickListenner();
	}
	
	private void initChoieceDatas() {
		// TODO Auto-generated method stub
		if(NetworkUtils.isNetworkAvailable(SearchActivity.this))
		{
			mServer=Server.getInstance();
			mPoolManager=ThreadPoolManager.getInstance();
			mPoolManager.addTask(new Runnable() {
			@Override
			public void run() {
				hotKeyWordsInfo=mServer.getHotKeyWordsInfo("all", access_token);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(hotKeyWordsInfo!=null)
				{
				Message message=mHandler.obtainMessage();
				message.what=1;
				message.sendToTarget();
				}
			}
		});
		}else
		{
			Toast.makeText(SearchActivity.this, "当前网络不可用", Toast.LENGTH_SHORT).show();
		}
	}

	private void initClickListenner() {
		
		btn_area.setOnClickListener(this);
		
		btn_industry.setOnClickListener(this);
		
		btn_hot.setOnClickListener(this);
		
		btn_type.setOnClickListener(this);
		
		lv_key_words.setOnItemLongClickListener(this);
		
		iv_add.setOnClickListener(this);
		
		tv_back.setOnClickListener(this);
		
		tv_search.setOnClickListener(this);
	}

	private void initViews()
	{
		mGetShareDatas=GetShareDatas.getInstance(MConstants.USER_INFO, SearchActivity.this);
		
		access_token=mGetShareDatas.getStringMessage(MConstants.ACCESS_TOKEN, "");
		
		setContentView(R.layout.act_search);
		
		btn_area=(Button) findViewById(R.id.btn_area);
		
		btn_industry=(Button) findViewById(R.id.btn_industry);
		
		btn_hot=(Button) findViewById(R.id.btn_hot);
		
		btn_type=(Button) findViewById(R.id.btn_type);
		
		tv_search=(TextView) findViewById(R.id.tv_search);
		
		lv_key_words=(ListView) findViewById(R.id.lv_key_words);
		
		et_add_key_words=(EditText) findViewById(R.id.et_add_key_words);
		
		tv_back=(TextView) findViewById(R.id.tv_back);
		
		iv_add=(ImageView) findViewById(R.id.iv_add);
		
		initConditionList= new ArrayList<Map<String,Object>>();
		
	}
	
	private void initConditionDatas()
	{
		
		myItems=new String[initConditionList.size()];
		for(int i=0;i<initConditionList.size();i++)
		{
			myItems[i]=initConditionList.get(i).get("value").toString();
		}
		adapter = new ArrayAdapter<String>(  
                this,  
                R.layout.item_search,//只能有一个定义了id的TextView  
                myItems);//data既可以是数组，也可
		
		lv_key_words.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_area:
			if(areaArray!=null&&areaArray.size()>0)
			{
			show("地区热词选择",areaArray);
			}else
			{
				Toast.makeText(SearchActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.btn_industry:
			if(tradeArray!=null&&tradeArray.size()>0)
			{
			show("行业热词选择",tradeArray);
			}else
			{
				Toast.makeText(SearchActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_hot:
			if(hotArray!=null&&hotArray.size()>0)
			{
			show("热门关键字选择",hotArray);
			}else
			{
				Toast.makeText(SearchActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_type:	
			if(typeArray!=null&&typeArray.size()>0)
			{
			show("类型选择",typeArray);
			}else
			{
				Toast.makeText(SearchActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.iv_add:
			if(CommenTools.inputCheck(et_add_key_words.getText().toString()))
			{
				if(equalValue(et_add_key_words.getText().toString()))
				{
					
				}else
				{
				HashMap<String, Object> info=new HashMap<String, Object>();
				info.put(MConstants.TYPE, "hot");
				info.put("value", et_add_key_words.getText().toString());
				initConditionList.add(info);
				initConditionDatas();
				}
				et_add_key_words.setText("");
			}else
			{
				et_add_key_words.requestFocus();
				Toast.makeText(SearchActivity.this, "请填写标签", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_search:
			try {
				search();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	private void search() throws UnsupportedEncodingException {
		typeCondition="%7B";
		areaCondition="%7B";
		tradeConditon="%7B";
		hotCondition="%7B";
		if(initConditionList!=null&&initConditionList.size()>0)
		{
			int maxTypeCount=0;
			int maxAreaCount=0;
			int maxTradeCount=0;
			int maxHotCount=0;
			for(int i=0;i<initConditionList.size();i++)
			{
				if(initConditionList.get(i).get(MConstants.TYPE).toString().equals("hot"))
				{
					maxHotCount++;
				}else if(initConditionList.get(i).get(MConstants.TYPE).toString().equals("area"))
				{
					maxAreaCount++;
				}else if(initConditionList.get(i).get(MConstants.TYPE).toString().equals("trade"))
				{
					maxTradeCount++;
				}else if(initConditionList.get(i).get(MConstants.TYPE).toString().equals("type"))
				{
					maxTypeCount++;
				}
			}
			int currentTypeCount=0;
			int currentAreaCount=0;
			int currentTradeCount=0;
			int currentHotCount=0;
			for(int i=0;i<initConditionList.size();i++)
			{
			if(maxHotCount>=1&&initConditionList.get(i).get(MConstants.TYPE).toString().equals("hot"))
			{
				currentHotCount++;
				String  value= URLDecoder.decode(initConditionList.get(i).get(MConstants.VALUE).toString(), "UTF-8");  
				Log.e("tag", "value: "+value);
				if(currentHotCount==maxHotCount)
				{
					hotCondition+="%22"+value+"%22";
				}else
				{
					hotCondition+="%22"+value+"%22"+",";
				}
			}else if(maxAreaCount>=1&&initConditionList.get(i).get(MConstants.TYPE).toString().equals("area"))
			{
				currentAreaCount++;
				String value=URLDecoder.decode(initConditionList.get(i).get(MConstants.VALUE).toString(), "UTF-8");  
				if(currentAreaCount==maxAreaCount)
				{
					areaCondition+="%22"+value+"%22";
				}else
				{
					areaCondition+="%22"+value+"%22"+",";
				}
			}else if(maxTradeCount>=1&&initConditionList.get(i).get(MConstants.TYPE).toString().equals("trade"))
			{
				currentTradeCount++;
				String value=URLDecoder.decode(initConditionList.get(i).get(MConstants.VALUE).toString(), "UTF-8");  
				if(currentTradeCount==maxTradeCount)
				{
					tradeConditon+="%22"+value+"%22";
				}else
				{
					tradeConditon+="%22"+value+"%22"+",";
				}
				
			}else if(maxTypeCount>=1&&initConditionList.get(i).get(MConstants.TYPE).toString().equals("type"))
			{
				currentTypeCount++;
				if(currentTypeCount==maxTypeCount)
				{
					typeCondition+="%22"+initConditionList.get(i).get(MConstants.VALUE).toString()+"%22";
				}else
				{
					typeCondition+="%22"+initConditionList.get(i).get(MConstants.VALUE).toString()+"%22"+",";
				}
			}
		
		}
			typeCondition+="%7D";
			areaCondition+="%7D";
			tradeConditon+="%7D";
			hotCondition+="%7D";
			Intent intent=new Intent(SearchActivity.this, SearchConetentActivity.class);
			intent.putExtra("type", typeCondition);
			intent.putExtra("area", areaCondition);
			intent.putExtra("trade", tradeConditon);
			intent.putExtra("hot", hotCondition);
			startActivity(intent);
		}else
		{
			Toast.makeText(SearchActivity.this, "请先输入查询条件", Toast.LENGTH_SHORT).show();
		}
	}

	private void show(String title,ArrayList<Map<String,Object>> stringlist) {
		ContextThemeWrapper contextThemeWrapper=new ContextThemeWrapper(SearchActivity.this, R.style.dialog);
		final String [] mItems;
		final String [] mTypes;
		boolean [] checkArray;
		checkArray=new boolean[stringlist.size()];
		mItems=new String[stringlist.size()];
		mTypes=new String[stringlist.size()];
		for(int i=0;i<stringlist.size();i++)
		{
			mItems[i]=stringlist.get(i).get("value").toString();
			mTypes[i]=stringlist.get(i).get(MConstants.TYPE).toString();
			checkArray[i]=false;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(contextThemeWrapper);   
		MultiChoiceID.clear();
		for(int i=0;i<stringlist.size();i++)
		{
			MultiChoiceID.put(i, 0);
		}
		    builder.setTitle(title);  
		    builder.setMultiChoiceItems(mItems,  
		    		checkArray,  
		            new DialogInterface.OnMultiChoiceClickListener() {  
		                public void onClick(DialogInterface dialog, int whichButton,  
		                        boolean isChecked) {  
		                   if(isChecked) {  
		                       MultiChoiceID.put(whichButton, 1);  
		                   }else {  
		                       MultiChoiceID.put(whichButton, 0);  
		                   }  
		                      
		                }  
		            });  
		    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
		        public void onClick(DialogInterface dialog, int whichButton) {  
		            String str = "";  
		            for (int i = 0 ;i < MultiChoiceID.size(); i++) {  
		            if(MultiChoiceID.get(i).equals(1))
		            {
		            if(equalValue(mItems[i]))
		            {
		            }else
		            {
		            	HashMap<String,Object> info=new HashMap<String,Object>();
		            	info.put(MConstants.TYPE, mTypes[i]);
		            	info.put("value",mItems[i]);
			            initConditionList.add(info);
		            }
		            }  
		            }
		            initConditionDatas();
		        }

		    });  
		    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
		        public void onClick(DialogInterface dialog, int whichButton) {  
		 
		        }  
		    });  
		   builder.create().show(); 
		
	}
	
	private boolean equalValue(String string) {
		boolean reuslt=false;
		for(int i=0;i<initConditionList.size();i++)
		{
			if(initConditionList.get(i).get("value").toString().equals(string))
			{
				reuslt=true;
			}
		}
		
		return reuslt;
	}  
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		ContextThemeWrapper contextThemeWrapper=new ContextThemeWrapper(SearchActivity.this, R.style.dialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(contextThemeWrapper);
		builder.setTitle("你确定要删除该关键字么？");  
		builder.setPositiveButton("确定",new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				initConditionList.remove(position);
				initConditionDatas();
			}
			
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}
		);
		builder.create().show();
		return false;
	}
}
