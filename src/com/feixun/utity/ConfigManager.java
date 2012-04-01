package com.feixun.utity;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Manage the configuration of phone conference server, including IP info
 * 
 * @author sk
 * 
 */
public class ConfigManager {

	private static ConfigManager cm;

	private Properties prop;

	private ConfigManager() {
		prop = new Properties();
	}

	/**
	 * get the instance of ConfigManager
	 * 
	 * @return
	 */
	public static ConfigManager getInstance() {
		if (cm == null) {
			cm = new ConfigManager();
		}

		return cm;
	}

	/**
	 * load the configuration file
	 * 
	 * @param inputStream
	 */
	public void loadConfig(InputStream inputStream) {
		try {
			InputStreamReader reader = new InputStreamReader(inputStream,
					"UTF-8");
			prop.load(reader);
			// prop.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * load the configuration file
	 * 
	 * @param FileReader
	 */
	public void loadConfig(FileReader file) {
		try {
			prop.load(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * get the property by textMessageUrl
	 * 
	 * @return
	 */
	public String getTextMessageUrl() {
		return prop.getProperty("textMessageUrl");
	}

	/**
	 * get the property by textMessageUserName
	 * 
	 * @return
	 */
	public String getTextMessageUserName() {
		return prop.getProperty("textMessageUserName");
	}

	/**
	 * get the property by textMessagePwd
	 * 
	 * @return
	 */
	public String getTextMessagePwd() {
		return prop.getProperty("textMessagePwd");
	}

	/**
	 * get the property by url
	 * 
	 * @return
	 */
	public String getUrl() {
		// TODO Auto-generated method stub
		return prop.getProperty("url");
	}

}
