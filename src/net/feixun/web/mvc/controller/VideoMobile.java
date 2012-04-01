package net.feixun.web.mvc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.feixun.constant.Channels;
import com.feixun.impl.Movie;
import com.feixun.impl.Series;
import com.feixun.impl.Video;
import com.feixun.utity.MobileList;
import com.feixun.utity.Pager;

@Controller
@RequestMapping("/mobile/video")
public class VideoMobile {
	public static final int pageSize = 20;

	/**
	 * 根据视频类别获取视频列表
	 * 
	 * @param offset
	 * @param response
	 * @param channel
	 *            - 0 (video) , 1 (movie), 2 (series)
	 * @throws Exception
	 */
	@RequestMapping("/{channel}")
	public void list(
			@RequestParam(value = "offset", required = false, defaultValue = "1") int offset,
			HttpServletResponse response, @PathVariable int channel)
			throws Exception {
		String url = "/mobile/video/" + channel + "?";
		int count = 0;
		List<Map<String, Object>> list = null;
		if (channel == Channels.VIDEO) {
			count = Video.getListCount();
			list = Video.getList(offset, pageSize);
		} else if (channel == Channels.MOVIE) {
			count = Movie.getListCount();
			list = Movie.getList(offset, pageSize);
		} else if (channel == Channels.SERIES) {
			count = Series.getListCount();
			list = Series.getList(offset, pageSize);
		} else {
			count = Video.getListCountByCategory(channel);
			list = Video.getListByCategory(offset, pageSize, channel);
		}
		Pager pager = new Pager(offset, pageSize, count, url);
		response.getWriter().print(MobileList.getList(list, pager));
	}
}
