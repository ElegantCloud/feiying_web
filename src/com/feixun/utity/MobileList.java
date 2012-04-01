package com.feixun.utity;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MobileList {
	public static String getList(List<Map<String, Object>> list, Pager pager) {
		JSONObject jsonObject = new JSONObject();
		try {
			JSONObject jsonPager = new JSONObject();
			jsonPager.put("offset", pager.getOffset());
			jsonPager.put("pagenumber", pager.getPageNumber());
			jsonPager.put("hasPrevious", pager.getHasPrevious());
			jsonPager.put("hasNext", pager.getHasNext());
			jsonPager.put("previousPage", pager.getPreviousPage());
			jsonPager.put("nextPage", pager.getNextPage());
			jsonPager.put("count", pager.getSize());
			jsonObject.put("pager", jsonPager);
			JSONArray jsonArrayList = new JSONArray();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					jsonArrayList.put(map);
				}
			}
			jsonObject.put("list", jsonArrayList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	public static String getList(List<Map<String, Object>> list) {
		JSONObject jsonObject = new JSONObject();
		try {
			JSONArray jsonArrayList = new JSONArray();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					jsonArrayList.put(map);
				}
			}
			jsonObject.put("list", jsonArrayList);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject.toString();
	}

	public static String getList(List<Map<String, Object>> list,
			Map<String, Object> series) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("series", series);
			JSONArray jsonArrayList = new JSONArray();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					jsonArrayList.put(map);
				}
			}
			jsonObject.put("list", jsonArrayList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
}
