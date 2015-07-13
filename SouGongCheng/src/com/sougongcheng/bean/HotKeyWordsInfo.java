package com.sougongcheng.bean;

import java.util.ArrayList;
import java.util.Map;

public class HotKeyWordsInfo {
	
	public int status;
	
	public ArrayList<Map<String,Object>> area;
	
	public ArrayList<Map<String,Object>> trade;
	
	public ArrayList<Map<String,Object>> hot;
	
	public ArrayList<Map<String,Object>> type;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ArrayList<Map<String, Object>> getArea() {
		return area;
	}

	public void setArea(ArrayList<Map<String, Object>> area) {
		this.area = area;
	}

	public ArrayList<Map<String, Object>> getTrade() {
		return trade;
	}

	public void setTrade(ArrayList<Map<String, Object>> trade) {
		this.trade = trade;
	}

	public ArrayList<Map<String, Object>> getHot() {
		return hot;
	}

	public void setHot(ArrayList<Map<String, Object>> hot) {
		this.hot = hot;
	}



}
