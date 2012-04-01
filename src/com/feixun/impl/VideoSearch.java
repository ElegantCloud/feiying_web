package com.feixun.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.feixun.bean.VideoSearchBean;
import com.feixun.db.DBHelper;

public class VideoSearch {
	private static Log log = LogFactory.getLog(VideoSearch.class);

	public static final int SOURCEID = 4;
	public static final int TITLE = 5;
	public static final int IMAGEURL = 6;
	public static final int CHANNEL = 3;

	public static final int TOTAL = 1;
	public static final int TOTALFOUND = 2;

	public static VideoSearchBean getList(int offset, int pageSize,
			String searchTitle) {
		VideoSearchBean videoSearchBean = new VideoSearchBean();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> countMap = new HashMap<String, Object>();
		ResultSet rs = null;
		Connection conn = null;
		try {
			String sql = "SELECT source_id," + "title," + "image_url,"
					+ "channel " + "FROM feiying_rt " + "WHERE match('"
					+ searchTitle + "') " + "LIMIT " + (offset - 1) * pageSize
					+ ", " + pageSize;

			conn = DBHelper.getCoreSeekInstance().getConn();
			Statement s = conn.createStatement();
			log.info("sql: " + sql);
			s.execute(sql);
			rs = s.getResultSet();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("source_id", rs.getString(SOURCEID));
				map.put("title", rs.getString(TITLE));
				map.put("image_url", rs.getString(IMAGEURL));
				map.put("channel", rs.getString(CHANNEL));
				list.add(map);
			}
			videoSearchBean.setList(list);
			String sqlCount = "show meta";
			s.execute(sqlCount);
			rs = s.getResultSet();
			while (rs.next()) {
				countMap.put(rs.getString(1), rs.getString(2));
			}
			videoSearchBean.setCountMap(countMap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBHelper.getCoreSeekInstance().closeAll(rs, null, conn);
		}
		return videoSearchBean;
	}

	/**
	 * count the keyword
	 * 
	 * @param keyword
	 */
	public static void countKeyword(String keyword) {
		DBHelper dh = DBHelper.getInstance();
		String sql = "UPDATE fy_keywords SET count = count + 1 WHERE keyword = ?";
		Object[] params = new Object[] { keyword };
		try {
			int rows = dh.update(sql, params);
			if (rows <= 0) {
				// insert new keyword
				sql = "INSERT INTO fy_keywords (keyword) VALUES(?)";
				dh.update(sql, params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get 10 hot keywords
	 * 
	 * @return
	 */
	public static List<Map<String, Object>> getHotKeywords() {
		String sql = "SELECT keyword, count " +
				"FROM fy_keywords " +
				"WHERE DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(update_time) " +
				"ORDER BY count DESC LIMIT 10";
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			result = DBHelper.getInstance().query(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
