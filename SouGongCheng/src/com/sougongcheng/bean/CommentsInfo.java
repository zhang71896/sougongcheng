package com.sougongcheng.bean;

import java.util.ArrayList;
import java.util.Map;

public class CommentsInfo {
	
	public String status;
	
	public ArrayList<Map<String,Object>> comments;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<Map<String, Object>> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Map<String, Object>> comments) {
		this.comments = comments;
	}
	
	

}
