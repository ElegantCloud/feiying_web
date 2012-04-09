package com.feiying.impl;

import java.sql.SQLException;

import com.feiying.db.DBHelper;

public class Fav {
	public static final String PLAY = "play_count";
	public static final String FAV = "fav_count";
	public static final String SHARE = "share_count";

	public static final String INCREASE = "increase";
	public static final String REDUCTION = "reduction";

	/**
	 * 用户收藏视频
	 * 
	 * @param username
	 * @param sourceId
	 * @return
	 */
	public static String favVideo(String username, String sourceId) {
		String result = "";
		result = check_fy_fav(username, sourceId);
		if (result.equals("0")) {
			result = create_fy_fav(username, sourceId); // 视频与用户关联
			if (result.equals("0")) {
				result = Video.record_fy_video_info(sourceId, Fav.FAV,
						Fav.INCREASE, 1); // 视频收藏量+1
			}
		}
		return result;
	}

	/**
	 * 用户删除收藏视频
	 * 
	 * @param username
	 * @param sourceId
	 * @return
	 */
	public static String deleteVideo(String username, String sourceId) {
		String result = "";
		result = delete_fy_fav(username, sourceId); // 删除视频与用户关联
		if (result.equals("0")) {
			result = Video.record_fy_video_info(sourceId, Fav.FAV,
					Fav.REDUCTION, 1); // 视频收藏量-1
		}
		return result;
	}

	/**
	 * 收藏视频
	 * 
	 * @param username
	 * @param sourceId
	 * @return
	 */
	public static String create_fy_fav(String username, String sourceId) {
		String result = "";

		String sql = "INSERT INTO fy_fav(username, source_id) VALUE (?, ?)";
		Object[] params = new Object[] { Long.parseLong(username), sourceId };

		try {
			int resultCount = DBHelper.getInstance().update(sql, params);
			result = resultCount > 0 ? "0" : "1001";
		} catch (SQLException e) {
			e.printStackTrace();
			result = "1001";
		}
		return result;
	}

	/**
	 * 删除视频
	 * 
	 * @param username
	 * @param sourceId
	 * @return
	 */
	public static String delete_fy_fav(String username, String sourceId) {
		String result = "";

		String sql = "DELETE FROM fy_fav WHERE username = ? AND source_id = ?";
		Object[] params = new Object[] { Long.parseLong(username), sourceId };

		try {
			int resultCount = DBHelper.getInstance().update(sql, params);
			result = resultCount > 0 ? "0" : "1001";
		} catch (SQLException e) {
			e.printStackTrace();
			result = "1001";
		}
		return result;
	}

	/**
	 * 判断该视频与用户是否关联
	 * 
	 * @param username
	 * @param sourceId
	 * @return
	 */
	public static String check_fy_fav(String username, String sourceId) {
		String result = "";
		String sql = "SELECT count(source_id) FROM fy_fav WHERE username = ? AND source_id = ?";
		Object[] params = new Object[] { Long.parseLong(username), sourceId };

		try {
			int count = Integer.parseInt(DBHelper.getInstance().scalar(sql,
					params));
			result = count == 0 ? "0" : "1";
		} catch (SQLException e) {
			e.printStackTrace();
			result = "1001";
		}
		return result;
	}

	/**
	 * 该用户收藏视频类型下的所有视频的总量
	 * 
	 * @param username
	 * @param channel
	 * @return
	 * @throws SQLException
	 */
	public static int getFavListCount(String username, int channel)
			throws SQLException {
		String sql = "SELECT count(f.source_id) FROM fy_fav f "
				+ "LEFT JOIN fy_video v ON f.source_id = v.source_id "
				+ "WHERE f.username = ? AND v.channel = ? ";
		Object[] params = new Object[] { Long.parseLong(username), channel };
		return DBHelper.getInstance().count(sql, params);
	}
}
