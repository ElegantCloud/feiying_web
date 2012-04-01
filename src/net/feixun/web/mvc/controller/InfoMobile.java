package net.feixun.web.mvc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.feixun.constant.Channels;
import com.feixun.impl.Movie;
import com.feixun.impl.Series;
import com.feixun.impl.Video;

@Controller
@RequestMapping("/mobile/info")
public class InfoMobile {

	@RequestMapping("/{channel}/{sourceId}")
	public void info(HttpServletResponse response, @PathVariable int channel,
			@PathVariable String sourceId) throws Exception {
		
		Map<String, Object> info = null;
		if (channel == Channels.MOVIE) {
			info = Movie.getMoiveBySourceId(sourceId);
		} else if (channel == Channels.SERIES) {
			info = Series.getSeriesBySourceId(sourceId);
			List<Map<String, Object>> list = Series.getEpisodeList(sourceId);
			info.put("list", list);
		} else {
			info = Video.getVideoBySourceId(sourceId);
		}
		JSONObject jsonObject = new JSONObject(info);
		response.getWriter().print(jsonObject.toString());
	}
}
