package net.feixun.web.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.feixun.impl.User;

/**
 * 拦截所有的请求并判断该用户是否已经登录，如果没有登录那么跳转到登录页面。 如果用户已经登录，那么跳转到内页。
 * 如果用户设置了保存登录状态，那么检查其session是否过期，如果已过期需要读取数据库中保存的登录信息 来恢复其session。
 * 
 * @author huuguanghui
 * 
 */
public class AuthInterceptor implements HandlerInterceptor {
	private static Log log = LogFactory.getLog(AuthInterceptor.class);	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception e)
			throws Exception {
		//
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj, ModelAndView mv) throws Exception {
		//
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object obj) throws Exception {
		String uri = request.getRequestURI().toString();
		
		HttpSession session = request.getSession();
		if (session.getAttribute(User.SESSION_BEAN) != null) {
			return true;
		}

		return gotoHomePage(request, response, uri);
	}

	private boolean gotoHomePage(HttpServletRequest request,
			HttpServletResponse response, String uri) throws IOException {
		
		
		if (uri.equals("/")) {
			return true;
		} else {
			String xmlHttpRequest = request.getHeader("X-Requested-With");
			if ("XMLHttpRequest".equals(xmlHttpRequest)) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			} else {
				response.sendRedirect("/");
			}
			return false;
		}
	}

}