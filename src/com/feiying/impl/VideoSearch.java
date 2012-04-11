package com.feiying.impl;

import java.net.ConnectException;
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
import org.sphx.api.SphinxClient;
import org.sphx.api.SphinxException;
import org.sphx.api.SphinxMatch;
import org.sphx.api.SphinxResult;

import com.feiying.bean.VideoSearchBean;
import com.feiying.db.DBHelper;
import com.feiying.utity.ConfigManager;

public class VideoSearch {
	private static Log log = LogFactory.getLog(VideoSearch.class);

	public static final int SOURCEID = 4;
	public static final int TITLE = 5;
	public static final int IMAGEURL = 6;
	public static final int CHANNEL = 3;

	public static final int TOTAL = 1;
	public static final int TOTALFOUND = 2;

	@Deprecated
	public static VideoSearchBean getList(int offset, int pageSize,
			String searchTitle) {
		VideoSearchBean videoSearchBean = new VideoSearchBean();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Integer> countMap = new HashMap<String, Integer>();
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
				countMap.put(rs.getString(1), rs.getInt(2));
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
	 * query in the sphinx server by api
	 * 
	 * @param offset
	 * @param pageSize
	 * @param keyword
	 * @return
	 * @throws Exception 
	 */
	public static VideoSearchBean queryInSphinx(int offset, int pageSize,
			String keyword) throws Exception {
		VideoSearchBean videoSearchBean = new VideoSearchBean();

		SphinxClient cl = new SphinxClient();
		String host = ConfigManager.getInstance().getSphinxHost();
		int port = ConfigManager.getInstance().getSphinxPort();
		int mode = SphinxClient.SPH_MATCH_ALL;
		String index = ConfigManager.getInstance().getSphinxIndex();
		int sortMode = SphinxClient.SPH_SORT_RELEVANCE;
		String sortClause = "";
		String groupBy = "";
		String groupSort = "";

		cl.SetServer(host, port);
		cl.SetWeights(new int[] { 100, 1 });
		cl.SetMatchMode(mode);
		cl.SetLimits(offset - 1, pageSize);
		cl.SetSortMode(sortMode, sortClause);
		if (groupBy.length() > 0) {
			cl.SetGroupBy(groupBy, SphinxClient.SPH_GROUPBY_ATTR, groupSort);
		}

		SphinxResult res = cl.Query(keyword, index);

		if (res == null) {
			log.error("Error: " + cl.GetLastError());
			throw new ConnectException(cl.GetLastError());
		}
		
		log.info("Query '" + keyword + "' retrieved " + res.total + " of "
				+ res.totalFound + " matches in " + res.time + " sec.");
		

		Map<String, Integer> countMap = new HashMap<String, Integer>();
		countMap.put("total_found", res.totalFound);
		videoSearchBean.setCountMap(countMap);
		
		// get the result list
		StringBuffer idBuf = new StringBuffer();
		idBuf.append('(');
		if (res.matches != null) {
			for (SphinxMatch info : res.matches) {
				idBuf.append(info.docId).append(',');
			}
		}
		if (idBuf.toString().endsWith(",")) {
			idBuf.replace(idBuf.length() - 1, idBuf.length(), ")");
		} else {
			idBuf.append(')');
		}
		log.info("ids: " + idBuf.toString());

		// query in mysql
		if (idBuf.length() <= 2) {
			throw new Exception("no result found");
		}
		
		String sql = "SELECT source_id, title, image_url, channel FROM fy_video WHERE id IN "
				+ idBuf.toString();
		List<Map<String, Object>> resultList = DBHelper.getInstance()
				.query(sql);
		videoSearchBean.setList(resultList);

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
		String sql = "SELECT keyword, count "
				+ "FROM fy_keywords "
				+ "WHERE DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(update_time) "
				+ "ORDER BY count DESC LIMIT 10";
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			result = DBHelper.getInstance().query(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
