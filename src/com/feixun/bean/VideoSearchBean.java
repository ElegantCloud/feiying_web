package com.feixun.bean;

import java.util.List;
import java.util.Map;

public class VideoSearchBean {
	private List<Map<String, Object>> list;
	private Map<String, Object> countMap;
	
	public List<Map<String, Object>> getList() {
		return list;
	}
	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
	public Map<String, Object> getCountMap() {
		return countMap;
	}
	public void setCountMap(Map<String, Object> countMap) {
		this.countMap = countMap;
	}
}
