package com.feixun.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.feixun.db.DBHelper;

public class Video {

	/**
	 * 得到视频列表
	 * 
	 * @param currPage
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getList(int currPage, int pageSize)
			throws SQLException {
		String sql = "SELECT s.time," 
				+ "s.size," 
				+ "s.video_url,"
				+ "v.source_id," 
				+ "v.title," 
				+ "v.image_url," 
				+ "v.channel,"
				+ "v.play_count," 
				+ "v.fav_count," 
				+ "v.share_count "
				+ "FROM fy_short_video s "
				+ "LEFT JOIN fy_video v ON s.source_id = v.source_id "
				+ "ORDER BY v.created_time DESC";
		return DBHelper.getInstance().queryPager(sql, currPage, pageSize);
	}

	/**
	 * 得到视频数量
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static int getListCount() throws SQLException {
		String sql = "SELECT count(source_id) FROM fy_short_video";
		return DBHelper.getInstance().count(sql);
	}

	/**
	 * 得到视频列表根据视频类型
	 * 
	 * @param currPage
	 * @param pageSize
	 * @param channel
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getListByCategory(int currPage,
			int pageSize, int channel) throws SQLException {
		String sql = "SELECT s.time," 
				+ "s.size," 
				+ "s.video_url,"
				+ "v.source_id," 
				+ "v.title," 
				+ "v.image_url," 
				+ "v.channel,"
				+ "v.play_count," 
				+ "v.fav_count," 
				+ "v.share_count "
				+ "FROM fy_short_video s "
				+ "LEFT JOIN fy_video v ON s.source_id = v.source_id "
				+ "WHERE v.channel = ? " 
				+ "ORDER BY v.created_time DESC";
		Object[] params = new Object[] { channel };
		return DBHelper.getInstance().queryPager(sql, params, currPage,
				pageSize);
	}

	/**
	 * 得到视频数量根据视频类型
	 * 
	 * @param channel
	 * @return
	 * @throws SQLException
	 */
	public static int getListCountByCategory(int channel) throws SQLException {
		String sql = "SELECT count(s.source_id) " 
				+ "FROM fy_short_video s "
				+ "LEFT JOIN fy_video v ON s.source_id = v.source_id "
				+ "WHERE v.channel = ?";
		Object[] params = new Object[] { channel };
		return DBHelper.getInstance().count(sql, params);
	}

	/**
	 * 得到视频根据sourceId
	 * 
	 * @param sourceId
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, Object> getVideoBySourceId(String sourceId)
			throws SQLException {
		Map<String, Object> bean = null;
		String sql = "SELECT s.time," 
				+ "s.size," 
				+ "s.video_url,"
				+ "v.source_id," 
				+ "v.title," 
				+ "v.image_url," 
				+ "v.channel,"
				+ "v.play_count," 
				+ "v.fav_count," 
				+ "v.share_count "
				+ "FROM fy_short_video s "
				+ "LEFT JOIN fy_video v ON s.source_id = v.source_id "
				+ "WHERE s.source_id = ?";
		Object[] params = new Object[] { sourceId };
		List<Map<String, Object>> list = DBHelper.getInstance().query(sql,
				params);
		if (list.size() > 0) {
			bean = list.get(0);
		}
		return bean;
	}

	/**
	 * 该用户收藏视频类型下的所有视频
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
		String sql = "SELECT s.time," 
				+ "s.size," 
				+ "s.video_url,"
				+ "v.source_id," 
				+ "v.title," 
				+ "v.image_url," 
				+ "v.channel,"
				+ "v.play_count," 
				+ "v.fav_count " 
				+ "FROM fy_fav f "
				+ "LEFT JOIN fy_short_video s ON f.source_id = s.source_id "
				+ "LEFT JOIN fy_video v ON f.source_id = v.source_id "
				+ "WHERE f.username = ? AND v.channel = ? "
				+ "ORDER BY f.created_time DESC";
		Object[] params = new Object[] { Long.parseLong(username), channel };
		return DBHelper.getInstance().queryPager(sql, params, currPage,
				pageSize);
	}

	/**
	 * 记录视频量
	 * 
	 * @param sourceId
	 * @param type
	 * @param oper
	 * @param count
	 * @return
	 */
	public static String record_fy_video_info(String sourceId, String type,
			String oper, int count) {
		String result = "";
		String sql = "";
		if (oper.equals(Fav.INCREASE)) {
			sql = "UPDATE fy_video SET " + type + " = " + type
					+ " + ? WHERE source_id = ? ";
		} else {
			sql = "UPDATE fy_video SET " + type + " = " + type
					+ " - ? WHERE source_id = ? ";
		}
		Object[] params = new Object[] { count, sourceId };

		try {
			int resultCount = DBHelper.getInstance().update(sql, params);
			result = resultCount > 0 ? "0" : "1001";
		} catch (SQLException e) {
			e.printStackTrace();
			result = "1001";
		}
		return result;
	}

}
