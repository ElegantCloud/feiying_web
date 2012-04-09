package com.feiying.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.feiying.db.DBHelper;

public class FavCount {

	/**
	 * 获得用户视频类型收藏总量
	 * 
	 * @param username
	 * @return
	 */
	public static List<Map<String, Object>> getList(String username) {
		String sql = "SELECT v.channel," 
				+ "count(v.channel) as favcount "
				+ "FROM fy_video v "
				+ "RIGHT JOIN fy_fav f ON v.source_id = f.source_id "
				+ "WHERE f.username = ? " 
				+ "group by v.channel";
		Object[] params = new Object[] { Long.parseLong(username) };
		try {
			return DBHelper.getInstance().query(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
