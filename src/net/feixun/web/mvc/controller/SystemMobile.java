package net.feixun.web.mvc.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.feixun.impl.User;

@Controller
@RequestMapping("/mobile/system")
public class SystemMobile {

	private static Log log = LogFactory.getLog(VideoMobile.class);

	@RequestMapping("/login")
	public void login(
			@RequestParam(value = "loginName") String loginName,
			@RequestParam(value = "loginPwd") String loginPwd,
			@RequestParam(value = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "model", required = false, defaultValue = "") String model,
			@RequestParam(value = "release", required = false, defaultValue = "") String release,
			@RequestParam(value = "sdk", required = false, defaultValue = "") String sdk,
			@RequestParam(value = "width", required = false, defaultValue = "0") String width,
			@RequestParam(value = "height", required = false, defaultValue = "0") String height,
			HttpServletResponse response, HttpSession session) throws Exception {
		log.info("login loginname: " + loginName + " pwd: " + loginPwd);
		JSONObject jsonUser = new JSONObject();
		try {
			String result = User.login(session, loginName, loginPwd);
			if (result.equals("0")) {
				User.recodeDeviceInfo(loginName, brand, model, release, sdk,
						width, height);
			}
			jsonUser.put("result", result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		log.info("result: " + jsonUser.toString());
		response.getWriter().print(jsonUser.toString());
	};

	@RequestMapping("/getPhoneCode")
	public void getPhoneCode(@RequestParam(value = "phone") String phone,
			HttpServletResponse response, HttpSession session) throws Exception {
		JSONObject jsonUser = new JSONObject();
		try {
			String result = User.getPhoneCode(session, phone);
			jsonUser.put("result", result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response.getWriter().print(jsonUser.toString());
	};

	@RequestMapping("/checkPhoneCode")
	public void checkPhoneCode(@RequestParam(value = "code") String code,
			HttpServletResponse response, HttpSession session) throws Exception {
		JSONObject jsonUser = new JSONObject();
		try {
			String result = "0";
			if (session.getAttribute("phonecode") != null) {
				result = User.checkPhoneCode(session, code);
			} else {
				result = "6"; // session timeout
			}
			jsonUser.put("result", result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response.getWriter().print(jsonUser.toString());
	};

	@RequestMapping("/regUser")
	public void regUser(@RequestParam(value = "password") String password,
			@RequestParam(value = "password1") String password1,
			HttpServletResponse response, HttpSession session) throws Exception {
		JSONObject jsonUser = new JSONObject();
		try {
			String result = "";
			String phone = "";
			if (session.getAttribute("phonenumber") != null) {
				phone = (String) session.getAttribute("phonenumber");
				result = User.regUser(phone, password, password1);
			} else {
				result = "6"; // session过期
			}
			jsonUser.put("result", result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response.getWriter().print(jsonUser.toString());
	};

	@RequestMapping("/reglogin")
	public void registerAndLogin(
			@RequestParam(value = "code") String code,
			@RequestParam(value = "brand", required = false, defaultValue = "") String brand,
			@RequestParam(value = "model", required = false, defaultValue = "") String model,
			@RequestParam(value = "release", required = false, defaultValue = "") String release,
			@RequestParam(value = "sdk", required = false, defaultValue = "") String sdk,
			@RequestParam(value = "width", required = false, defaultValue = "0") String width,
			@RequestParam(value = "height", required = false, defaultValue = "0") String height,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		JSONObject jsonUser = new JSONObject();
		try {
			String result = "0";
			if (session.getAttribute("phonecode") != null) {
				result = User.checkPhoneCode(session, code);

				if (result.equals("0")) {
					// get user info
					String phone = (String) session.getAttribute("phonenumber");
					if (phone != null) {
						JSONObject info = User.getUserInfo(phone, brand);
						jsonUser.put("info", info);
						User.recodeDeviceInfo(phone, brand, model, release, sdk, width,
								height);
					} else {
						result = "6"; // session过期
					}
				}

			} else {
				result = "6"; // session timeout
			}
			
			jsonUser.put("result", result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response.getWriter().print(jsonUser.toString());
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping("/auth")
	public void authenticate() {
		
	}
	
}
