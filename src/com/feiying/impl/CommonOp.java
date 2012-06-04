package com.feiying.impl;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.feiying.db.DBHelper;
import com.feiying.utity.ConfigManager;

public class CommonOp {

	public static String getAppDownloadURL(String device) {
		String sql = "SELECT app_name FROM fy_mobile_version WHERE type = ? ORDER BY update_time DESC LIMIT 1";
		Object[] params = new Object[] { device };

		String url = ConfigManager.getInstance().getUrl() + "/download/";
		String appName = "feiying.apk";
		try {
			appName = DBHelper.getInstance().scalar(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		url = url + appName;

		return url;
	}

	public static String getAppVersion(String device) {
		JSONObject appVer = new JSONObject();
		String sql = "SELECT version FROM fy_mobile_version WHERE type = ? ORDER BY update_time DESC LIMIT 1";
		Object[] params = new Object[] { device };
		try {
			String version = DBHelper.getInstance().scalar(sql, params);
			appVer.put("version", version);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return appVer.toString();
	}

	public static void saveFeedback(String user, String comment, String type) {
		String sql = "INSERT INTO fy_feedback(user, comment, type) VALUES(?,?,?)";
		try {
			DBHelper.getInstance().update(sql,
					new Object[] { user, comment, type });
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get user business status
	 * 
	 * @param username
	 * @return
	 * @throws SQLException 
	 */
	public static String getBusinessStatus(String username) throws SQLException {
		String sql = "SELECT business_status AS status FROM fy_user WHERE username=?";
		String status = DBHelper.getInstance().scalar(sql,
				new Object[] { username });
		return status;
	}
}
