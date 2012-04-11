package com.feiying.bean;

import java.util.List;
import java.util.Map;

public class VideoSearchBean {
	private List<Map<String, Object>> list;
	private Map<String, Integer> countMap;
	
	public List<Map<String, Object>> getList() {
		return list;
	}
	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
	public Map<String, Integer> getCountMap() {
		return countMap;
	}
	public void setCountMap(Map<String, Integer> countMap) {
		this.countMap = countMap;
	}
}
