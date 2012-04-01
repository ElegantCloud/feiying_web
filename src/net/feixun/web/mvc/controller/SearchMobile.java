package net.feixun.web.mvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.feixun.bean.VideoSearchBean;
import com.feixun.impl.VideoSearch;
import com.feixun.utity.MobileList;
import com.feixun.utity.Pager;

@Controller
public class SearchMobile {
	public static final int pageSize = 20;

	@RequestMapping("/mobile/search")
	public void search(
			@RequestParam(value = "offset", required = false, defaultValue = "1") int offset,
			@RequestParam(value = "searchTitle", required = false, defaultValue = "") String searchTitle,
			HttpServletResponse response) throws Exception {
		String url = "/mobile/search?searchTitle=" + searchTitle;
		VideoSearchBean videoSearchBean = null;
		int count = 0;
		if (searchTitle.equals("")) {
			videoSearchBean = new VideoSearchBean();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			videoSearchBean.setList(list);
		} else {
			videoSearchBean = VideoSearch
					.getList(offset, pageSize, searchTitle);
			Map<String, Object> countMap = videoSearchBean.getCountMap();
			count = Integer.parseInt((String) countMap.get("total_found"));
		}
		if (count > 0) {
			VideoSearch.countKeyword(searchTitle);
		}
		Pager pager = new Pager(offset, pageSize, count, url);
		response.getWriter().print(
				MobileList.getList(videoSearchBean.getList(), pager));
	}
	
	@RequestMapping("/keywords")
	public void getHotKeywords(HttpServletResponse response) throws IOException {
		response.getWriter().print(
				MobileList.getList(VideoSearch.getHotKeywords()));
	}

}
