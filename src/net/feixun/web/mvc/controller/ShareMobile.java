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
@RequestMapping("/mobile/share")
public class ShareMobile {
	public static final int pageSize = 20;

	@RequestMapping(value = "/add")
	public void add(@RequestParam(value = "sourceId") String sourceId,
			@RequestParam(value = "phoneStr") String phoneStr,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "channel") int channel,
			@RequestParam(value = "info", defaultValue = " ") String info,
			HttpServletResponse response) throws Exception {
		JSONObject jsonShare = new JSONObject();
		String result = "";
		try {
			result = Share.shareVideo(username, sourceId, phoneStr, info,
					channel);

			jsonShare.put("result", result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response.getWriter().print(jsonShare.toString());
	}

	@RequestMapping(value = "/del")
	public void shareDelete(@RequestParam(value = "shareId") String shareId,
			HttpServletResponse response) throws Exception {
		JSONObject jsonShare = new JSONObject();
		try {
			String result = Share.delele_fy_share(shareId);
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
		String url = "/mobile/share/list?";
		int count = Share.get_fy_share_count(username);
		List<Map<String, Object>> videolist = Share.get_fy_share_list(username,
				offset, pageSize);
		Pager pager = new Pager(offset, pageSize, count, url);
		response.getWriter().print(MobileList.getList(videolist, pager));
	}

	@RequestMapping("/recvs")
	public void shareRecvs(
			@RequestParam(value = "shareId") String shareId,
			HttpServletResponse response) throws Exception {
		List<Map<String, Object>> recvlist = Share
				.get_fy_share_recv_recvs(shareId);
		response.getWriter().print(MobileList.getList(recvlist));
	}

}
