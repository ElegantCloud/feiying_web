package net.feixun.web.mvc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.feixun.constant.Channels;
import com.feixun.impl.Fav;
import com.feixun.impl.FavCount;
import com.feixun.impl.Movie;
import com.feixun.impl.Series;
import com.feixun.impl.Video;
import com.feixun.utity.MobileList;
import com.feixun.utity.Pager;

@Controller
@RequestMapping("/mobile/fav")
public class FavMobile {

	public static final int pageSize = 20;

	/**
	 * 添加、删除收藏操作
	 * 
	 * @param sourceId
	 * @param username
	 * @param action
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("")
	public void favOperate(@RequestParam(value = "sourceId") String sourceId,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "action") String action,
			HttpServletResponse response) throws Exception {
		JSONObject jsonFav = new JSONObject();
		String result = "";
		try {
			if (action.equals("add")) {
				result = Fav.favVideo(username, sourceId);
			} else if (action.equals("del")) {
				result = Fav.deleteVideo(username, sourceId);
			}
			jsonFav.put("result", result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response.getWriter().print(jsonFav.toString());
	}

	/**
	 * 获取收藏频道列表
	 * 
	 * @param username
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/channellist")
	public void favChannelList(
			@RequestParam(value = "username") String username,
			HttpServletResponse response) throws Exception {
		List<Map<String, Object>> list = FavCount.getList(username);
		response.getWriter().print(MobileList.getList(list));
	}

	/**
	 * 根据频道获取收藏视频列表
	 * 
	 * @param offset
	 * @param username
	 * @param channel
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/channel/{channel}")
	public void favVideoList(
			@RequestParam(value = "offset", required = false, defaultValue = "1") int offset,
			@RequestParam(value = "username") String username,
			@PathVariable int channel, HttpServletResponse response)
			throws Exception {
		String url = "/mobile/fav/channel/" + channel + "?";
		int count = 0;
		List<Map<String, Object>> list;

		if (channel == Channels.MOVIE) {
			count = Fav.getFavListCount(username, channel);
			list = Movie.getFavList(username, channel, offset, pageSize);
		} else if (channel == Channels.SERIES) {
			count = Fav.getFavListCount(username, channel);
			list = Series.getFavList(username, channel, offset, pageSize);
		} else {
			count = Fav.getFavListCount(username, channel);
			list = Video.getFavList(username, channel, offset, pageSize);
		}

		Pager pager = new Pager(offset, pageSize, count, url);
		response.getWriter().print(MobileList.getList(list, pager));
	}

}
