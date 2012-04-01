package com.feixun.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.feixun.db.DBHelper;

public class Movie {

	/**
	 * 得到电影列表
	 * 
	 * @param currPage
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getList(int currPage, int pageSize)
			throws SQLException {
		String sql = "SELECT m.time," 
				+ "m.size," 
				+ "m.video_url," 
				+ "m.actor,"
				+ "m.release_date," 
				+ "m.origin," 
				+ "v.source_id," 
				+ "v.title,"
				+ "v.image_url," 
				+ "v.channel," 
				+ "v.play_count,"
				+ "v.fav_count," 
				+ "v.share_count " 
				+ "FROM fy_movie m "
				+ "LEFT JOIN fy_video v ON m.source_id = v.source_id "
				+ "ORDER BY v.created_time DESC";
		return DBHelper.getInstance().queryPager(sql, currPage, pageSize);
	}

	/**
	 * 得到电影数量
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static int getListCount() throws SQLException {
		String sql = "SELECT count(source_id) FROM fy_movie";
		return DBHelper.getInstance().count(sql);
	}

	/**
	 * 得到电影根据sourceId
	 * 
	 * @param sourceId
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, Object> getMoiveBySourceId(String sourceId)
			throws SQLException {
		Map<String, Object> bean = null;
		String sql = "SELECT m.time," 
				+ "m.size," 
				+ "m.video_url,"
				+ "m.director," 
				+ "m.actor," 
				+ "m.release_date," 
				+ "m.origin," 
				+ "m.description," 
				+ "v.source_id," 
				+ "v.title,"
				+ "v.image_url," 
				+ "v.channel," 
				+ "v.play_count,"
				+ "v.fav_count," 
				+ "v.share_count " 
				+ "FROM fy_movie m "
				+ "LEFT JOIN fy_video v ON m.source_id = v.source_id "
				+ "WHERE m.source_id=?";
		Object[] params = new Object[] { sourceId };
		List<Map<String, Object>> list = DBHelper.getInstance().query(sql,
				params);
		if (list.size() > 0) {
			bean = list.get(0);
		}
		return bean;
	}

	/**
	 * 该用户收藏下的所有电影
	 * 
	 * @param username
	 * @param channel
	 * @param currPage
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getFavList(String username,
			int channel, int currPage, int pageSize) throws SQLException {
		String sql = "SELECT m.time," 
				+ "m.size," 
				+ "m.video_url," 
				+ "m.actor,"
				+ "m.release_date," 
				+ "m.origin," 
				+ "v.source_id," 
				+ "v.title,"
				+ "v.image_url," 
				+ "v.channel," 
				+ "v.play_count,"
				+ "v.fav_count," 
				+ "v.share_count " 
				+ "FROM fy_fav f "
				+ "LEFT JOIN fy_movie m ON f.source_id = m.source_id "
				+ "LEFT JOIN fy_video v ON f.source_id = v.source_id "
				+ "WHERE f.username = ? AND v.channel = ? "
				+ "ORDER BY f.created_time DESC";
		Object[] params = new Object[] { Long.parseLong(username), channel };
		return DBHelper.getInstance().queryPager(sql, params, currPage,
				pageSize);
	}
}
