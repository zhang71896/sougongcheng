package com.sougongcheng.bean;

import java.util.ArrayList;
import java.util.Map;

public class SearchMachine {
	
	public int status;
	
	public ArrayList<Map<String,Object>> keywords;

	public ArrayList<Map<String, Object>> getKeywords() {
		return keywords;
	}

	public void setKeywords(ArrayList<Map<String, Object>> keywords) {
		this.keywords = keywords;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	

}
