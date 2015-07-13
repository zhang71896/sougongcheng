package com.sougongcheng.bean;

import java.util.ArrayList;
import java.util.Map;

public class RecommandInfo {
	
	public int status;
	
	public ArrayList<Map<String,Object>> banners;
	
	public ArrayList<Map<String,Object>> items;

	public int getStatus() {
		return status;
	}

	public ArrayList<Map<String, Object>> getBanners() {
		return banners;
	}

	public void setBanners(ArrayList<Map<String, Object>> banners) {
		this.banners = banners;
	}

	public ArrayList<Map<String, Object>> getItems() {
		return items;
	}

	public void setItems(ArrayList<Map<String, Object>> items) {
		this.items = items;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	
	
}
