package com.feixun.framework.service.server;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.feixun.db.DBHelper;
import com.feixun.utity.ConfigManager;

public class ContextLoaderServlet extends ContextLoaderListener {

	public void contextDestroyed(ServletContextEvent event) {
		//
		DBHelper.getInstance().closeAll();
		DBHelper.getCoreSeekInstance().closeAll();	
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		ServletContext context = event.getServletContext();

		// load configuration file
		InputStream configStream = context
				.getResourceAsStream("/WEB-INF/config/Configuration.properties");
		ConfigManager.getInstance().loadConfig(configStream);

		ApplicationContext appContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);
		
		DBHelper.getInstance().setAppContext(appContext);
		DBHelper.getCoreSeekInstance().setAppContext(appContext);
	}

}
