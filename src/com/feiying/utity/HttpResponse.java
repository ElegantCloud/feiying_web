package com.feiying.utity;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.feiying.constant.Constant;

/**
 * DonkeyResponse
 * 
 * @author sk
 * 
 */
public class HttpResponse {
	private int httpStatusCode;

	private JSONObject jsonData;

	public HttpResponse(int status, JSONObject data) {
		this.httpStatusCode = status;
		this.jsonData = data;
		try {
			if (jsonData != null) {
				jsonData.put("status", httpStatusCode);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public HttpResponse(int status, String content) {
		this.httpStatusCode = status;
		if (null != content && content.length() > 0) {
			try {
				this.jsonData = new JSONObject(content);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public HttpResponse(int status) {
		this(status, new JSONObject());
	}

	public int getStatusCode() {
		return httpStatusCode;
	}

	public JSONObject getContent() {
		return jsonData;
	}

	public String toString() {
		String msg = "";
		if (jsonData != null) {
			msg = jsonData.toString();
		}
		return msg;
	}

	public void put(String key, Object value) {
		if (null == jsonData) {
			jsonData = new JSONObject();
		}
		try {
			jsonData.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * respond to user
	 * 
	 * @param response
	 */
	public void respond(HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(httpStatusCode);
		try {
			PrintWriter out = response.getWriter();
			out.print(this.toString());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static HttpResponse OK() {
		HttpResponse response = new HttpResponse(HttpServletResponse.SC_OK);
		return response;
	}

	public static HttpResponse Unauthorized() {
		HttpResponse response = new HttpResponse(
				HttpServletResponse.SC_UNAUTHORIZED);
		response.put(Constant.reason.name(), "Unauthorized.");
		return response;
	}


	public static HttpResponse NotFound(String session) {
		HttpResponse response = new HttpResponse(
				HttpServletResponse.SC_NOT_FOUND);
		response.put(Constant.reason.name(), "The conference [" + session
				+ "] is not existed.");
		return response;
	}

	public static HttpResponse InternalException(String msg) {
		HttpResponse response = new HttpResponse(
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.put(Constant.reason.name(), msg);
		return response;
	}

	public static HttpResponse BadRequest() {
		HttpResponse response = new HttpResponse(
				HttpServletResponse.SC_BAD_REQUEST);
		response.put(Constant.reason.name(), "Invalid Request Parameters!");
		return response;
	}

}
