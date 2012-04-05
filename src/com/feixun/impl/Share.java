package com.feixun.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.feixun.db.DBHelper;
import com.feixun.utity.ValidatePattern;

public class Share {

	public static final String SEND_TYPE = "send";
	public static final String RECEIVE_TYPE = "receive";

	/**
	 * 用户分享视频
	 * 
	 * @param username
	 * @param sourceId
	 * @param phoneStr
	 * @param info
	 * @param channel
	 * @return
	 */
	public static String shareVideo(String username, String sourceId,
			String phoneStr, String info, int channel) {
		String result = "";
		String[] phoneList = phoneStr.split(",");
		result = Video.record_fy_video_info(sourceId, Fav.SHARE, Fav.INCREASE,
				phoneList.length); // 视频分享量+1
		if (result.equals("0")) {
			int share_id = create_fy_share(username, sourceId); // 视频与用户关联
			if (share_id != 0) {
				for (String phone : phoneList) {
					if (ValidatePattern.isValidMobilePhone(phone)) {
						result = create_fy_share_recv(share_id, phone); // 视频与用户关联
					}
				}
			}
		}
		return result;
	}

	/**
	 * 分享视频 视频与发送者关联
	 * 
	 * @param username
	 * @param sourceId
	 * @return
	 */
	public static int create_fy_share(String username, String sourceId) {
		int share_id = (sourceId + username + System.currentTimeMillis())
				.hashCode();
		String sql = "INSERT INTO fy_share(share_id,send,send_state,source_id)"
				+ " VALUE (?,?,?,?) ";
		Object[] params = new Object[] { share_id, Long.parseLong(username),
				"create", sourceId };
		try {
			share_id = DBHelper.getInstance().update(sql, params) > 0 ? share_id
					: 0;
		} catch (SQLException e) {
			e.printStackTrace();
			share_id = 0;
		}
		return share_id;
	}

	/**
	 * 分享视频 视频与接收者关联
	 * 
	 * @param shareId
	 * @param phone
	 * @return
	 */
	public static String create_fy_share_recv(int shareId, String phone) {
		String result = "";
		String sql = "INSERT INTO fy_share_recv(share_id,receive,receive_state) "
				+ "VALUE (?,?,?)";
		Object[] params = new Object[] { shareId, phone, "create" };
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
	 * 判断手机号码是否正确
	 * 
	 * @param phoneStr
	 * @return
	 */
	public static String checkRegisterPhone(String phoneStr) {
		String[] phoneList = phoneStr.split(",");
		for (String phone : phoneList) {
			if (phone.equals("")) {
				return "1"; // 手机号码必填
			} else if (!ValidatePattern.isValidMobilePhone(phone)) {
				return "1"; // 手机号码格式错误
			}
		}
		return "0";
	}

	/**
	 * 删除该用户分享的视频
	 * 
	 * @param id
	 * @return
	 */
	public static String delele_fy_share(String id) {
		String result = "";
		try {
			String sql = "UPDATE fy_share SET send_state = ? "
					+ "WHERE share_id = ? ";
			Object[] params = new Object[] { "delete", id };
			int resultCount = DBHelper.getInstance().update(sql, params);
			result = resultCount > 0 ? "0" : "1001";
		} catch (SQLException e) {
			e.printStackTrace();
			result = "1001";
		}
		return result;
	}

	/**
	 * 删除该用户接收的视频
	 * 
	 * @param id
	 * @return
	 */
	public static String delele_fy_share_recv(String shareId, String username) {
		String result = "";
		try {
			String sql = "UPDATE fy_share_recv SET receive_state = ? "
					+ "WHERE share_id = ? AND receive = ?";
			Object[] params = new Object[] { "delete", shareId, username };
			int resultCount = DBHelper.getInstance().update(sql, params);
			result = resultCount > 0 ? "0" : "1001";
		} catch (SQLException e) {
			e.printStackTrace();
			result = "1001";
		}
		return result;
	}

	/**
	 * 该用户分享视频的总量
	 * 
	 * @param send
	 * @return
	 * @throws SQLException
	 */
	public static int get_fy_share_count(String send) throws SQLException {
		String sql = "SELECT count(share_id) FROM fy_share "
				+ "WHERE send = ? AND send_state = ? ";
		Object[] params = new Object[] { Long.parseLong(send), "create" };
		return DBHelper.getInstance().count(sql, params);
	}

	/**
	 * 该用户接收的视频的总量
	 * 
	 * @param receive
	 * @return
	 * @throws SQLException
	 */
	public static int get_fy_share_recv_count(String receive)
			throws SQLException {
		String sql = "SELECT count(share_id) FROM fy_share_recv "
				+ "WHERE receive = ? AND receive_state = ?";
		Object[] params = new Object[] { Long.parseLong(receive), "create" };
		return DBHelper.getInstance().count(sql, params);
	}

	/**
	 * 该用户分享的视频
	 * 
	 * @param username
	 * @param currPage
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> get_fy_share_list(String username,
			int currPage, int pageSize) throws SQLException {
		String sql = "SELECT s.share_id,"
				+ "UNIX_TIMESTAMP(s.created_time) as share_time,"
				+ "v.source_id," + "v.title," + "v.image_url," + "v.channel,"
				+ "v.play_count," + "v.fav_count," + "v.share_count "
				+ "FROM fy_share s "
				+ "LEFT JOIN fy_video v on s.source_id = v.source_id "
				+ "WHERE s.send = ? AND s.send_state = ? "
				+ "ORDER BY v.created_time DESC";
		Object[] params = new Object[] { Long.parseLong(username), "create" };
		return DBHelper.getInstance().queryPager(sql, params, currPage,
				pageSize);
	}

	/**
	 * 该用户分享的视频接收者信息
	 * 
	 * @param shareId
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> get_fy_share_recv_recvs(
			String shareId) throws SQLException {
		String sql = "SELECT * FROM fy_share_recv WHERE share_id = ?";
		Object[] params = new Object[] { shareId };
		return DBHelper.getInstance().query(sql, params);
	}

	/**
	 * 该用户接收的视频
	 * 
	 * @param username
	 * @param currPage
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> get_fy_share_recv_list(
			String username, int currPage, int pageSize) throws SQLException {
		String sql = "SELECT r.share_id," + "s.send,"
				+ "UNIX_TIMESTAMP(s.created_time) as share_time,"
				+ "v.source_id," + "v.title," + "v.image_url," + "v.channel,"
				+ "v.play_count," + "v.fav_count," + "v.share_count "
				+ "FROM fy_share_recv r "
				+ "LEFT JOIN fy_share s ON r.share_id = s.share_id "
				+ "LEFT JOIN fy_video v ON s.source_id = v.source_id "
				+ "WHERE r.receive = ? AND r.receive_state = ? "
				+ "ORDER BY s.created_time DESC";
		Object[] params = new Object[] { Long.parseLong(username), "create" };
		return DBHelper.getInstance().queryPager(sql, params, currPage,
				pageSize);
	}
}
