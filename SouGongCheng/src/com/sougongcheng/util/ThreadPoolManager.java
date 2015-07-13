package com.sougongcheng.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 */
public class ThreadPoolManager {

	private ExecutorService service;

	private ThreadPoolManager() {
		int num = Runtime.getRuntime().availableProcessors();
		service = Executors.newFixedThreadPool(num * 4);
	}
	private static final ThreadPoolManager manager = new ThreadPoolManager();

	public static ThreadPoolManager getInstance() {
		return manager;
	}

	public void addTask(Runnable runnable) {
		try {
			service.execute(runnable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
