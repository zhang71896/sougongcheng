package com.sougongcheng.bean;

import java.util.ArrayList;
import java.util.Map;

public class CommentsInfo {
	
	public int status;
	
	public ArrayList<Map<String,Object>> comments;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ArrayList<Map<String, Object>> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Map<String, Object>> comments) {
		this.comments = comments;
	}
	
	

}
