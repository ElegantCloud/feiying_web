package net.feixun.web.mvc.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.feixun.constant.Channels;
import com.feixun.impl.CommonOp;
import com.feixun.impl.Fav;
import com.feixun.impl.Movie;
import com.feixun.impl.Series;
import com.feixun.impl.Video;

@Controller
public class FeixunController {

	private static Log log = LogFactory.getLog(FeixunController.class);
	public static final int pageSize = 20;

	@RequestMapping("")
	public String index() throws Exception {
		return "index";
	}

	@RequestMapping("/mobile")
	public String mobile() throws Exception {
		return "mobileIndex";
	}

	@RequestMapping("/play/{channel}/{sourceId}")
	public ModelAndView play(@PathVariable String sourceId,
			@PathVariable String channel) throws Exception {
		ModelAndView mv = new ModelAndView();
		Map<String, Object> bean = null;
		List<Map<String, Object>> episodeList = null;
		if (Integer.parseInt(channel) == Channels.MOVIE) {
			mv.setViewName("movie");
			bean = Movie.getMoiveBySourceId(sourceId);
		} else if (Integer.parseInt(channel) == Channels.SERIES) {
			mv.setViewName("series");
			bean = Series.getSeriesBySourceId(sourceId);
			episodeList = Series.getEpisodeList(sourceId);
		} else {
			mv.setViewName("video");
			bean = Video.getVideoBySourceId(sourceId);
		}
		if (bean == null) {
			mv.setViewName("error");
			mv.addObject("status", HttpStatus.NOT_FOUND.value());
		} else {
			mv.addObject("bean", bean);
			if (episodeList != null) {
				mv.addObject("episodeList", episodeList);
			}
		}

		return mv;
	}

	@RequestMapping("/mobile/recordPlayCount")
	public void recordPlayCount(
			@RequestParam(value = "sourceId") String sourceId,
			HttpServletResponse response) {
		log.info("recordPlayCount - sourceid: " + sourceId);
		Video.record_fy_video_info(sourceId, Fav.PLAY, Fav.INCREASE, 1);
	}

	@ResponseStatus(HttpStatus.MOVED_TEMPORARILY)
	@RequestMapping("/appget/{device}")
	public void appDownload(@PathVariable String device,
			HttpServletResponse response) {
		String url = CommonOp.getAppDownloadURL(device);
		response.addHeader("Location", url);
	}

	@RequestMapping("/version/{device}")
	public @ResponseBody
	String getAppVersion(@PathVariable String device) {
		String version = CommonOp.getAppVersion(device);
		return version;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping("/feedback")
	public void feedback(
			@RequestParam(value = "user", defaultValue = "", required = false) String user,
			@RequestParam(value = "comment", defaultValue = "", required = true) String comment,
			@RequestParam(value = "type", defaultValue = "problem") String type) {
		CommonOp.saveFeedback(user, comment, type);
	}
}
