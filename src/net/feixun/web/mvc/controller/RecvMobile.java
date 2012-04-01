package net.feixun.web.mvc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.feixun.impl.Share;
import com.feixun.utity.MobileList;
import com.feixun.utity.Pager;

@Controller
@RequestMapping("/mobile/recv")
public class RecvMobile {
	public static final int pageSize = 20;

	@RequestMapping(value = "/del")
	public void recvDelete(
			@RequestParam(value = "shareId") String shareId,
			@RequestParam(value = "username") String username,
			HttpServletResponse response) throws Exception {
		JSONObject jsonShare = new JSONObject();
		try {
			String result = Share.delele_fy_share_recv(shareId, username);
			jsonShare.put("result", result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response.getWriter().print(jsonShare.toString());
	}

	@RequestMapping("/list")
	public void list(
			@RequestParam(value = "offset", required = false, defaultValue = "1") int offset,
			@RequestParam(value = "username") String username,
			HttpServletResponse response) throws Exception {
		String url = "/mobile/recv/list?";
		int count = Share.get_fy_share_recv_count(username);
		List<Map<String, Object>> videolist = Share.get_fy_share_recv_list(
				username, offset, pageSize);
		Pager pager = new Pager(offset, pageSize, count, url);
		response.getWriter().print(MobileList.getList(videolist, pager));
	}

}
