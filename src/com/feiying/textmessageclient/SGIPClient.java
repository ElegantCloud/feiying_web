package com.feiying.textmessageclient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.feiying.utity.ConfigManager;

public class SGIPClient {
	private static Log log = LogFactory.getLog(SGIPClient.class);
	private static SGIPClient instance;
	private String sgipClientPath;

	private SGIPClient() {
		sgipClientPath = ConfigManager.getInstance().getAttribute(
				"sgip_client_path");
	}

	public static SGIPClient getInstance() {
		if (instance == null) {
			instance = new SGIPClient();
		}
		return instance;
	}

	public int sendSMS(String phoneNumber, String msg) {
		int retCode = 0;
		String base64Msg;
		try {
			base64Msg = Base64.encodeBase64String(msg.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			base64Msg = Base64.encodeBase64String(msg.getBytes());
		}
		
		String cmd = "python " + sgipClientPath + " -n " + phoneNumber + " -b "
				+ base64Msg;
		log.info("send sms cmd: " + cmd);
		Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
		try {
			Process p = run.exec(cmd);// 启动另一个进程来执行命令
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
			String lineStr;
			while ((lineStr = inBr.readLine()) != null)
				// 获得命令执行后在控制台的输出信息
				log.debug(lineStr);// 打印输出信息
			// 检查命令是否执行失败。
			retCode = p.waitFor();
			if (retCode != 0) {
				log.info("send sms failed - exit code: " + retCode);
			}
			inBr.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			retCode = -1;
		}
		return retCode;
	}

	public void sendValidateCode(String phone, String validateCode) throws IOException, ParserConfigurationException, SAXException{
		String content = "验证码：" + validateCode + "[联通飞影]";
		int ret = sendSMS(phone, content);
		log.info("sgip gateway return: " + ret);
	}
}
